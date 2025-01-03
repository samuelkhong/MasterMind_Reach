package com.khong.samuel.Mastermind_Reach.feature.singleplayer.controller;


import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Game;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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
        try {
            // Create a new game based on the selected difficulty
            Game newGame = gameService.startNewGame(difficulty);

            // Add the new game to the model (if you need to use it for something else)
            model.addAttribute("game", newGame);

            // Redirect to the game page using the game ID
            return "redirect:/singleplayer/game/" + newGame.getId();  // Redirects to the /game/{gameId} URL
        } catch (Exception e) {
            e.printStackTrace();
            return "error";  // Return an error page if something goes wrong
        }
    }

    @GetMapping("/game/{gameId}")
    public String loadGame(@PathVariable String gameId, Model model) {
        // For debugging: check the gameId being passed
//        System.out.println("Game ID from URL: " + gameId);

        // Retrieve the game object by its ID
        Game game = gameService.getGameById(gameId);

        if (game == null) {
            return "error";  // Return error if no game is found
        }

        // Add the game object to the model for the Thymeleaf template
        model.addAttribute("game", game);

        // Return the game template
        return "singleplayer/game";  // This is the Thymeleaf template to display the game
    }

    @PostMapping("/guess")
    public String handleGuess(@RequestParam("gameId") String gameId,
                              @RequestParam Map<String, String> allParams,
                              Model model) {
        // test if we got gameID
        System.out.println(gameId);
        // print out guesses
        System.out.println(allParams);
        // Get the game object based on gameId
        Game game = gameService.getGameById(gameId);

        // Initialize the maxGuesses variable based on game difficulty
        int maxGuesses = 0;

        // Switch statement to set the number of guesses based on difficulty
        switch (game.getDifficulty()) {
            case EASY:
                maxGuesses = 4;  // 4 guesses for EASY
                break;
            case MEDIUM:
                maxGuesses = 6;  // 6 guesses for MEDIUM
                break;
            case HARD:
                maxGuesses = 8;  // 8 guesses for HARD
                break;
            default:
                maxGuesses = 4;  // Default to 4 guesses if difficulty is unknown
                break;
        }

        // List to store the guesses in order
        List<Integer> guesses = new ArrayList<>();

        // Loop through each guess (from 1 to maxGuesses) and retrieve the corresponding value
        for (int i = 1; i <= maxGuesses; i++) {
            String guessStr = allParams.get("guess" + i); // Get the value for guess1.

            if (guessStr != null && !guessStr.isEmpty()) {
                int guess = Integer.parseInt(guessStr);
                guesses.add(guess);  // The list will maintain the order of guesses
            }
        }

        System.out.println(guesses);

        // Send guesses to be processed
        gameService.processGuess(gameId, guesses);

        // Add the game data to the model for rendering the view
        model.addAttribute("game", game);

        return "redirect:/singleplayer/game/" + gameId;  // Redirects to the /game/{gameId} URL
    }




}

