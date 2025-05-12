package com.tkuimwd.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tkuimwd.Config;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class JoinRoomSecne extends SubScene {

    private String token;

    public JoinRoomSecne(String token) {
        this.token = token;
        var title = createTitle();
        var background = createBackground();
        var mainBox = createMainBox();
        var backButton = createBackButton();

        getContentRoot().getChildren().addAll(background, title, mainBox, backButton);
    }

    private Text createTitle() {
        Text title = FXGL.getUIFactoryService().newText("Join", Color.WHITE, 40);
        title.setTranslateX(100);
        title.setTranslateY(100);
        return title;
    }

    private ImageView createBackground() {
        ImageView background = Util.getImageView(getContentRoot(), "/MainMenu.jpg");
        background.setFitWidth(Config.WIDTH);
        background.setFitHeight(Config.HEIGHT);
        return background;
    }

    private VBox createMainBox() {
        TextField roomCodeField = new TextField();
        roomCodeField.setPromptText("Room Code");

        Text label = FXGL.getUIFactoryService().newText("Room Code", Color.WHITE, 20);

        Button button = new Button("Join");
        button.setOnAction((e) -> onSubmit(roomCodeField.getText(), token));

        VBox mainBox = new VBox(10);
        mainBox.setTranslateX(100);
        mainBox.setTranslateY(200);
        mainBox.setAlignment(Pos.CENTER_LEFT);
        mainBox.getChildren().addAll(label, roomCodeField, button);
        return mainBox;
    }

    private Text createBackButton() {
        Text backButton = new Text("🔙");
        backButton.setTranslateX(50);
        backButton.setTranslateY(100);
        backButton.setFill(Color.WHITE);
        backButton.setFont(FXGL.getUIFactoryService().newFont(30));
        backButton.setOnMouseClicked(e -> {
            Util.runLeaveAnimation(getContentRoot(), () -> {
                FXGL.getSceneService().popSubScene(); // main
            });
        });
        return backButton;
    }

    private void onSubmit(String roomCode, String token) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("roomCode", roomCode);
        node.put("token", token);
        API.getJoinRoomInfo(node)
                .thenAccept(info -> {
                    if (info != null) {
                        String code = info.substring(7, info.length());
                        System.out.println("roomCode:" + code);
                        Util.runLeaveAnimation(getContentRoot(), () -> {
                            FXGL.getSceneService().pushSubScene(new CreateRoomScene(false, code,null, token));
                        });
                    }
                }).exceptionally((e) -> {
                    Platform.runLater(() -> {
                        FXGL.getDialogService().showErrorBox("加入房間失敗", () -> {
                            Util.runLeaveAnimation(getContentRoot(), () -> {
                                FXGL.getSceneService().popSubScene(); // main
                            });
                        });
                    });
                    System.out.println("Error: " + e.getMessage());
                    return null;
                });
    }
}
