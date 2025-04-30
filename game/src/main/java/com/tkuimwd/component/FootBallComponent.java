package com.tkuimwd.component;


import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;


import javafx.geometry.Point2D;

public class FootBallComponent extends Component{
    
    private Point2D speed;
    private PhysicsComponent physics;
    private boolean touchingWall = false;
    
    @Override
    public void onAdded() {
        physics = getEntity().getComponent(PhysicsComponent.class);
    }

    public void setTouchingWall(boolean touching) {
        this.touchingWall = touching;
    }

    private double lastNudgeTime = 0;

    @Override
    public void onUpdate(double tpf) {
        speed = physics.getLinearVelocity();

        if (speed.magnitude() > 0 && speed.magnitude() < 30) {
            physics.setLinearVelocity(Point2D.ZERO);
        }

        //避免抖動
        lastNudgeTime += tpf;

        // 若接觸牆壁且速度小於1，就推推推
        if (touchingWall && speed.magnitude() < 1 && lastNudgeTime > 1.0) {
            Point2D nudge = new Point2D(Math.random() - 0.5, Math.random() - 0.5).normalize().multiply(20);
            physics.setLinearVelocity(nudge);
            lastNudgeTime = 0;
        }
    }
}
