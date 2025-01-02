package feature.singleplayer.service;

import feature.singleplayer.model.Game;
import feature.singleplayer.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game startNewGame() {
        Game newGame = Game.builder()
                .secretCode(generateSecretCode()) // Generate secret code
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

    private String generateSecretCode() {
        // Logic to generate a random secret code for Mastermind
        return "1234"; // Example static code for simplicity
    }


}

