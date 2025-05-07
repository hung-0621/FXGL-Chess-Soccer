package com.exp.server.service.simulation.factory;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.exp.server.service.simulation.abstruct.BodyFactory;

public class GoalFactory extends BodyFactory {

    public GoalFactory(World world, float ppm) {
        super(world, ppm);
    }

    @Override
    public Body createBody(float x, float y) {
        float x_m = x / ppm; // in meters
        float y_m = y / ppm; // in meters
        float width = 32 / ppm;
        float height = 179 / ppm;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(x_m, y_m);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.isSensor = true;
        fd.shape = shape;

        body = world.createBody(bd);
        body.createFixture(fd);

        return body;

    }
}
