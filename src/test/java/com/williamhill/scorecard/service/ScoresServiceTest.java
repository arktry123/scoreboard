package com.williamhill.scorecard.service;

import com.williamhill.scorecard.domain.ScoreBoard;
import com.williamhill.scorecard.domain.ScoreEmbeddable;
import com.williamhill.scorecard.exception.ScoreCreationException;
import com.williamhill.scorecard.exception.ScoreNotFoundException;
import com.williamhill.scorecard.repository.ScoreBoardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoresServiceTest {

    @Mock
    private ScoreBoardRepository scoreBoardRepository;

    @DisplayName("When the teams are found, it should return the correct score board")
    @Test
    public void whenGivenTeamsShouldReturnScoreIfFound() {
        ScoresService scoresService = new ScoresService(scoreBoardRepository);
        ScoreBoard scoreBoard = ScoreBoard.builder().team1("team1").score1(1).team2("team2").score2(0).build();
        when(scoreBoardRepository.findById(new ScoreEmbeddable("team1", "team2"))).thenReturn(Optional.ofNullable(scoreBoard));

        ScoreBoard actualScore = scoresService.getScoreBoard("team1", "team2");
        assertThat(actualScore.getScore1()).isEqualTo(1);
        assertThat(actualScore.getScore2()).isEqualTo(0);
        assertThat(actualScore.getTeam1()).isEqualTo("team1");
        assertThat(actualScore.getTeam2()).isEqualTo("team2");
    }

    @DisplayName("When the teams are not found, it should throw exception")
    @Test
    public void testScoreBoardNotFound() {
        ScoresService scoresService = new ScoresService(scoreBoardRepository);
        ScoreBoard scoreBoard = ScoreBoard.builder().team1("team1").score1(1).team2("team2").score2(0).build();
        when(scoreBoardRepository.findById(new ScoreEmbeddable("team1", "team2"))).thenReturn(Optional.empty());
        assertThrows(ScoreNotFoundException.class, () -> scoresService.getScoreBoard("team1", "team2"));
    }

    @DisplayName("Create Scoreboard - positive case")
    @Test
    public void testCreateScore() {
        ScoresService scoresService = new ScoresService(scoreBoardRepository);
        ScoreBoard scoreBoard = ScoreBoard.builder().team1("team1").score1(1).team2("team2").score2(0).build();
        when(scoreBoardRepository.save(scoreBoard)).thenReturn(scoreBoard);

        ScoreBoard actualScore = scoresService.saveScoreBoard(scoreBoard);
        assertThat(actualScore.getScore1()).isEqualTo(1);
        assertThat(actualScore.getScore2()).isEqualTo(0);
        assertThat(actualScore.getTeam1()).isEqualTo("team1");
        assertThat(actualScore.getTeam2()).isEqualTo("team2");
    }

    @DisplayName("Create Scoreboard - exception case")
    @Test
    public void testCreateScoreExceptionCase() {
        ScoresService scoresService = new ScoresService(scoreBoardRepository);
        ScoreBoard scoreBoard = ScoreBoard.builder().team1("team1").score1(1).team2("team2").score2(0).build();
        when(scoreBoardRepository.save(scoreBoard)).thenThrow(new RuntimeException());

        assertThrows(ScoreCreationException.class, () -> scoresService.saveScoreBoard(scoreBoard));
    }

    @DisplayName("Update Scoreboard - positive case")
    @Test
    public void testUpdateScore() {
        ScoresService scoresService = new ScoresService(scoreBoardRepository);
        ScoreBoard scoreBoard = ScoreBoard.builder().team1("team1").score1(1).team2("team2").score2(0).build();
        ScoreBoard scoreBoardUpdated = ScoreBoard.builder().team1("team1").score1(1).team2("team2").score2(1).build();
        when(scoreBoardRepository.findById(new ScoreEmbeddable("team1", "team2"))).thenReturn(Optional.of(scoreBoard));
        when(scoreBoardRepository.save(scoreBoard)).thenReturn(scoreBoardUpdated);

        ScoreBoard actualScore = scoresService.updateScoreBoard("team1", "team2", scoreBoard);
        assertThat(actualScore.getScore1()).isEqualTo(1);
        assertThat(actualScore.getScore2()).isEqualTo(1);
        assertThat(actualScore.getTeam1()).isEqualTo("team1");
        assertThat(actualScore.getTeam2()).isEqualTo("team2");
    }

    @DisplayName("Update Scoreboard - exception case")
    @Test
    public void testUpdateScoreExceptionCase() {
        ScoresService scoresService = new ScoresService(scoreBoardRepository);
        ScoreBoard scoreBoard = ScoreBoard.builder().team1("team1").score1(1).team2("team2").score2(0).build();
        when(scoreBoardRepository.findById(new ScoreEmbeddable("team1", "team2"))).thenReturn(Optional.empty());

        assertThrows(ScoreNotFoundException.class,
                () -> scoresService.updateScoreBoard("team1", "team2", scoreBoard));
    }
}
