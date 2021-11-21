package com.williamhill.scorecard.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "scoreboards")
@IdClass(ScoreEmbeddable.class)
public class ScoreBoard {
    @Id
    private String team1;
    @Id
    private String team2;
    @Column(name = "score1")
    private int score1;
    @Column(name = "score2")
    private int score2;
    @Column(name = "date_updated")
    private LocalDate date;

}
