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
    private Point2D speed;
    private PhysicsComponent physics;
    private Circle chess;
    private double maxForce;
    private double maxDistance;

    public ChessComponent() {
        this.maxForce = 200; // default
        this.maxDistance = 150; // default
    }

    @Override
    public void onAdded() {
        physics = getEntity().getComponent(PhysicsComponent.class);
        chess = (Circle) getEntity().getViewComponent().getChildren().get(0);
        chess.setOnMousePressed(e -> onPress(e));
        chess.setOnMouseReleased(e -> onRelease(e));
    }

    @Override
    public void onUpdate(double tpf) {
        speed = physics.getLinearVelocity();
        if(speed.magnitude() > 0 && speed.magnitude() < 30){
            physics.setLinearVelocity(Point2D.ZERO);
        }
    }

    public void onPress(MouseEvent event) {
        Circle chess = (Circle) event.getSource();
        chess.setStroke(Color.YELLOW);

        this.start = getEntity().getPosition();
        // System.out.println("Mouse Pressed on Chess: ( " + start.getX() + ", " + start.getY() + " )");
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
        // System.out.println("Mouse Released on Chess: ( " + end.getX() + ", " + end.getY() + " )");
        // System.out.println("Impulse: ( " + impulse.getX() + ", " + impulse.getY() + " )");

        Vec2 centerVec = physics.getBody().getWorldCenter();
        Point2D center = new Point2D(centerVec.x, centerVec.y);

        physics.applyLinearImpulse(
                impulse,
                center,
                true);
    }

    public void onMove(Point2D point){
        if(speed.getX() < 5 && speed.getY() < 5){

        }
    }

}
