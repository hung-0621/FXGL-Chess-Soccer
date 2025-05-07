package com.exp.server.service.simulation;

import org.jbox2d.dynamics.World;

public class PhysicsWorldWapper {
    private World world;
    
    public PhysicsWorldWapper(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
