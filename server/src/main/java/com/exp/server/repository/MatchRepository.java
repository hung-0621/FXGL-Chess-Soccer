package com.exp.server.repository;

import com.exp.server.model.MatchModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface MatchRepository extends MongoRepository<MatchModel, String> {

}
