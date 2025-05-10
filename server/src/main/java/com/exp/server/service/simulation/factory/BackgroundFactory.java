package com.exp.server.service.simulation.factory;

import org.jbox2d.collision.shapes.PolygonShape;
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

        // 設定形狀為矩形背景（你可以根據實際大小調整）
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(512f, 384f); // 這裡是假設背景大小為 1024x768，可依需要調整

        FixtureDef fd = new FixtureDef();
        fd.shape = shape; // 一定要設這個！
        fd.restitution = 0f;
        fd.friction = 1f;
        fd.density = 0f;

        body = world.createBody(bd);
        body.createFixture(fd);

        return body;
    }
}
