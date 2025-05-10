package com.tkuimwd.factory;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.tkuimwd.ui.MainMenu;

public class MenuFactory extends SceneFactory {
    @Override
    public FXGLMenu newMainMenu() {
        return new MainMenu(MenuType.MAIN_MENU);
    }

    // @Override
    // public FXGLMenu newGameMenu() {
    //     return new MyMenu(MenuType.GAME_MENU);
    // }
}
