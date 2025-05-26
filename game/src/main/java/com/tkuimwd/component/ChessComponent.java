package com.tkuimwd.component;

import com.tkuimwd.Config;
import com.tkuimwd.event.ChessReleaseEvent;
import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class ChessComponent extends Component {

    private String id;
    private Point2D start;
    private Point2D end;
    private Point2D speed;
    private PhysicsComponent physics;
    private Circle chess;

    private double maxForce; // 可施加最大的力
    private double maxDistance; // 可拖曳的最大距離

    public ChessComponent(String id) {
        this.id = id;
    }

    @Override
    public void onAdded() {
        physics = getEntity().getComponent(PhysicsComponent.class);
        chess = (Circle) getEntity().getViewComponent().getChildren().get(0);
        maxForce = Config.MAX_FORCE;
        maxDistance = Config.MAX_DISTANCE;

        chess.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> onPress());
        chess.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> onRelease());
    }

    @Override
    public void onUpdate(double tpf) {
        speed = physics.getLinearVelocity();
        if (speed.magnitude() > 0 && speed.magnitude() < 30) {
            physics.setLinearVelocity(Point2D.ZERO);
        }
    }

    @Override
    public void onRemoved() {

    }

    public void onPress() {
        chess.setStroke(Color.YELLOW);
        this.start = getEntity().getPosition();
        // System.out.println("Mouse Pressed on Chess: ( " + start.getX() + ", " +
        // start.getY() + " )");
    }

    public void onRelease() {
        chess.setStroke(Color.WHITE);
        this.end = FXGL.getInput().getMousePositionWorld();

        FXGL.getEventBus().fireEvent(new ChessReleaseEvent(
                id,
                start.getX(), start.getY(),
                end.getX(), end.getY()));
                
        caculateImpulse();
        applyImpulse(caculateImpulse());
    }

    public Point2D caculateImpulse() {

        // 計算衝量
        double dist = start.distance(end);
        double dist_percentage = Math.min(dist / maxDistance, 1.0);
        double force = dist_percentage * maxForce;

        // 方向跟衝量
        Point2D direction = start.subtract(end).normalize();
        Point2D impulse = direction.multiply(force);
        return impulse;
    }

    public void applyImpulse(Point2D impulse){
        Vec2 centerVec = physics.getBody().getWorldCenter();
        Point2D center = new Point2D(centerVec.x, centerVec.y);

        physics.applyLinearImpulse(
                impulse,
                center,
                true);
    }

    public String getId() {
        return id;
    }

    public double getRedius(){
        return chess.getRadius();
    }

    public void setLock(){
        // chess.setDisable(true);
        chess.setMouseTransparent(true);
    }

    public void setUnlock(){
        // chess.setDisable(false);
        chess.setMouseTransparent(false);
    }

}
