package com.williamhill.scorecard.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.williamhill.scorecard.domain.ScoreBoard;
import com.williamhill.scorecard.exception.ScoreCreationException;
import com.williamhill.scorecard.exception.ScoreNotFoundException;
import com.williamhill.scorecard.service.ScoresService;
import com.williamhill.scorecard.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScoresResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScoresService scoresService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getScorePositiveCase()
            throws Exception {

        ScoreBoard scoreBoard = ScoreBoard.builder()
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .score1(3)
                .score2(4)
                .build();

        given(scoresService.getScoreBoard("UK", "USA")).willReturn(scoreBoard);

        this.mockMvc.perform(get("/scores/UK/USA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team1", is(scoreBoard.getTeam1())))
                .andExpect(jsonPath("$.team2", is(scoreBoard.getTeam2())))
                .andExpect(jsonPath("$.score1", is(scoreBoard.getScore1())))
                .andExpect(jsonPath("$.score2", is(scoreBoard.getScore2())));
    }

    @Test
    public void getScoreNegitiveCase()
            throws Exception {

        given(scoresService.getScoreBoard("UK", "USA")).willThrow(new ScoreNotFoundException("test"));

        this.mockMvc.perform(get("/scores/UK/USA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateScore() throws Exception {

        ScoreBoard scoreBoard = ScoreBoard.builder()
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .score1(3)
                .score2(4)
                .build();

        doReturn(scoreBoard).when(scoresService).saveScoreBoard(any());
        this.mockMvc.perform(post("/scores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(scoreBoard)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.team1", is(scoreBoard.getTeam1())));
    }

    @Test
    public void testCreateScoreNegativeCase() throws Exception {

        ScoreBoard scoreBoard = ScoreBoard.builder()
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .score1(3)
                .score2(4)
                .build();

        given(scoresService.saveScoreBoard(any())).willThrow(new ScoreCreationException("test"));

        this.mockMvc.perform(post("/scores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(scoreBoard)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdateScore() throws Exception {
        ScoreBoard scoreBoard = ScoreBoard.builder()
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .score1(3)
                .score2(4)
                .build();
        given(scoresService.updateScoreBoard(any(), any(), any())).willReturn(scoreBoard);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        this.mockMvc.perform(put("/scores/" + scoreBoard.getTeam1() + "/" + scoreBoard.getTeam2())
                .content(mapper.writeValueAsString(scoreBoard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team1", is(scoreBoard.getTeam1())))
                .andExpect(jsonPath("$.team2", is(scoreBoard.getTeam2())))
                .andExpect(jsonPath("$.score1", is(scoreBoard.getScore1())))
                .andExpect(jsonPath("$.score2", is(scoreBoard.getScore2())));
    }

    @Test
    public void testUpdateScoreNegativeCase() throws Exception {
        ScoreBoard scoreBoard = ScoreBoard.builder()
                .team1("UK")
                .team2("USA")
                .date(LocalDate.now())
                .score1(3)
                .score2(4)
                .build();
        given(scoresService.updateScoreBoard(any(), any(), any())).willCallRealMethod();
        given(scoresService.getScoreBoard(any(), any())).willThrow(new ScoreNotFoundException("test"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        this.mockMvc.perform(put("/scores/UK/USA")
                .content(mapper.writeValueAsString(scoreBoard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
