package feature.singleplayer.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private boolean gameOver; // Indicates if the game is finished
    private boolean won; // Indicates if the player has won

    @Builder.Default
    private List<String> guesses = new ArrayList<>(); // List of guesses as strings

    @Builder.Default
    private List<String> feedbacks = new ArrayList<>(); // Feedback strings for guesses



    public void addGuess(String guess) {
        this.guesses.add(guess);
    }

    public void addFeedback(String feedback) {
        this.feedbacks.add(feedback);
    }
}