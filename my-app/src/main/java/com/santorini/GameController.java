package com.santorini;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000"}) 
public class GameController {

    @Autowired
    private GameService gameService;

    @RequestMapping(value = "/newgame", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> newGame() {
        String response = gameService.handleNewGame();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/play", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> playAction(@RequestParam Map<String, String> params) {
        String response = gameService.handlePlayAction(params);
        return ResponseEntity.ok(response);
    }
}
