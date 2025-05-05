package com.exp.server.util;

public class Point2D {
    private final double x;
    private final double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Point2D normalize() {
        double mag = magnitude();
        return mag == 0 ? new Point2D(0, 0) : new Point2D(x / mag, y / mag);
    }

    public Point2D multiply(double scalar) {
        return new Point2D(x * scalar, y * scalar);
    }

    public Point2D add(Point2D other) {
        return new Point2D(this.x + other.x, this.y + other.y);
    }

    public Point2D subtract(Point2D other) {
        return new Point2D(this.x - other.x, this.y - other.y);
    }
}