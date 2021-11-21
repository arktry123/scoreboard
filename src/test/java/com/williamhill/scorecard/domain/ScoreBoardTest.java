package com.williamhill.scorecard.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ScoreBoardTest {

    /*@Test
    public void emptyFields() {
        assertThrows(ConstraintViolationException.class, () -> ScoreBoard.builder().build());
    }

    @Test
    public void negativeScore() {
        final ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> ScoreBoard.builder()
                        .team1("UK")
                        .team2("USA")
                        .date(LocalDate.now())
                        .goals1((byte) 3)
                        .goals2((byte) -1)
                        .build());

        assertThat(exception.getConstraintViolations().stream().findFirst().get().getMessage(),
                equalTo("com.score.test.scorecard.validation.negative_goals2"));
    }*/

}