package com.tkuimwd.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.tkuimwd.Config;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;

public class AimComponent extends Component {

    private Circle chess;
    private Group aimView;
    private Line line;
    private Circle forceCircle;
    private boolean isPressing = false;
    private Point2D start;
    private Point2D end;
    private double distance;
    private double maxForce; // 可施加最大的力
    private double maxDistance; // 可拖曳的最大距離

    @Override
    public void onAdded() {
        chess = (Circle) getEntity().getViewComponent().getChildren().get(0);
        aimView = (Group) getEntity().getViewComponent().getChildren().get(1);

        line = (Line) aimView.getChildren().get(0);
        forceCircle = (Circle) aimView.getChildren().get(1);

        start = getEntity().getPosition();
        maxForce = Config.MAX_FORCE;
        maxDistance = Config.MAX_DISTANCE;
        end = start;
        distance = 0;

        aimView.setVisible(false);

        line.setStartX(0);
        line.setStartY(0);

        chess.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> onPress());
        chess.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> onRelease());
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isPressing) {
            return;
        }
        // tmep: 按下滑鼠後，滑鼠的移動位置
        // end: 以start, temp最半徑畫圓，temp的對蹠點
        Point2D temp = FXGL.getInput().getMousePositionWorld();
        Point2D vec = end.subtract(start);
        double distance = getDistance();
        double dx = vec.normalize().getX() * distance;
        double dy = vec.normalize().getY() * distance;
        end = start.multiply(2).subtract(temp);

        // double angle = Math.toDegrees(Math.atan2(end.getY(), end.getX()));
        // System.out.println(
        //         "Mouse Pressed on Chess: ( " + start.getX() + ", " + start.getY() + " )" +
        //                 "\nMouse Released on Chess: ( " + end.getX() + ", " + end.getY() + " )" +
        //                 "\nDistance: " + distance +
        //                 "\nAngle: " + angle +
        //                 "\nVector: (" + dx + ", " + dy + ")\n");
        // 設定線段長度
        forceCircle.setRadius(getDistance());
        line.setEndX(dx);
        line.setEndY(dy);
    }

    private void onPress() {
        start = getEntity().getPosition();
        aimView.setVisible(true);
        
        isPressing = true;
    }

    private void onRelease() {
        aimView.setVisible(false);
        forceCircle.setRadius(0);
        line.setEndX(0);
        line.setEndY(0);
        isPressing = false;
    }

    private double getDistance() {
        return Math.min(end.distance(start), maxDistance);
    }
}
