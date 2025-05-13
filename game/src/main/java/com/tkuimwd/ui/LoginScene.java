package com.tkuimwd.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.tkuimwd.Config;
import com.tkuimwd.api.API;
import com.tkuimwd.factory.MenuFactory;
import com.tkuimwd.util.Fetch;

import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

public class LoginScene extends SubScene {

    public LoginScene() {
        var background = createBackground();
        var title = createTitle();
        var loginForm = createloginFrom();
        var backButton = createBackButton();

        getContentRoot().getChildren().addAll(background, title, loginForm, backButton);
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
        Text title = FXGL.getUIFactoryService().newText("Login", Color.WHITE, 40);
        title.setTranslateX(100);
        title.setTranslateY(100);
        return title;
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
                    if (token != null) {

                        // 儲存 token 進 Config
                        Config.token = token;
                        System.out.println("[LoginScene] 儲存成功：token = " + Config.token);

                        Platform.runLater(() -> {
                            Util.runLeaveAnimation(getContentRoot(), () -> {
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
