package com.tkuimwd.api.dto;

import com.tkuimwd.type.EntityType;

// /api/game/init
public class Entityinit {
    String id;
    EntityType type;
    double x, y;
    PhysicsAttribute physics;
}
