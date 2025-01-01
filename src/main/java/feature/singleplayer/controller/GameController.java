package feature.singleplayer.controller;


import feature.singleplayer.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

public class GameController {

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


    }
}
