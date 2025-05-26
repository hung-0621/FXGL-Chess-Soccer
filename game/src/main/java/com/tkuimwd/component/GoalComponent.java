package com.tkuimwd.component;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.BoundingBoxComponent;
import com.tkuimwd.event.GoalEvent;
import com.tkuimwd.type.EntityType;

public class GoalComponent extends Component {

    private Entity football;
    private boolean score;

    @Override
    public void onAdded() {
        score = false;
        football = FXGL.getGameWorld().getEntitiesByType(EntityType.FOOTBALL).get(0);
    }

    @Override
    public void onUpdate(double tpf) {
        if (football == null || score) {
            return;
        }
        
        if (isGoal(football)){
            FXGL.getEventBus().fireEvent(new GoalEvent());
        }
    }

    @Override
    public void onRemoved() {
        
    }

    // 檢查footBall是否進入Goal
    private boolean isGoal(Entity football) {
        BoundingBoxComponent footBallBox = football.getBoundingBoxComponent();
        BoundingBoxComponent goalBox = getEntity().getBoundingBoxComponent();
        return footBallBox.getMinXWorld() >= goalBox.getMinXWorld()
                && footBallBox.getMaxXWorld() <= goalBox.getMaxXWorld()
                && footBallBox.getMinYWorld() >= goalBox.getMinYWorld()
                && footBallBox.getMaxYWorld() <= goalBox.getMaxYWorld();
    }
}
