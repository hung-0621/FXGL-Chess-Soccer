package com.exp.server.service.simulation.factory;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.exp.server.service.simulation.abstruct.BodyFactory;
import com.exp.server.util.UnitConverter;

public class GoalFactory extends BodyFactory {

    public GoalFactory(World world) {
        super(world);
    }

    @Override
    public Body createBody(float x, float y) {

        float width = UnitConverter.pxToMeter(32);
        float height = UnitConverter.pxToMeter(179);

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(x, y);

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
