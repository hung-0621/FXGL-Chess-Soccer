package com.exp.server.service;

public class PhysicalProperties {

    private double mass;          // 質量
    private double restitution;   // 彈性係數 (0~1)
    private double friction;      // 摩擦係數 (0~1)

    public PhysicalProperties(double mass, double restitution, double friction) {
        this.mass = mass;
        this.restitution = restitution;
        this.friction = friction;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getRestitution() {
        return restitution;
    }

    public void setRestitution(double restitution) {
        this.restitution = restitution;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    @Override
    public String toString() {
        return "PhysicalProperties{" +
                "mass=" + mass +
                ", restitution=" + restitution +
                ", friction=" + friction +
                '}';
    }
} 
