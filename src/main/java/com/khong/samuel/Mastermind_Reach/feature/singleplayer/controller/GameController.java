package com.khong.samuel.Mastermind_Reach.feature.singleplayer.controller;


import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Game;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/singleplayer")
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }



    // create new game
    @GetMapping ("/start")
    public ResponseEntity<Void> startGame() {
        Game newGame = gameService.startNewGame();

        // Dynamically create new link using gameID
        URI gameUri = URI.create("./game/" + newGame.getId());

        // Return a 302 redirect with the Location header
        return ResponseEntity.status(302).location(gameUri).build();
    }

    // Get the current game by ID
    @GetMapping("/game/{id}")
    public ResponseEntity<Game> getGame(@PathVariable("id") String gameId) {
        Game game = gameService.getGameById(gameId);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(game);
    }

    @PostMapping("/game/{id}")
    public ResponseEntity<Game> postGuess(@PathVariable("id") String gameId, @RequestBody String guess) {
        // Retrieve the game by its ID
        Game game = gameService.getGameById(gameId);

        if (game == null) {
            return ResponseEntity.notFound().build();  // Return 404 if game not found
        }

        // Handle updating processing the game guess
        Game updatedGame = gameService.processGuess(game, guess);  // Process the guess and return updated game

        // Return the updated game object
        return ResponseEntity.ok(updatedGame);
    }








}

