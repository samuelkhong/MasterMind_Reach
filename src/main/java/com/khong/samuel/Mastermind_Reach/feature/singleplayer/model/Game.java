package com.khong.samuel.Mastermind_Reach.feature.singleplayer.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "games")
public class Game {

    @Id
    private String id; // Unique identifier for the game
    private String secretCode; // The secret code to be guessed


    private String[][] board;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    @Builder.Default
    private boolean gameOver = false; // Indicates if the game is finished

    @Builder.Default
    private Difficulty difficulty = Difficulty.EASY; // Default difficulty is easy

    @Builder.Default
    private boolean won = false; // Indicates if the player has won

    // sets default to turn 1
    @Builder.Default
    private int turn = 1; // represents the current turn

    @Builder.Default
    private List<String> guesses = new ArrayList<>(); // List of guesses as strings

    @Builder.Default
    private String[] feedbacks = new String[10]; // Feedback strings for guesses

    // Constructor fills feedback so not empty



    public void addGuess(String guess) {
        this.guesses.add(guess);
    }
    public void addFeedback(String feedback, int turn) {
        this.feedbacks[turn - 1] = feedback;
    }


    public void endGame() {
        setWon(true);
    }

}
