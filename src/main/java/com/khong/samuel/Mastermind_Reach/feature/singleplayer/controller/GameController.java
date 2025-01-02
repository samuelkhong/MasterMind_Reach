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

    @GetMapping("/")
    public String getText() {
        return "Hello, World!";
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
        System.out.println(gameId); // Log the gameId for debugging
        Game game = gameService.getGameById(gameId);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(game);
    }






}

