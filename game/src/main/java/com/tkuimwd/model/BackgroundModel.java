package com.tkuimwd.model;

public class BackgroundModel{
    
    private final String backgroundImagePath;
    
    public BackgroundModel(){
        this.backgroundImagePath = "/field.jpg";
    }

    public BackgroundModel(String backgroundImagePath){
        this.backgroundImagePath = backgroundImagePath;
    }

    public String get_background_image_path(){
        return backgroundImagePath;
    }

}