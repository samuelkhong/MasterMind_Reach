package com.khong.samuel.Mastermind_Reach.feature.singleplayer.controller;


import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Game;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@Controller
@RequestMapping("/singleplayer")
public class GameController {

    private final GameService gameService;


    public GameController(GameService gameService) {
        this.gameService = gameService;
    }



    // create new game
    @GetMapping ("/start")
    public String startGame() {
        return "singleplayer/gameSelect";
    }

    @PostMapping("/start")
    public String startNewGame(@RequestParam String difficulty, Model model) {
        // Create a new game object based on the selected difficulty
        Game newGame = gameService.startNewGame(difficulty);

        // Add the new game to the model so it can be accessed in the Thymeleaf template
//        model.addAttribute("newGame", newGame);

        // Return the name of the Thymeleaf template
        return "hello world";
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

