package com.exp.server.service.simulation.factory;

import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import com.exp.server.service.simulation.abstruct.BodyFactory;

public class BackgroundFactory extends BodyFactory {

    public BackgroundFactory(World world) {
        super(world);
    }

    @Override
    public Body createBody(float x, float y) {
       
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(x, y);

        FixtureDef fd = new FixtureDef();
        fd.restitution = 0f;
        fd.friction = 1f;
        fd.density = 0f;

        body = world.createBody(bd);
        body.createFixture(fd);

        return body;
    }
}
