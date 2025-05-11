package com.tkuimwd.ui;

import java.io.InputStream;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.scene.SubScene;
import com.tkuimwd.Config;

import javafx.animation.FadeTransition;
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
    
    private ImageView createBackground(){
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
        userField.setPromptText("Username");
        userField.setMinSize(200,20);
        
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMinSize(200,20);
        
        Button submit = new Button("Submit");
        
        
        Text userLabel = new Text("Username");
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
    
    private Text createBackButton(){
        Text backButton = new Text("🔙");
        backButton.setTranslateX(50);
        backButton.setTranslateY(100);
        backButton.setFill(Color.WHITE);
        backButton.setFont(FXGL.getUIFactoryService().newFont(30));
        backButton.setOnMouseClicked(e -> {
            FXGL.getSceneService().popSubScene();
        });
        return backButton;
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
