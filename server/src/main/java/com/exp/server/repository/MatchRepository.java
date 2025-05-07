package com.exp.server.repository;

import com.exp.server.model.MatchModel;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<MatchModel, String> {
    MatchModel findByPlayer1Id(String player1Id);
    MatchModel findByPlayer2Id(String player2Id);
    List<MatchModel> findAllByPlayer1Id(String player1Id);
    List<MatchModel> findAllByPlayer2Id(String player2Id);
}
