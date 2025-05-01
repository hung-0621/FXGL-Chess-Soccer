package com.exp.server.repository;

import com.exp.server.model.PlayerModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PlayerRepository extends MongoRepository<PlayerModel, String> {
    PlayerModel findByUserName(String userName);
    PlayerModel findByEmail(String email);
    PlayerModel findByToken(String token);
    Optional<PlayerModel> findById(String id);
}
