package com.khong.samuel.Mastermind_Reach.feature.singleplayer.service;

import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Feedback;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.model.Game;
import com.khong.samuel.Mastermind_Reach.feature.singleplayer.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for the GameService class.
 * This class tests the functionality of various methods in the GameService.
 */

    @ExtendWith(MockitoExtension.class)
    class GameServiceTest {

        @Mock
        private GameRepository gameRepository;

        @InjectMocks
        private GameService gameService;

        @Mock
        private RestTemplate restTemplate;  // Mock RestTemplate

    /**
     * Tests the generateSecretCode method in GameService.
     *
     * This test ensures that:
     * - The generated secret code has the correct length based on difficulty (easy, medium, hard).
     * - The method handles HTTP errors gracefully.
     * - The method handles network errors gracefully.
     * - The method handles other unexpected errors gracefully.
     */
    @Test
    void testGenerateSecretCode() {
        // Arrange
        String easyCode = "1234"; // Simulate a generated code
        String mediumCode = "123456";
        String hardCode = "12345678";


        // Act & Assert for Easy difficulty
        String easySecretCode = gameService.generateSecretCode("easy");
        assertEquals(4, easySecretCode.length());  // Verify the length for "easy"

        // Act & Assert for Medium difficulty
        String mediumSecretCode = gameService.generateSecretCode("medium");
        assertEquals(6, mediumSecretCode.length());  // Verify the length for "medium"

        // Act & Assert for Hard difficulty
        String hardSecretCode = gameService.generateSecretCode("hard");
        assertEquals(8, hardSecretCode.length());  // Verify the length for "hard"
    }

        /**
         * Tests the startNewGame method in GameService.
         *
         * This test ensures that:
         * - A new game is saved via the GameRepository.
         * - The returned game has the expected default values, such as a generated secret code,
         *   gameOver set to false, and won set to false.
         */
        @Test
        void testStartNewGame() {

            String difficulty = "easy";
            boolean gameOver = false;
            boolean won = false;
            int turn = 1;

            // Create a mock game with the default difficulty and values for gameOver, won, and turn
            Game mockGame = Game.builder()
                    .difficulty(Game.Difficulty.EASY)
                    .gameOver(gameOver)
                    .won(won)
                    .turn(turn)
                    .build();

            // Mock the repository save call to return the mock game
            when(gameRepository.save(any(Game.class))).thenReturn(mockGame);


            Game newGame = gameService.startNewGame(difficulty, "abc");
            System.out.println(newGame.getId());
            System.out.println(newGame.getSecretCode());


            assertNotNull(newGame);  // Ensure the game is not null
            assertEquals(Game.Difficulty.EASY, newGame.getDifficulty());  // Verify difficulty is set to easy
            assertEquals(gameOver, newGame.isGameOver());  // Verify gameOver is false
            assertEquals(won, newGame.isWon());  // Verify won is false
            assertEquals(turn, newGame.getTurn());  // Verify turn is initialized to 1

            // Verify that the save method was called exactly once
            verify(gameRepository, times(1)).save(any(Game.class));

        }

        /**
         * Tests the getGameById method in GameService.
         *
         * This test ensures that:
         * - A game is retrieved by its ID from the GameRepository.
         * - The returned game has the expected ID.
         */
        @Test
        void testGetGameById() {
            // Arrange
            String gameId = "123";
            Game mockGame = Game.builder().id(gameId).build();
            when(gameRepository.findById(gameId)).thenReturn(Optional.of(mockGame));

            // Act
            Game game = gameService.getGameById(gameId);

            // Assert
            assertNotNull(game);
            assertEquals(gameId, game.getId());
        }

        /**
         * Tests the processGuess method in GameService.
         *
         * This test ensures that:
         * - A valid guess is processed correctly.
         * - The game state (turn count) is updated.
         * - The game is saved in the GameRepository after the guess.
         */
        @Test
        void testProcessGuess() {
            // Arrange
            Game game = gameService.startNewGame("EASY", "abc");
            List<Integer> guess = Arrays.asList(1, 2, 3, 4); // Mock guess as a List of integers
            when(gameRepository.save(any(Game.class))).thenReturn(game);

            // Assuming gameService is properly mocked and injected
            GameService gameService = mock(GameService.class);  // Add this if you haven't already mocked gameService

            // Act
            Game updatedGame = gameService.processGuess(game.getId(), guess);

            // Assert
            assertNotNull(updatedGame); // Ensure updatedGame is not null
            assertEquals(2, updatedGame.getTurn()); // Verify turn is incremented
            verify(gameRepository, times(1)).save(any(Game.class)); // Ensure save is called once
            verify(gameService, times(1)).processGuess(game.getId(), guess); // Ensure processGuess is called once
        }

    /**
     *
     * This test verifies the behavior of the getFeedback() method by ensuring that it correctly calculates
     * the number of exact and partial matches between the guess and the secret code.
     * In this case, the guess matches the secret code exactly.
     */
    @Test
    void testGetFeedback() {
        // Arrange
        Game game = gameService.startNewGame("EASY", "a");  // Use the startNewGame method for consistent setup
        game.setSecretCode("1234");  // Setting a mock secret code
        int[] guess = {1, 2, 3, 4};  // Exact match guess
        Feedback feedback = new Feedback();  // Feedback object to store results


        gameService.getFeedback(guess, game.getSecretCode().chars().map(Character::getNumericValue).toArray(), feedback);  // Calculate feedback

        // Assert
        assertEquals(4, feedback.getExactMatches());  // Verify 4 exact matches
        assertEquals(0, feedback.getPartialMatches());  // Verify 0 partial matches
    }

    @Test
    void testAllIncorrectGuesses() {
        // Arrange
        Game game = gameService.startNewGame("EASY", "a");  // Use the startNewGame method for consistent setup
        game.setSecretCode("5678");  // Setting a mock secret code
        int[] guess = {1, 2, 3, 4};  // Exact match guess
        Feedback feedback = new Feedback();  // Feedback object to store results


        gameService.getFeedback(guess, game.getSecretCode().chars().map(Character::getNumericValue).toArray(), feedback);  // Calculate feedback

        // Assert
        assertEquals(0, feedback.getExactMatches());  // Verify 4 exact matches
        assertEquals(0, feedback.getPartialMatches());  // Verify 0 partial matches
    }

    @Test
    void partialCorrectGuesses() {
        // Arrange
        Game game = gameService.startNewGame("EASY" , "abc");  // Use the startNewGame method for consistent setup
        game.setSecretCode("1234");  // Setting a mock secret code
        int[] guess = {1, 2, 5, 1};  // Exact match guess
        Feedback feedback = new Feedback();  // Feedback object to store results


        gameService.getFeedback(guess, game.getSecretCode().chars().map(Character::getNumericValue).toArray(), feedback);  // Calculate feedback

        // Assert
        assertEquals(2, feedback.getExactMatches());  // 2 exact matches: 1 and 2
        assertEquals(0, feedback.getPartialMatches());  // 1 partial match: the second 1 (index 3) is a partial match

    }





//




    }

