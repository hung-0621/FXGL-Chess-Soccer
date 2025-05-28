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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RegisterScene extends SubScene {

    public RegisterScene() {
        var background = UiManager.createBackground("/MainMenu.jpg");
        var title = UiManager.createTitle(100, 100, "Register");
        var backButton = UiManager.createBackButton(50, 100, getContentRoot());
        var RegisterForm = createRegisterFrom();

        getContentRoot().getChildren().addAll(background, title, RegisterForm, backButton);
    }

    private VBox createRegisterFrom() {

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setMinSize(200, 20);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMinSize(200, 20);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMinSize(200, 20);

        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm Password");
        confirmPassField.setMinSize(200, 20);

        Button submit = new Button("Submit");

        Text userLabel = new Text("Username");
        userLabel.setFill(Color.WHITE);
        userLabel.setFont(FXGL.getUIFactoryService().newFont(20));

        Text emailLabel = new Text("Email");
        emailLabel.setFill(Color.WHITE);
        emailLabel.setFont(FXGL.getUIFactoryService().newFont(20));

        Text passLabel = new Text("Password");
        passLabel.setFill(Color.WHITE);
        passLabel.setFont(FXGL.getUIFactoryService().newFont(20));

        Text confirmPassLabel = new Text("Confirm Password");
        confirmPassLabel.setFill(Color.WHITE);
        confirmPassLabel.setFont(FXGL.getUIFactoryService().newFont(20));

        submit.setOnAction(e -> onSubmit(userField.getText(), emailField.getText(), passField.getText(),
                confirmPassField.getText()));
        VBox root = new VBox(userLabel, userField, emailLabel, emailField, passLabel, passField, confirmPassLabel,
                confirmPassField, submit);
        root.setSpacing(20);
        root.setAlignment(Pos.CENTER_LEFT);
        submit.setAlignment(Pos.CENTER_RIGHT);
        root.setTranslateX(100);
        root.setTranslateY(200);
        return root;
    }

    private void onSubmit(String user, String email, String pass, String confirmPass) {
        if (user.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            System.out.println("Error: 請輸入所有欄位");
            Platform.runLater(() -> {
                FXGL.getDialogService().showErrorBox("請輸入所有欄位", () -> {
                });
            });
            return;
        }

        if (!pass.equals(confirmPass)) {
            System.out.println("Error: 密碼不一致");
            Platform.runLater(() -> {
                FXGL.getDialogService().showErrorBox("密碼不一致", () -> {
                });
            });
            return;
        }

        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("userName", user);
        node.put("email", email);
        node.put("password", pass);
        API.getRegisterInfo(node)
                .thenAccept(success -> {
                    if (success) {
                        Platform.runLater(() -> {
                            FXGL.getDialogService().showConfirmationBox("註冊成功！是否前往登入？", ans -> {
                                if (ans) {
                                    SourceGetter.runLeaveAnimation(getContentRoot(), () -> {
                                        FXGL.getSceneService().popSubScene(); // main
                                        FXGL.getSceneService().pushSubScene(new LoginScene());
                                    });
                                } else {
                                    SourceGetter.runLeaveAnimation(getContentRoot(), () -> {
                                        FXGL.getSceneService().popSubScene(); // main
                                    });
                                }
                            });
                        });
                    } else {
                        // 註冊失敗
                    }
                });

    }

}
