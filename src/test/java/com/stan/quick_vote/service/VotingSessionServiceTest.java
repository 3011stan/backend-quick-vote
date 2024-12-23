package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.TopicResponseDTO;
import com.stan.quick_vote.dto.VotingSessionRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.model.VotingSession;
import com.stan.quick_vote.repository.VotingSessionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotingSessionServiceTest {
    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private TopicService topicService;

    @InjectMocks
    private VotingSessionService votingSessionService;

    @Test
    @DisplayName("Should create a voting session successfully")
    void testCreateVotingSession() {
        // Arrange
        VotingSessionRequestDTO requestDTO = VotingSessionRequestDTO.builder()
                .topicId("topic123")
                .name("Session Name")
                .build();

        LocalDateTime fixedNow = LocalDateTime.of(2024, 12, 25, 12, 0);
        LocalDateTime fixedEnd = fixedNow.plusMinutes(1);

        TopicResponseDTO mockedTopicResponse = TopicResponseDTO.builder()
                .id("topic123")
                .name("Topic Name")
                .description("Topic Description")
                .createdAt(fixedNow)
                .updatedAt(fixedNow)
                .cooperativeId("cooperative123")
                .build();

        when(topicService.findById("topic123")).thenReturn(mockedTopicResponse);

        VotingSession savedSession = VotingSession.builder()
                .id("session456")
                .topicId("topic123")
                .cooperativeId("cooperative123")
                .name("Session Name")
                .startAt(fixedNow)
                .endAt(fixedEnd)
                .build();

        when(votingSessionRepository.save(any())).thenReturn(savedSession);

        // Act
        VotingSessionResponseDTO responseDTO = votingSessionService.createVotingSession(requestDTO);

        // Assert
        assertNotNull(responseDTO, "response should not be null");
        assertEquals("session456", responseDTO.getId(), "should return the saved session id");
        assertEquals("topic123", responseDTO.getTopicId(), "topicId should be the same as the request");
        assertEquals("cooperative123", responseDTO.getCooperativeId());
        assertEquals("Session Name", responseDTO.getName());

        verify(topicService, times(1)).findById("topic123");

        verify(votingSessionRepository, times(1)).save(any(VotingSession.class));

        ArgumentCaptor<VotingSession> captor = ArgumentCaptor.forClass(VotingSession.class);
        verify(votingSessionRepository).save(captor.capture());
        VotingSession capturedSession = captor.getValue();

        assertNotNull(capturedSession.getStartAt(), "startAt should be defined");
        assertNotNull(capturedSession.getEndAt(), "endAt should be defined");
        assertEquals(capturedSession.getStartAt().plusMinutes(1), capturedSession.getEndAt(),
                "endAt should be startAt + 1 minute");
    }
}
