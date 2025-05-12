package com.tkuimwd.ui;

import com.almasb.fxgl.dsl.FXGL;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class MainButton extends StackPane {

    private final Color SELECT_COLOR = Color.WHITE;
    private final Color UNSELECT_COLOR = Color.LIGHTGRAY;

    private String name;
    private Runnable action;
    private Text text;
    private Rectangle selector;
    private boolean isClickable = true;

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

    public void setClickable(boolean clickable) {
        isClickable = clickable;
        setDisable(!clickable);
        if (clickable) {
            setOpacity(1.0);
        } else {
            setOpacity(0.5);
        }
    }
}
