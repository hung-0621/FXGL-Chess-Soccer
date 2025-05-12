package com.tkuimwd.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tkuimwd.Config;
import com.tkuimwd.api.dto.MatchData;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CreateRoomScene extends SubScene {

    private double accumulator = 0;
    private static final double INTERVAL = 3;

    private Boolean isHost;
    private String roomCode;
    private String hostToken;
    private String guestToken;
    private String p1Name;
    private String p2Name;
    private MainButton button;
    private Group p1_nameHolder;
    private Group p2_nameHolder;

    private boolean isGuestReady = false;
    private boolean tokensFetched = false;
    private boolean namesFetched = false;
    private boolean isStarted = false;

    public CreateRoomScene(boolean isHost, String roomCode, String hostToken, String guestToken) {
        this.isHost = isHost;
        this.roomCode = roomCode;
        this.hostToken = hostToken;
        this.guestToken = guestToken;

        var background = createBackground();
        var mainBox = createMainBox();
        var title = createTitle();
        var backButton = createBackButton();
        var p1_nameHolder = createNameHolder(250, 350, p1Name);
        var p2_nameHolder = createNameHolder(570, 350, p2Name);
        var p1_Label = createNameLabel("Player 1", 250, 350 - 5);
        var p2_Label = createNameLabel("Player 2", 570, 350 - 5);
        var roomCodeHolder = createRoomCodeHolder(350, 230, roomCode);
        button = createButton();

        getContentRoot().getChildren().addAll(background, mainBox, title, backButton, p1_nameHolder, p2_nameHolder,
                p1_Label, p2_Label, roomCodeHolder, button);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onUpdate(double tpf) {
        accumulator += tpf;
        if (accumulator < INTERVAL)
            return;
        accumulator = 0;

        // 1) 如果是 Host，先輪詢 guestStatus
        if (isHost && !isGuestReady) {
            API.getPlayerReadyStatus(roomCode)
                    .thenAccept(r -> {
                        System.out.println("guestReady = " + r);
                        isGuestReady = Boolean.TRUE.equals(r);
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
            return;
        }

        // 2) 只有當 host/guest 的 token 都還沒抓過，才去抓
        if (!tokensFetched) {
            // Host 要抓 guestToken；Guest 要抓 hostToken
            if (isHost) {
                API.getRoomGuestToken(roomCode)
                        .thenAccept(tok -> {
                            System.out.println("got guestToken=" + tok);
                            guestToken = tok;
                        }).exceptionally(e -> {
                            e.printStackTrace();
                            return null;
                        });
            } else {
                API.getRoomHostToken(roomCode)
                        .thenAccept(tok -> {
                            System.out.println("got hostToken=" + tok);
                            hostToken = tok;
                        }).exceptionally(e -> {
                            e.printStackTrace();
                            return null;
                        });
            }
            tokensFetched = true;
            return;
        }

        // 3) 兩支 token 都有了、還沒抓過名字，就去抓
        if (!namesFetched && hostToken != null && guestToken != null) {
            // 同時拉 p1、p2 名稱
            API.getName(hostToken)
                    .thenAccept(name -> {
                        p1Name = name;
                        System.out.println("p1Name=" + p1Name);
                        Platform.runLater(() -> {
                            getContentRoot().getChildren().remove(p1_nameHolder);
                            p1_nameHolder = createNameHolder(250, 350, p1Name);
                            getContentRoot().getChildren().add(p1_nameHolder);
                        });
                    });
            API.getName(guestToken)
                    .thenAccept(name -> {
                        p2Name = name;
                        System.out.println("p2Name=" + p2Name);
                        Platform.runLater(() -> {
                            getContentRoot().getChildren().remove(p2_nameHolder);
                            p2_nameHolder = createNameHolder(570, 350, p2Name);
                            getContentRoot().getChildren().add(p2_nameHolder);
                            // 兩名字都到齊後才啟用按鈕
                            button.setClickable(true);
                        });
                    });
            namesFetched = true;
        }

        if (isStarted) {
            System.out.println("開始遊戲");
            isStarted = false;
            Platform.runLater(() -> {
                FXGL.getGameController().startNewGame();
            });
        }

        // ① 如果是 Guest 而且還沒標記開始，就去向後端問一次
        if (!isHost && !isStarted) {
            API.getMatchInfo(roomCode)
                    .thenAccept(matchData -> {
                        if (matchData != null && matchData.getMatchStatus().equals("playing")) {
                            System.out.println("取得開始資訊成功: matchId=" + matchData.getId());
                            Platform.runLater(() -> isStarted = true);
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        return null;
                    });
            return;
        }

    }

    private ImageView createBackground() {
        ImageView background = Util.getImageView();
        background.setFitWidth(Config.WIDTH);
        background.setFitHeight(Config.HEIGHT);
        ColorAdjust adjust = new ColorAdjust();
        adjust.setBrightness(-0.5);
        background.setEffect(adjust);
        return background;
    }

    private Rectangle createMainBox() {
        Rectangle mainBox = new Rectangle(600, 500, Color.GREY);
        mainBox.setArcHeight(10);
        mainBox.setArcWidth(10);
        mainBox.setTranslateX(200);
        mainBox.setTranslateY(100);
        return mainBox;
    }

    private Text createTitle() {
        Text title = FXGL.getUIFactoryService().newText("Room", Color.WHITE, 40);
        title.setTranslateX(450);
        title.setTranslateY(150);
        return title;
    }

    private Text createBackButton() {
        // todo delete room
        Text backButton = new Text("🔙");
        backButton.setTranslateX(230);
        backButton.setTranslateY(150);
        backButton.setFill(Color.WHITE);
        backButton.setFont(FXGL.getUIFactoryService().newFont(30));
        backButton.setOnMouseClicked(e -> {
            Util.runLeaveAnimation(getContentRoot(), () -> {
                FXGL.getSceneService().popSubScene();
            });
        });
        return backButton;
    }

    private Group createNameHolder(double x, double y, String name) {
        Rectangle nameHolder = new Rectangle(200, 50);
        nameHolder.setTranslateX(x);
        nameHolder.setTranslateY(y);
        nameHolder.setFill(Color.DARKGREY);
        Text user_name;
        if (name != null) {
            user_name = FXGL.getUIFactoryService().newText(name, 20);
        } else {
            user_name = FXGL.getUIFactoryService().newText("Loading...", 20);
        }
        user_name.setTranslateX(x + 10);
        user_name.setTranslateY(y + 30);
        user_name.setFill(Color.WHITE);
        user_name.setViewOrder(-1);

        Group holder = new Group();
        holder.getChildren().addAll(nameHolder, user_name);
        return holder;
    }

    private Text createNameLabel(String name, double x, double y) {
        Text nameLabel = FXGL.getUIFactoryService().newText(name, Color.WHITE, 20);
        nameLabel.setTranslateX(x);
        nameLabel.setTranslateY(y);
        return nameLabel;
    }

    private HBox createRoomCodeHolder(double x, double y, String roomCode) {
        TextField roomCodeHolder = new TextField(roomCode);
        roomCodeHolder.setEditable(false);

        Text roomCodeLable = FXGL.getUIFactoryService().newText("Room Code:", 17);
        roomCodeLable.setFill(Color.WHITE);
        roomCodeLable.setViewOrder(-1);

        HBox holder = new HBox(10);
        holder.setTranslateX(x);
        holder.setTranslateY(y);
        holder.getChildren().addAll(roomCodeLable, roomCodeHolder);
        return holder;
    }

    private MainButton createButton() {
        String buttonText = isHost ? "Start Game" : "Ready";
        MainButton button = new MainButton(buttonText, () -> onSubmit());
        if ("Start Game".equals(buttonText)) {
            button.setClickable(false);
        }
        button.setTranslateX(450);
        button.setTranslateY(500);
        return button;
    }

    private void onSubmit() {
        if (!isHost) {
            ObjectNode node = JsonNodeFactory.instance.objectNode();
            node.put("roomCode", roomCode);
            node.put("token", guestToken);
            API.setPlayerReady(node)
                    .thenAccept(success -> {
                        if (success) {
                            System.out.println("玩家準備好了");
                            button.setClickable(false);
                        } else {
                            System.out.println("玩家準備失敗");
                        }
                    })
                    .exceptionally((ex) -> {
                        System.out.println("Error: " + ex.getMessage());
                        return null;
                    });
        } else {
            ObjectNode node = JsonNodeFactory.instance.objectNode();
            node.put("roomCode", roomCode);
            node.put("token", hostToken);
            API.getStart(node)
                    .thenAccept(matchData -> {
                        if (matchData != null) {
                            System.out.println("matchId=" + matchData.getId());

                            // **一定要切回 JavaFX Thread**，才能安全地呼 FXGL API
                            Platform.runLater(() -> {
                                isStarted = true;
                            });
                        } else {
                            // Platform.runLater(() -> FXGL.getDialogService().showErrorBox("無法啟動遊戲，後端回傳
                            // null"),()->{});
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        // Platform.runLater(() -> FXGL.getDialogService().showErrorBox("開始遊戲失敗：" +
                        // ex.getMessage()));
                        return null;
                    });
        }
    }

}
