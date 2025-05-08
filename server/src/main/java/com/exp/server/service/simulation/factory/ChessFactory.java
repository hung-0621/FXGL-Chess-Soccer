package com.exp.server.service.simulation.factory;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.exp.server.service.simulation.abstruct.BodyFactory;
import com.exp.server.util.UnitConverter;

public class ChessFactory extends BodyFactory {

    public ChessFactory(World world) {
        super(world);
    }

    public Body createBody(float x, float y) {

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.fixedRotation = true;
        bd.linearDamping = 0.2f;
        bd.position.set(x, y);

        CircleShape shape = new CircleShape();
        shape.setRadius(UnitConverter.pxToMeter(20)); // 20 pixels to meters

        FixtureDef fd = new FixtureDef();
        fd.restitution = 0.5f;
        fd.friction = 0.3f;
        fd.density = 1f;
        fd.shape = shape;

        body = world.createBody(bd);
        body.createFixture(fd);

        return body;
    }
}
