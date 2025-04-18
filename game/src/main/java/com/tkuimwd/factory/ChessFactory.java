package com.tkuimwd.factory;

import com.tkuimwd.type.Role;
import com.tkuimwd.model.ChessModel;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.IrremovableComponent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

// 暫時兩個棋子
public class ChessFactory implements EntityFactory {

    @Spawns("Chess")
    public Entity spawnPlayer1(SpawnData data) {
        ChessModel chessModel = data.get("chessModel");
        
        Circle chess = new Circle(chessModel.getSize());
        chess.setFill(chessModel.getRole() == Role.PLAYER1 ? Color.BLUE : Color.RED);
        chess.setStroke(Color.WHITE);
        chess.setStrokeWidth(3);

        return FXGL.entityBuilder(data)
                .view(chess)
                .with(new IrremovableComponent())
                .collidable()
                .build();
    }
}
