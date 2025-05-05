package com.exp.server.service;

import com.exp.server.util.Point2D;
import java.util.HashMap;
import java.util.Map;

public class PhysicsEngine {

    public static class SimulationResult {
        public String chessId;
        public double newX;
        public double newY;
        public double ballX;
        public double ballY;

        public SimulationResult(String chessId, double newX, double newY, double ballX, double ballY) {
            this.chessId = chessId;
            this.newX = newX;
            this.newY = newY;
            this.ballX = ballX;
            this.ballY = ballY;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            result.put("chessId", chessId);
            result.put("newX", newX);
            result.put("newY", newY);
            result.put("ballX", ballX);
            result.put("ballY", ballY);
            return result;
        }
    }

    public static class EntityPhysicsProperties {
        public double mass;
        public double friction;
        public double elasticity;

        public EntityPhysicsProperties(double mass, double friction, double elasticity) {
            this.mass = mass;
            this.friction = friction;
            this.elasticity = elasticity;
        }
    }

    private static final EntityPhysicsProperties CHESS_PROPERTIES = new EntityPhysicsProperties(1.0, 0.2, 0.6);
    private static final EntityPhysicsProperties BALL_PROPERTIES = new EntityPhysicsProperties(0.5, 0.05, 0.8);

    public SimulationResult simulate(String chessId, double dx, double dy, double power) {
        // normalize direction
        Point2D dir = new Point2D(dx, dy).normalize();

        // apply force based on power and mass
        Point2D velocity = dir.multiply(power / CHESS_PROPERTIES.mass * (1.0 - CHESS_PROPERTIES.friction));

        // simulate new position
        double newX = velocity.getX() * 10; // scale factor
        double newY = velocity.getY() * 10;

        // apply collision to ball
        Point2D ballVelocity = applyElasticCollision(velocity, CHESS_PROPERTIES, BALL_PROPERTIES);

        double ballX = ballVelocity.getX() * 10;
        double ballY = ballVelocity.getY() * 10;

        return new SimulationResult(chessId, newX, newY, ballX, ballY);
    }

    private Point2D applyElasticCollision(Point2D velocity, EntityPhysicsProperties source, EntityPhysicsProperties target) {
        double massRatio = 2 * source.mass / (source.mass + target.mass);
        double elasticity = (source.elasticity + target.elasticity) / 2.0;
        return velocity.multiply(massRatio * elasticity * (1.0 - target.friction));
    }
} 
