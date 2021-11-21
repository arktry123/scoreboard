package com.williamhill.scorecard.resource;

import com.williamhill.scorecard.domain.ScoreBoard;
import com.williamhill.scorecard.exception.ScoreCreationException;
import com.williamhill.scorecard.service.ScoresService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/scores")
@Slf4j
public class ScoresResource {

    @Autowired
    private ScoresService scoresService;

    @GetMapping("/{team1}/{team2}")
    @ApiOperation("gets an existing Operation")
    public ScoreBoard getScoreBoard(@PathVariable @NotNull String team1, @PathVariable @NotNull String team2) {
        log.info("Find score by id {} / {}", team1, team2);
        return scoresService.getScoreBoard(team1, team2);
    }

    @PostMapping
    @ApiOperation(value = "Create a score board")
    public ResponseEntity<ScoreBoard> createScoreBoard(@RequestBody ScoreBoard scoreBoard) {
        ScoreBoard createdScoreBoard = scoresService.saveScoreBoard(scoreBoard);

        if (createdScoreBoard == null) {
            throw new ScoreCreationException("Error when Creating ScoreBoard");
        }
        return new ResponseEntity<>(createdScoreBoard, HttpStatus.CREATED);
    }


    @PutMapping("/{team1}/{team2}")
    @ApiOperation(value = "updates a Scoreboard")
    public ResponseEntity<ScoreBoard> updateScoreBoard(@RequestBody ScoreBoard scoreBoard,
                                                       @PathVariable String team1, @PathVariable String team2) {


        final ScoreBoard updatedScoreBoard = scoresService.updateScoreBoard(team1, team2, scoreBoard);
        return ResponseEntity.ok(updatedScoreBoard);
    }
}

