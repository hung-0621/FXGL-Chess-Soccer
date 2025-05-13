package com.exp.server.service.simulation.factory;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import org.jbox2d.collision.shapes.EdgeShape;

import com.exp.server.EntityConfig;
import com.exp.server.service.simulation.abstruct.BodyFactory;
import com.exp.server.util.UnitConverter;

public class WallFactory extends BodyFactory {

    public WallFactory(World world) {
        super(world);
    }

    @Override
    public Body createBody(float x, float y) {
        final float[][] WALL_EDGES = EntityConfig.WALL_EDGES;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(0, 0); // 用全域座標，不要 offset 70
        body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();
        fd.restitution = 1.0f;
        fd.friction = 0.1f;
        fd.density = 1.0f;

        for (int i = 0; i < WALL_EDGES.length; i++) {
            int nextIndex = (i + 1) % WALL_EDGES.length;

            float x1 = UnitConverter.pxToMeter(WALL_EDGES[i][0]);
            float y1 = UnitConverter.pxToMeter(WALL_EDGES[i][1]);
            float x2 = UnitConverter.pxToMeter(WALL_EDGES[nextIndex][0]);
            float y2 = UnitConverter.pxToMeter(WALL_EDGES[nextIndex][1]);

            EdgeShape edge = new EdgeShape();
            edge.set(new Vec2(x1, y1), new Vec2(x2, y2));
            fd.shape = edge;
            body.createFixture(fd);
        }

        return body;
    }
}
