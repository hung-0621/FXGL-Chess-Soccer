package com.exp.server.service.simulation;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.springframework.stereotype.Component;

@Component
public class PhysicsWorldWapper {
    private World world;

    public PhysicsWorldWapper() {
        world = new World(new Vec2(0, 0));
        world.setContactListener(new ContactListenerImply());
    }

    public World getWorld() {
        return world;
    }
}
