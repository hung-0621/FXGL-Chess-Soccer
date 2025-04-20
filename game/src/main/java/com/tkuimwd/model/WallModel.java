package com.tkuimwd.model;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class WallModel {

    private ArrayList<Point2D> edges = new ArrayList<>();

    public WallModel(double[][] points) {
        for (double[] point : points) {
            this.edges.add(new Point2D(point[0], point[1]));
        }
    }

    public ArrayList<Point2D> getEdges() {
        return this.edges;
    }

    public void resetEdges(double[][] points) {
        this.edges.clear();
        for (double[] point : points) {
            this.edges.add(new Point2D(point[0], point[1]));
        }
    }
}
