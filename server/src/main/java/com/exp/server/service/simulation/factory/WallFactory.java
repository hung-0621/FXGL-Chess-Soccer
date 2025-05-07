package com.exp.server.service.simulation.factory;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import org.jbox2d.collision.shapes.EdgeShape;

import com.exp.server.service.simulation.abstruct.BodyFactory;

public class WallFactory extends BodyFactory {

    public WallFactory(World world) {
        super(world);
    }

    @Override
    public Body createBody(float x, float y) {
        float x_m = x / ppm; // in meters
        float y_m = y / ppm; // in meters
        final double[][] WALL_EDGES = {
                { 61, 54 }, { 61, 254 }, { 29, 254 }, { 29, 433 },
                { 61, 433 }, { 61, 651 }, { 958, 651 }, { 958, 433 },
                { 991, 433 }, { 991, 254 }, { 958, 254 }, { 958, 54 },
        };

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(0, 70);
        bd.position.set(x_m, y_m);

        FixtureDef fd = new FixtureDef();
        fd.restitution = 1.0f;
        fd.friction = 0.1f;
        fd.density = 1.0f;

        // 設定每個邊緣
        for (int i = 0; i < WALL_EDGES.length; i++) {
            int nextIndex = (i + 1) % WALL_EDGES.length; // 循環到下一個點

            // 獲取當前點和下一個點
            float x1 = (float) (WALL_EDGES[i][0] / ppm);
            float y1 = (float) (WALL_EDGES[i][1] / ppm);
            float x2 = (float) (WALL_EDGES[nextIndex][0] / ppm);
            float y2 = (float) (WALL_EDGES[nextIndex][1] / ppm);

            EdgeShape edge = new EdgeShape();
            edge.set(new Vec2(x1, y1), new Vec2(x2, y2));

            fd.shape = edge;
            body.createFixture(fd);
        }

        body = world.createBody(bd);
        body.createFixture(fd);

        return body;
    }
}
