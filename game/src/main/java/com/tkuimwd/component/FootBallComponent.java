package com.tkuimwd.component;


import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

import javafx.geometry.Point2D;

public class FootBallComponent extends Component{
    
    private Point2D speed;
    private PhysicsComponent physics;
    
    @Override
    public void onAdded() {
        physics = getEntity().getComponent(PhysicsComponent.class);
    }

    @Override
    public void onUpdate(double deltaTime) {
        speed = physics.getLinearVelocity();
        if(speed.magnitude() > 0 && speed.magnitude() < 30){
            physics.setLinearVelocity(Point2D.ZERO);
        }
    }
}
