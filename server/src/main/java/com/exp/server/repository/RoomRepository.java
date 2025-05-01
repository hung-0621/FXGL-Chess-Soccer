package com.exp.server.repository;

import com.exp.server.model.RoomModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RoomRepository extends MongoRepository<RoomModel, String> {

}
