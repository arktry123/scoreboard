package com.williamhill.scorecard.repository;


import com.williamhill.scorecard.domain.ScoreBoard;
import com.williamhill.scorecard.domain.ScoreEmbeddable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ScoreBoardRepository extends JpaRepository<ScoreBoard, ScoreEmbeddable> {
}
