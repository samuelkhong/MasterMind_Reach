package com.khong.samuel.Mastermind_Reach.feature.singleplayer.repository;

import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {


}
