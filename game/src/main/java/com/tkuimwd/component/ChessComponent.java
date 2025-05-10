package com.tkuimwd.component;

import com.tkuimwd.Config;

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
    
    private double maxForce; // 可施加最大的力
    private double maxDistance; // 可拖曳的最大距離

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
        if(speed.magnitude() > 0 && speed.magnitude() < 30){
            physics.setLinearVelocity(Point2D.ZERO);
        }
    }

    public void onPress() {
        chess.setStroke(Color.YELLOW);
        this.start = getEntity().getPosition();

        // System.out.println("Mouse Pressed on Chess: ( " + start.getX() + ", " + start.getY() + " )");
    }

    public void onRelease() {
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

    public Point2D getSpeed(){
        return speed;
    }

}
