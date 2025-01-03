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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game startNewGame(String difficulty) {
        // Set the difficulty
        Game.Difficulty gameDifficulty = Game.Difficulty.valueOf(difficulty.toUpperCase());

        // Initialize the board based on difficulty
        String[][] board = intializeBoard(difficulty);

        // Create a new game instance with the provided difficulty and board
        Game newGame = Game.builder()
                .secretCode(generateSecretCode(difficulty)) // Generate secret code based on difficulty
                .difficulty(gameDifficulty) // Set the difficulty
                .board(board) // Initialize the board
                .gameOver(false) // Game is not over initially
                .won(false) // Player has not won yet
                .turn(1) // Start from turn 1
                .build();

        gameRepository.save(newGame);


        // Save the game to the database
        return newGame;
    }

    // get game from ID
    public Game getGameById(String gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game with ID " + gameId + " not found"));
    }

    // update player input into the board
    public Game processGuess(Game game, String guess) {
        try {
            // Early exit if game already won or game over
            if (game.isGameOver()) {
                return game;
            }

            // Check if the max turn count is exceeded
            if (game.getTurn() >= 10) {
                game.setWon(false);  // Set false for player loss
                game.setGameOver(true);
                gameRepository.save(game);
                return game;
            }

            // Convert the guess into a list of integers
            List<Integer> guessValues = guess.chars()
                    .map(c -> Character.getNumericValue(c))
                    .boxed()
                    .collect(Collectors.toList());

            // Validate the guess if it is integers
            if (!isValidNumber(guess)) {
                // log an error.
                return game;  // Or return some error response in a different context
            }

            // Add feedback to the game state
            addFeedback(guess, game);

            // Update the board with the guess
            updateBoard( guess, game);

            // Check if the guess resulted in a win
            if (checkWin(guess, game)) {
                game.setWon(true);  // Set won to true
                game.setGameOver(true);  // Set game over
            } else {
                // Increment the turn count
                game.setTurn(game.getTurn() + 1);
            }

            // Save the updated game state
            gameRepository.save(game);

            return game;
        } catch (Exception e) {
            // Handle exceptions (e.g., logging or rethrowing as a runtime exception)
            e.printStackTrace();
            return game; // Or return an error response if needed
        }
    }



    private void addFeedback(String guess, Game game) {
        // Convert the guess string into an array of characters
        char[] guessArr = guess.toCharArray();
        // Convert the secret code from the game into an array of characters
        char[] secretCodeArr = game.getSecretCode().toCharArray();

        // Create a Feedback object to store the result
        Feedback feedback = new Feedback();

        // Calculate feedback based on the guess and the secret code
        getFeedback(guessArr, secretCodeArr, feedback);

        // Convert the feedback object into a string and store it in the game
        game.addFeedback(feedbackToString(feedback));
    }

    private void getFeedback(char[] guessArr, char[] secretCodeArr, Feedback feedback) {
        int correctNumLoc = 0;
        int correctNumOnly = 0;

        // Keep track of matching indexes
        Set<Integer> guessIndexMatch = new HashSet<>();
        Set<Integer> secretIndexMatch = new HashSet<>();

        // First, find exact matches (correct number and correct location)
        for (int i = 0; i < guessArr.length; i++) {
            if (guessArr[i] == secretCodeArr[i]) {
                correctNumLoc++;
                guessIndexMatch.add(i);
                secretIndexMatch.add(i);
            }
        }

        // Next, find partial matches (correct number but wrong location)
        for (int i = 0; i < guessArr.length; i++) {
            if (guessIndexMatch.contains(i)) {
                continue;  // Skip if this index is already an exact match
            }

            // Look for the first occurrence of the number in the secret code that's not already matched
            for (int j = 0; j < secretCodeArr.length; j++) {
                if (guessArr[i] == secretCodeArr[j] && !secretIndexMatch.contains(j)) {
                    correctNumOnly++;
                    secretIndexMatch.add(j);
                    break;  // Move on to the next guess character
                }
            }
        }

        // Set the feedback counts for exact and partial matches
        feedback.setExactMatches(correctNumLoc);
        feedback.setPartialMatches(correctNumOnly);
    }

    private String feedbackToString(Feedback feedback) {
        // Convert the feedback object into a string format
        return "Exact Matches: " + feedback.getExactMatches() + ", Partial Matches: " + feedback.getPartialMatches();
    }



    private boolean checkWin(String guess, Game game) {
        String secret = game.getSecretCode();

        // If guess matches the secret, game is won
        if (guess.equals(secret)) {
            // Set game state to 'won'
            game.setWon(true);
            game.setGameOver(true); // Mark game as complete
            gameRepository.save(game);
            return true;
        }

        return false;
    }

    private void updateBoard(String guess, Game game) {
        String[][] board = game.getBoard();
        int turn = game.getTurn();

        // Convert guess into a string array for each character (or split it as needed)
        String[] guessArr = guess.split("");  // Split the guess into individual characters

        // Add guess to the board at the appropriate turn position
        if (turn < 10) {  // Make sure the turn is within the maximum number of turns. Prevent adding to null index
            board[10 - turn ] = guessArr;  //
        }

        // Update the game board in the game object
        game.setBoard(board);

        // Save the updated game state
        gameRepository.save(game);
    }


    private boolean isValidNumber(String input) {
        // Check if the input is a valid number based on the allowed range and length
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            // Return false if a non-digit character is found
            if (!Character.isDigit(ch)) {
                return false;
            }

            // Ensure the digit is within the valid range (0-6 based on earlier context)
            int digit = Character.getNumericValue(ch);
            if (digit < 0 || digit >= 7) { // Assuming valid digits are 0-6 for the guess
                return false;
            }
        }

        return true;
    }

    private String[][] intializeBoard(String gameDifficulty) {


        int colSize = 4;

        switch (gameDifficulty.toLowerCase()) {
            case "easy":
                colSize = 4;
                break;
            case "medium":
                colSize = 6;
                break;
            case "hard":
                colSize = 8;
                break;
            default:
                colSize = 4; // Default to easy
                break;
        }
        // Intialize  board size based on difficulty of game
        String[][] board = new String[10][colSize];

        // Iterate through each col on the board
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < colSize; j++) {
                board[i][j] = "#";
            }
        }
        return board;

    }





    public String generateSecretCode(String difficulty) {
        int codeLength;
        // Set code length based on difficulty
        switch (difficulty.toLowerCase()) {
            case "easy":
                codeLength = 4;
                break;
            case "medium":
                codeLength = 6;
                break;
            case "hard":
                codeLength = 8;
                break;
            default:
                codeLength = 4; // Default to easy
                break;
        }
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

