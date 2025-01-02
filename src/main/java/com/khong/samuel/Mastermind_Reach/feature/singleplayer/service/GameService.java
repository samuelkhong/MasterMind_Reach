package com.khong.samuel.Mastermind_Reach.feature.singleplayer.service;

import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Game;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game startNewGame() {
        Game newGame = Game.builder()
                .secretCode(generateSecretCode(4)) // Generate secret code
                .gameOver(false)
                .won(false)
                .build();


        return gameRepository.save(newGame); // Save the game to the database
    }

    // get game from ID
    public Game getGameById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));
    }

    private String generateSecretCode(int codeLength) {
        return "1234";
    }


}

