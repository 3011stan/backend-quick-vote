package com.stan.quick_vote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stan.quick_vote.dto.VotingSessionRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.service.VotingSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VotingSessionController.class)
public class VotingSessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotingSessionService votingSessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateVotingSession() throws Exception {
        // Arrange
        VotingSessionResponseDTO responseDTO = VotingSessionResponseDTO.builder()
                .id("session123")
                .topicId("topic123")
                .cooperativeId("cooperative123")
                .name("Session Name")
                .startAt(LocalDateTime.of(2024, 12, 25, 12, 0))
                .endAt(LocalDateTime.of(2024, 12, 25, 12, 1))
                .createdAt(LocalDateTime.of(2024, 12, 25, 11, 0))
                .updatedAt(LocalDateTime.of(2024, 12, 25, 11, 30))
                .build();

        Mockito.when(votingSessionService.createVotingSession(any(VotingSessionRequestDTO.class)))
                .thenReturn(responseDTO);

        VotingSessionRequestDTO requestDTO = VotingSessionRequestDTO.builder()
                .topicId("topic123")
                .name("Session Name")
                .build();

        // Act & Assert
        mockMvc.perform(post("/voting-sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("session123"))
                .andExpect(jsonPath("$.topicId").value("topic123"))
                .andExpect(jsonPath("$.cooperativeId").value("cooperative123"))
                .andExpect(jsonPath("$.name").value("Session Name"))
                .andExpect(jsonPath("$.startAt").value("2024-12-25T12:00:00"))
                .andExpect(jsonPath("$.endAt").value("2024-12-25T12:01:00"))
                .andExpect(jsonPath("$.createdAt").value("2024-12-25T11:00:00"))
                .andExpect(jsonPath("$.updatedAt").value("2024-12-25T11:30:00"));
    }
}
