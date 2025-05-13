package com.tkuimwd.ui;

import java.io.InputStream;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tkuimwd.Config;
import com.tkuimwd.api.API;
import com.tkuimwd.util.Fetch;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MainMenu extends FXGLMenu {

        private String token;

        public MainMenu() {
                super(MenuType.MAIN_MENU);
                initUI();
                token = "";
        }

        public MainMenu(String token) {
                super(MenuType.MAIN_MENU);
                initUI();
                this.token = token;
        }

        @Override
        public void onCreate() {

        }

        private void initUI() {
                var background = getBackground();
                var title = createTitle();
                var buttons = createButton();
                getContentRoot().getChildren().addAll(background, title, buttons);
        }

        private ImageView getBackground() {
                InputStream input = getClass().getResourceAsStream("/MainMenu.jpg");
                if (input == null) {
                        throw new IllegalStateException("找不到資源：" + "MainMenu.jpg");
                }
                Image image = new Image(input);
                ImageView background = new ImageView(image);
                background.setFitWidth(Config.WIDTH);
                background.setFitHeight(Config.HEIGHT);
                return background;
        }

        private Text createTitle() {
                Text title = FXGL.getUIFactoryService().newText("CHESS SOCCER", Color.WHITE, 40);
                title.setTranslateX(80);
                title.setTranslateY(100);
                return title;
        }

        private VBox createButton() {
                MainButton loginButton = new MainButton("Login", () -> toLoginScene());
                MainButton registerButton = new MainButton("Register", () -> toRegisterScene());
                MainButton createRoomButton = new MainButton("Create Room", () -> toCreateRoomScene());
                MainButton joinRoomButton = new MainButton("Join Room", () -> toJoinRoomScene());

                MainButton exitButton = new MainButton("Exit", () -> fireExit());
                VBox vbox = new VBox(15, loginButton, registerButton, createRoomButton, joinRoomButton, exitButton);
                vbox.setTranslateX(100);
                vbox.setTranslateY(250);
                vbox.setAlignment(Pos.CENTER_LEFT);
                return vbox;
        }

        private void toLoginScene() {
                FXGL.getSceneService().pushSubScene(new LoginScene());
        }

        private void toRegisterScene() {
                FXGL.getSceneService().pushSubScene(new RegisterScene());
        }

        private void toCreateRoomScene() {
                if (token.equals("") || token == null) {
                        Platform.runLater(() -> {
                                FXGL.getDialogService().showConfirmationBox("創建房間前需要先登入，是否前往登入？", (ans) -> {
                                        if (ans) {
                                                FXGL.getSceneService().pushSubScene(new LoginScene());
                                        }
                                });
                        });
                } else {
                        ObjectNode node = JsonNodeFactory.instance.objectNode();
                        node.put("token", token);
                        API.getCreateRoomInfo(node)
                                        .thenAccept(roomCode -> {
                                                if (roomCode != null) {
                                                        Platform.runLater(() -> {
                                                                FXGL.getSceneService().pushSubScene(
                                                                                new CreateRoomScene(true, roomCode,
                                                                                                token, null));
                                                        });
                                                } else {
                                                        Platform.runLater(() -> {
                                                                FXGL.getDialogService().showMessageBox(
                                                                                "創建房間失敗，請稍後再試。");
                                                        });
                                                }
                                        });
                }

        }

        private void toJoinRoomScene() {
                if (token.equals("") || token == null) {
                        Platform.runLater(() -> {
                                // show confirmation box
                                FXGL.getDialogService().showConfirmationBox("加入房間前需要先登入，是否前往登入？", (ans) -> {
                                        if (ans) {
                                                FXGL.getSceneService().pushSubScene(new LoginScene());
                                        }
                                });
                        });
                } else {
                        Platform.runLater(() -> {
                                FXGL.getSceneService().pushSubScene(new JoinRoomSecne(token));
                        });
                }
        }

        @Override
        protected void onUpdate(double tpf) {

        }
}