package com.exp.server.service.simulation;

import java.util.ArrayList;
import java.util.List;

import com.exp.server.service.simulation.dto.EntityState;

// set up EntityStatus
public class GameService {
    private List<EntityState> entityStatesList;

    public GameService() {
        entityStatesList = new ArrayList<>();
    }

    public List<EntityState> createEntityStates() {
        entityStatesList.clear();
        return entityStatesList;
    }

    public List<EntityState> getEntityStatesList() {
        return entityStatesList;
    }
}
