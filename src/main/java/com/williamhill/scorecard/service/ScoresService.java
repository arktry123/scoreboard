package com.williamhill.scorecard.service;

import com.williamhill.scorecard.domain.ScoreBoard;
import com.williamhill.scorecard.domain.ScoreEmbeddable;
import com.williamhill.scorecard.exception.ScoreCreationException;
import com.williamhill.scorecard.exception.ScoreNotFoundException;
import com.williamhill.scorecard.repository.ScoreBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static java.lang.String.format;

@Service
public class ScoresService {

    private ScoreBoardRepository scoreBoardRepository;

    @Autowired
    public ScoresService(ScoreBoardRepository scoreBoardRepository) {
        this.scoreBoardRepository = scoreBoardRepository;
    }

    public ScoreBoard getScoreBoard(String team1, String team2) {
        Optional<ScoreBoard> optionalScoreBoard = scoreBoardRepository.findById(new ScoreEmbeddable(team1, team2));
        if (optionalScoreBoard.isEmpty()) {
            throw new ScoreNotFoundException(format("Could not find ScoreBoard for %s / %s ", team1, team2));
        } else {
            return optionalScoreBoard.get();
        }
    }

    public ScoreBoard saveScoreBoard(ScoreBoard scoreBoard) {
        ScoreBoard scoreBoard1 = null;
        try {
            scoreBoard1 = scoreBoardRepository.save(scoreBoard);
        } catch (Exception e) {
            throw new ScoreCreationException("Could not create Scoreboard");
        }
        return scoreBoard1;
    }

    public ScoreBoard updateScoreBoard(String team1, String team2, ScoreBoard scoreBoard) {
        ScoreBoard scoreBoard1 = getScoreBoard(team1, team2);

        scoreBoard1.setScore1(scoreBoard.getScore1());
        scoreBoard1.setScore2(scoreBoard.getScore2());
        scoreBoard1.setDate(LocalDate.now());

        return saveScoreBoard(scoreBoard1);
    }
}
