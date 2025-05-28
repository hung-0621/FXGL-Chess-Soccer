package com.tkuimwd.component;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.collections.ObservableList;
import javafx.scene.shape.Polygon;
import com.tkuimwd.Config;

public class FootBallComponent extends Component {

    private String id;
    private Point2D speed;
    private PhysicsComponent physics;

    public FootBallComponent(String id) {
        this.id = id;
    }

    @Override
    public void onAdded() {
        physics = getEntity().getComponent(PhysicsComponent.class);
    }

    @Override
    public void onUpdate(double tpf) {

        speed = physics.getLinearVelocity();

        if (speed.magnitude() > 0 && speed.magnitude() < 80) {
            physics.setLinearVelocity(Point2D.ZERO);
        }

        // // 避免貼牆
        // Point2D ballCenter = getEntity().getCenter();
        // double ballRadius = getEntity().getBoundingBoxComponent().getWidth() / 2;

        // Point2D wallOrigin = Config.WALL_POSITION;
        // double[][] WALL_EDGES = Config.WALL_EDGES;

        // Polygon polygon = new Polygon();
        // for (double[] p : WALL_EDGES) {
        // polygon.getPoints().addAll(p[0] + wallOrigin.getX(), p[1] +
        // wallOrigin.getY());
        // }

        // // Check point-to-edge minimum distance
        // double minEdgeDist = Double.MAX_VALUE;
        // ObservableList<Double> pts = polygon.getPoints();
        // for (int i = 0; i < pts.size(); i += 2) {
        // double x1 = pts.get(i);
        // double y1 = pts.get(i + 1);
        // double x2 = pts.get((i + 2) % pts.size());
        // double y2 = pts.get((i + 3) % pts.size());

        // Point2D p1 = new Point2D(x1, y1);
        // Point2D p2 = new Point2D(x2, y2);

        // // Project ball center onto the edge segment
        // Point2D edge = p2.subtract(p1);
        // Point2D toCenter = ballCenter.subtract(p1);
        // double t = Math.max(0, Math.min(1, toCenter.dotProduct(edge) /
        // edge.dotProduct(edge)));
        // Point2D closest = p1.add(edge.multiply(t));
        // double dist = closest.distance(ballCenter);

        // if (dist < minEdgeDist) {
        // minEdgeDist = dist;
        // }
        // }

        // // debug
        // // System.out.println("Ball center: " + ballCenter);
        // // System.out.println("minEdgeDist = " + minEdgeDist + ", ballRadius = " +
        // // ballRadius);

        // if (minEdgeDist <= ballRadius && speed.magnitude() < 1) {
        // System.out.println(" 球貼在牆邊界上，推開！");
        // Point2D nudge = new Point2D(Math.random() - 0.5, Math.random() - 0.5)
        // .normalize()
        // .multiply(30);
        // physics.setLinearVelocity(nudge);
        // }
    }

    public String getId() {
        return id;
    }

    public double getRedius() {
        return getEntity().getBoundingBoxComponent().getWidth() / 2;
    }

}
