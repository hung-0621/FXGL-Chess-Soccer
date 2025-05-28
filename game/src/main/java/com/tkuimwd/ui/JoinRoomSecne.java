package com.tkuimwd.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tkuimwd.api.API;
import com.tkuimwd.ui.util.SourceGetter;
import com.tkuimwd.ui.util.UiManager;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class JoinRoomSecne extends SubScene {

    private String token;

    public JoinRoomSecne(String token) {
        this.token = token;
        var title = UiManager.createTitle(100, 100,"Join");
        var background = UiManager.createBackground("/MainMenu.jpg");
        var backButton = UiManager.createBackButton(50,100, getContentRoot());
        var mainBox = createMainBox();

        getContentRoot().getChildren().addAll(background, title, mainBox, backButton);
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

    private void onSubmit(String roomCode, String token) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("roomCode", roomCode);
        node.put("token", token);
        API.getJoinRoomInfo(node)
                .thenAccept(info -> {
                    if (info != null) {
                        String code = info.substring(7, info.length());
                        System.out.println("roomCode:" + code);
                        SourceGetter.runLeaveAnimation(getContentRoot(), () -> {
                            FXGL.getSceneService().pushSubScene(new CreateRoomScene(false, code,null, token));
                        });
                    }
                }).exceptionally((e) -> {
                    Platform.runLater(() -> {
                        FXGL.getDialogService().showErrorBox("加入房間失敗", () -> {
                            SourceGetter.runLeaveAnimation(getContentRoot(), () -> {
                                FXGL.getSceneService().popSubScene(); // main
                            });
                        });
                    });
                    System.out.println("Error: " + e.getMessage());
                    return null;
                });
    }
}
