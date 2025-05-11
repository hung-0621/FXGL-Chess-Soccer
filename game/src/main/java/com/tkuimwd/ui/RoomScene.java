package com.tkuimwd.ui;

import java.io.InputStream;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.tkuimwd.Config;

import javafx.animation.FadeTransition;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class RoomScene extends SubScene {

    public RoomScene() {
        var background = createBackground();
        var mainBox = createMainBox();
        var title = createTitle();
        var backButton = createBackButton();
        var p1_nameHolder = createNameHolder(250, 400, "Player 1");
        var p2_nameHolder = createNameHolder(450, 400, "Player 2");
        getContentRoot().getChildren().addAll(background, mainBox, title, backButton, p1_nameHolder, p2_nameHolder);
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
        Text backButton = new Text("🔙");
        backButton.setTranslateX(230);
        backButton.setTranslateY(150);
        backButton.setFill(Color.WHITE);
        backButton.setFont(FXGL.getUIFactoryService().newFont(30));
        backButton.setOnMouseClicked(e -> {
            FXGL.getSceneService().popSubScene();
        });
        return backButton;
    }

    private Group createNameHolder(double x, double y, String name) {
        Rectangle nameHolder = new Rectangle(100, 40);
        nameHolder.setTranslateX(x);
        nameHolder.setTranslateY(y);
        nameHolder.setFill(null);
        
        Text user_name = new Text(name);
        user_name.setFill(Color.WHITE);
        user_name.setFont(FXGL.getUIFactoryService().newFont(20));
        user_name.setViewOrder(-1);

        Text nameLabel = createNameLabel(name, x, y - 5);

        Group holder = new Group();
        holder.getChildren().addAll(nameHolder, user_name, nameLabel);
        return holder;
    }

    private Text createNameLabel(String name, double x, double y) {
        Text nameLabel = FXGL.getUIFactoryService().newText(name, Color.WHITE, 20);
        nameLabel.setTranslateX(x);
        nameLabel.setTranslateY(y);
        return nameLabel;
    }

    private void onSubmit(String user, String pass) {
        // 驗證邏輯...
        // 1) 播放淡出動畫
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), getContentRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(evt -> {
            // 2) 移除 SubScene 並回到底層場景
            FXGL.getSceneService().popSubScene();
            // 3) (可選) 顯示登入結果或進入下一個畫面
        });
        fadeOut.play();
    }
}
