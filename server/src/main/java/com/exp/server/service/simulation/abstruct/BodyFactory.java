package com.exp.server.service.simulation.abstruct;

import javax.swing.text.html.parser.Entity;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import com.exp.server.EntityConfig;
import com.exp.server.service.simulation.dto.EntityState;

@SuppressWarnings("unused")
public abstract class BodyFactory {
    protected World world;
    protected Body body;
    protected float ppm; // pixels per meter

    public BodyFactory(World world) {
        this.world = world;
        this.ppm = EntityConfig.PPM;
    }

    public abstract Body createBody(float x, float y);
}