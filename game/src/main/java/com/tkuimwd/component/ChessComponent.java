package com.tkuimwd.component;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ChessComponent extends Component {

    private Point2D start;
    private Point2D end;
    private PhysicsComponent physics;
    private Circle view;
    private double maxForce;
    private double maxDistance;

    public ChessComponent() {
        this.maxForce = 200; // default
        this.maxDistance = 150; // default
    }

    @Override
    public void onAdded() {
        physics = getEntity().getComponent(PhysicsComponent.class);

        view = (Circle) getEntity().getViewComponent().getChildren().get(0);
        view.setOnMousePressed(e -> onPress(e));
        view.setOnMouseReleased(e -> onRelease(e));
    }

    public void onPress(MouseEvent event) {
        Circle chess = (Circle) event.getSource();
        chess.setStroke(Color.YELLOW);

        this.start = getEntity().getPosition();
        System.out.println("Mouse Pressed on Chess: ( " + start.getX() + ", " + start.getY() + " )");
    }

    public void onRelease(MouseEvent event) {
        Circle chess = (Circle) event.getSource();
        chess.setStroke(Color.WHITE);
        this.end = FXGL.getInput().getMousePositionWorld();

        // 距離跟作用力
        double dist = start.distance(end);
        double dist_percentage = Math.min(dist / maxDistance, 1.0);
        double force = dist_percentage * maxForce;

        // 方向跟衝量
        Point2D direction = start.subtract(end).normalize();
        Point2D impulse = direction.multiply(force);
        System.out.println("Mouse Released on Chess: ( " + end.getX() + ", " + end.getY() + " )");
        System.out.println("Impulse: ( " + impulse.getX() + ", " + impulse.getY() + " )");

        Vec2 centerVec = physics.getBody().getWorldCenter();
        Point2D center = new Point2D(centerVec.x, centerVec.y);

        physics.applyLinearImpulse(
                impulse,
                center,
                true);
    }

}
