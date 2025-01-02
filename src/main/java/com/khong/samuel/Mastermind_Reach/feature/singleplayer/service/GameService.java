package com.khong.samuel.Mastermind_Reach.feature.singleplayer.service;

import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Feedback;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Game;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

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

    // update player input into the board
    public Game  processGuess(Game game, String guess ) {
       // check early exit if game already set to complete
        if (game.isWon()) {
           return game;
       }

        // check if guess is valid number
        if (!isValidNumber(guess)) {
            return game;

        }
        // check if exceed max turncount
        if (game.getTurn() >= 10) {
            game.setGameOver(true);
            return game;
        }

        // add guess onto board
        updateBoard(guess, game);

        // add guess to game state
        game.addGuess(guess);

        // add feedback





        // Increment turn count by 1
        game.setTurn(game.getTurn() + 1);


        // Save the game state
        gameRepository.save(game);

        return game;
    }


    private void addFeedback(String guess, Game game) {
        char[] guessArr = guess.toCharArray();
        char[] secretCodeArr = game.getSecretCode().toCharArray(); // Assume Game has getSecretCode() method
        Feedback feedback = new Feedback();

        // Calculate feedback based on guess and secret code
        getFeedback(guessArr, secretCodeArr, feedback);

        // Store the feedback into the game
        //convert feedback object into stirng


        game.addFeedback(feedback.toString()); // Assume Game has addFeedback() method
    }

    private void getFeedback(char[] guessArr, char[] secretCodeArr, Feedback feedback) {
        int correctNumLoc = 0;
        int correctNumOnly = 0;

        // Keep track of matching indexes
        Set<Integer> guessIndexMatch = new HashSet<>();
        Set<Integer> secretIndexMatch = new HashSet<>();

        // Find correct numbers and locations, and update matching indexes
        for (int i = 0; i < guessArr.length; i++) {
            if (guessArr[i] == secretCodeArr[i]) {
                correctNumLoc++;
                guessIndexMatch.add(i);
                secretIndexMatch.add(i);
            }
        }

        // Find correct numbers but in the wrong positions
        for (int i = 0; i < guessArr.length; i++) {
            // Skip indexes where the number and location already match
            if (guessIndexMatch.contains(i)) {
                continue;
            }

            // Find the first occurrence of a matching number not at the correct position
            for (int j = 0; j < secretCodeArr.length; j++) {
                if (guessArr[i] == secretCodeArr[j] && !secretIndexMatch.contains(j)) {
                    correctNumOnly++;
                    secretIndexMatch.add(j);
                    break; // Move to the next guess character
                }
            }
        }

        // Update feedback object
        feedback.setExactMatches(correctNumLoc);
        feedback.setPartialMatches(correctNumOnly);
    }



    private boolean isWon(String guess, Game game) {
        String secret = game.getSecretCode();

        // if guess matches the secret, game won
        if (guess.equals(secret)) {
            // set game to completed
            game.endGame();
            gameRepository.save(game);

            return true;
        }

        return false;
    }

    private void updateBoard(String guess, Game game) {
        String[] board = game.getBoard();
        int turn = game.getTurn();

        board[10 - turn] = guess;
        gameRepository.save(game);

    }

    private boolean isValidNumber(String input) {
        // iterate through every char in input and and convert into int
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            // return false if nonnumeric
            if (!Character.isDigit(ch)) {
                return false;
            }

            // check if digit is >=0 and < 8
            int digit = Character.getNumericValue(ch);
            if (digit < 0 || digit >= 7) {
                return false;
            }
        }
        return true;

    }


//    private int[][] intializeBoard(String difficulty) {
//
//    }
//
//



    private String generateSecretCode(int codeLength) {
        String baseUrl = "https://www.random.org/integers/";
        String apiUrl = String.format(
                "%s?num=%d&min=0&max=7&col=1&base=10&format=plain&rnd=new", // set to format string so that can vary num length
                baseUrl, codeLength
        );

        RestTemplate restTemplate = new RestTemplate();
        String response = "";

        // Calls the Random API
        try {
            ResponseEntity<String> apiResponse = restTemplate.getForEntity(apiUrl, String.class);
            response = apiResponse.getBody(); // Get response body

            // Clean string of newlines
            response = response.replace("\n", "");

        } catch (HttpClientErrorException e) {
            // Handle HTTP errors
            System.err.println("HTTP error occurred: " + e.getStatusCode());
            response = "Error: " + e.getStatusCode();
        } catch (ResourceAccessException e) {
            // Handle network or resource access issues
            System.err.println("Network error occurred: " + e.getMessage());
            response = "Network Error: Unable to access RANDOM.ORG";
        } catch (Exception e) {
            // If another error occurs upon API call
            System.err.println("Unexpected error occurred: " + e.getMessage());
            response = "Unexpected Error";
        }

        return response;
    }


}

