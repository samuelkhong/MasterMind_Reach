package feature.singleplayer.controller;


import feature.singleplayer.model.Game;
import feature.singleplayer.service.GameService;
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
    @PostMapping("/start")
    public ResponseEntity<Void> startGame() {
        Game newGame = gameService.startNewGame();

        // Dynamically create new link using gameID
        URI gameUri = URI.create("../games/" + newGame.getId());

        // Return a 302 redirect with the Location header
        return ResponseEntity.status(302).location(gameUri).build();
    }

    // Get the current game by ID
    @GetMapping("/game")
    public ResponseEntity<Game> getGame(@RequestParam("id") String gameId) {
        Game game = gameService.getGameById(gameId);

        if (game == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(game);
    }



}

