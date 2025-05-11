package com.tkuimwd.ui;

import java.io.InputStream;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import com.tkuimwd.Config;

import javafx.animation.FadeTransition;
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

        public MainMenu() {
                super(MenuType.MAIN_MENU);
                initUI();
        }

        private void initUI() {
                var background = getBackground();
                var title = createTitle();
                var buttons = createButton();
                getContentRoot().getChildren().addAll(background, title, buttons);
        }

        @Override
        public void onCreate() {

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
                MainButton loginButton = new MainButton("Login", () -> {
                        FXGL.getSceneService().pushSubScene(new LoginScene());
                });
                MainButton registerButton = new MainButton("Register", () -> {
                        FXGL.getSceneService().pushSubScene(new RegisterScene());
                });
                MainButton startButton = new MainButton("Start Game", () -> {
                        FXGL.getSceneService().pushSubScene(new RoomScene());
                });
                MainButton exitButton = new MainButton("Logout", () -> fireExit());
                VBox vbox = new VBox(15, loginButton, registerButton, startButton, exitButton);
                vbox.setTranslateX(100);
                vbox.setTranslateY(250);
                vbox.setAlignment(Pos.CENTER_LEFT);
                return vbox;
        }

        @Override
        protected void onUpdate(double tpf) {

        }

        private static class MainButton extends StackPane {

                private final Color SELECT_COLOR = Color.WHITE;
                private final Color UNSELECT_COLOR = Color.LIGHTGRAY;

                private String name;
                private Runnable action;
                private Text text;
                private Rectangle selector;

                public MainButton(String name, Runnable action) {
                        this.name = name;
                        this.action = action;
                        text = FXGL.getUIFactoryService().newText(name, Color.WHITE, 25);
                        text.fillProperty().bind(
                                        Bindings.when(focusedProperty())
                                                        .then(SELECT_COLOR)
                                                        .otherwise(UNSELECT_COLOR));

                        selector = new Rectangle(5, 20, Color.WHITE);
                        selector.setTranslateX(-20);
                        selector.visibleProperty().bind(focusedProperty().or(hoverProperty()));

                        setAlignment(Pos.CENTER_LEFT);
                        setFocusTraversable(true);
                        setOnKeyPressed(e -> {
                                if (e.getCode() == KeyCode.ENTER) {
                                        action.run();
                                }
                        });
                        setOnMouseEntered(e -> requestFocus());
                        setOnMouseClicked(e -> action.run());
                        getChildren().addAll(selector, text);
                }
        }
}