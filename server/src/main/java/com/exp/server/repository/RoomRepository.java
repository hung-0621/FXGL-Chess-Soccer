package com.exp.server.repository;

import com.exp.server.model.RoomModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<RoomModel, String> {
    RoomModel findByRoomCode(String roomCode);
}
