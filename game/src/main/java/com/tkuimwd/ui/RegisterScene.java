package com.tkuimwd.ui;

import java.io.InputStream;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tkuimwd.Config;
import com.tkuimwd.api.API;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class RegisterScene extends SubScene {

    public RegisterScene() {
        var background = createBackground();
        var title = createTitle();
        var RegisterForm = createRegisterFrom();
        var backButton = createBackButton();

        getContentRoot().getChildren().addAll(background, title, RegisterForm, backButton);
    }

    private ImageView getImageView(String path) {
        InputStream input = getClass().getResourceAsStream(path);
        if (input == null) {
            throw new IllegalStateException("找不到資源：" + path);
        }
        Image image = new Image(input);
        ImageView view = new ImageView(image);
        return view;
    }

    private ImageView createBackground() {
        ImageView background = getImageView("/MainMenu.jpg");
        background.setFitWidth(Config.WIDTH);
        background.setFitHeight(Config.HEIGHT);
        return background;
    }

    private Text createTitle() {
        Text title = FXGL.getUIFactoryService().newText("Register", Color.WHITE, 40);
        title.setTranslateX(100);
        title.setTranslateY(100);
        return title;
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
                                    Util.runLeaveAnimation(getContentRoot(), () -> {
                                        FXGL.getSceneService().popSubScene(); // main
                                        FXGL.getSceneService().pushSubScene(new LoginScene());
                                    });
                                } else {
                                    Util.runLeaveAnimation(getContentRoot(), () -> {
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
