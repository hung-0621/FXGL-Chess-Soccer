package com.tkuimwd.ui;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;

import com.tkuimwd.Config;
import com.tkuimwd.api.API;
import com.tkuimwd.ui.util.SourceGetter;
import com.tkuimwd.ui.util.UiManager;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class LoginScene extends SubScene {

    public LoginScene() {
        var background = UiManager.createBackground("/MainMenu.jpg");
        var title = UiManager.createTitle(100, 100, "Login");
        var backButton = UiManager.createBackButton(50, 100, getContentRoot());
        var loginForm = createloginFrom();

        getContentRoot().getChildren().addAll(background, title, loginForm, backButton);
    }

    private VBox createloginFrom() {

        TextField userField = new TextField();
        userField.setPromptText("Email");
        userField.setMinSize(200, 20);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMinSize(200, 20);

        Button submit = new Button("Submit");

        Text userLabel = new Text("Email");
        userLabel.setFill(Color.WHITE);
        userLabel.setFont(FXGL.getUIFactoryService().newFont(20));

        Text passLabel = new Text("Password");
        passLabel.setFill(Color.WHITE);
        passLabel.setFont(FXGL.getUIFactoryService().newFont(20));

        submit.setOnAction(e -> onSubmit(userField.getText(), passField.getText()));
        VBox root = new VBox(userLabel, userField, passLabel, passField, submit);
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER_LEFT);
        submit.setAlignment(Pos.CENTER_RIGHT);
        root.setTranslateX(100);
        root.setTranslateY(230);
        return root;
    }

    private void onSubmit(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            Platform.runLater(() -> {
                FXGL.getDialogService().showErrorBox("請輸入email跟密碼", () -> {
                });
            });
            return;
        }
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("email", email);
        node.put("password", pass);
        API.getLoginInfo(node)
                .thenAccept(token -> {
                    if (!token.isEmpty()) {

                        // 儲存 token 進 Config
                        Config.token = token;
                        System.out.println("[LoginScene] 儲存成功：token = " + Config.token);

                        Platform.runLater(() -> {
                            SourceGetter.runLeaveAnimation(getContentRoot(), () -> {
                                FXGL.getSceneService().popSubScene(); // main
                                FXGL.getSceneService().popSubScene(); // null
                                FXGL.getSceneService().pushSubScene(new MainMenu(token)); // main
                                FXGL.getDialogService().showMessageBox("登入成功！");
                            });
                        });
                    } else {
                        Platform.runLater(() -> {
                            FXGL.getDialogService().showErrorBox("登入失敗", () -> {
                            });
                        });
                    }
                });
    }
}
