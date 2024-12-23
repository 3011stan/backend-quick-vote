package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.TopicResponseDTO;
import com.stan.quick_vote.dto.VotingSessionRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.dto.VotingSessionResultDTO;
import com.stan.quick_vote.enums.VoteChoice;
import com.stan.quick_vote.model.Vote;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VotingSessionServiceTest {
    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private TopicService topicService;

    @Mock
    private VoteService voteService;

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

    @Test
    @DisplayName("Should generate result successfully for a valid session with votes")
    void testGenerateResult_Success() {
        // Arrange
        String sessionId = "session123";

        VotingSession votingSession = VotingSession.builder()
                .id(sessionId)
                .topicId("topic123")
                .cooperativeId("cooperative123")
                .startAt(LocalDateTime.now().minusMinutes(10))
                .endAt(LocalDateTime.now().plusMinutes(10))
                .build();

        // Mock do repository para encontrar a sessão de votação
        when(votingSessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(votingSession));

        List<Vote> mockVotes = List.of(
                Vote.builder().voteChoice(VoteChoice.YES).build(),
                Vote.builder().voteChoice(VoteChoice.NO).build(),
                Vote.builder().voteChoice(VoteChoice.YES).build()
        );

        // Mock para retornar os votos associados à sessão
        when(voteService.findAllByVotingSessionId(sessionId)).thenReturn(mockVotes);

        // Act
        VotingSessionResultDTO resultDTO = votingSessionService.generateResult(sessionId);

        // Assert
        assertNotNull(resultDTO, "ResultDTO should not be null");
        assertEquals(sessionId, resultDTO.getSessionId(), "Session ID should match");
        assertEquals("topic123", resultDTO.getTopicId(), "Topic ID should match");
        assertEquals(3, resultDTO.getTotalVotes(), "Total votes should match");
        assertEquals("YES", resultDTO.getWinnerChoice(), "Winner choice should match");
        assertEquals(2, resultDTO.getVoteCounts().get("YES"), "YES votes should be counted correctly");
        assertEquals(1, resultDTO.getVoteCounts().get("NO"), "NO votes should be counted correctly");

        verify(votingSessionRepository, times(1)).findById(sessionId);
        verify(voteService, times(1)).findAllByVotingSessionId(sessionId);
    }


    @Test
    @DisplayName("Should throw exception when session is not found")
    void testGenerateResult_SessionNotFound() {
        // Arrange
        String sessionId = "session123";
        when(votingSessionRepository.findById(sessionId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votingSessionService.generateResult(sessionId);
        });

        assertEquals("Voting session not found", exception.getMessage());
        verify(votingSessionRepository, times(1)).findById(sessionId);
        verify(voteService, never()).findAllByVotingSessionId(sessionId);
    }

    @Test
    @DisplayName("Should throw exception when no votes are found for session")
    void testGenerateResult_NoVotesFound() {
        // Arrange
        String sessionId = "session123";

        VotingSession votingSession = VotingSession.builder()
                .id(sessionId)
                .topicId("topic123")
                .cooperativeId("cooperative123")
                .startAt(LocalDateTime.now().minusMinutes(10))
                .endAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(votingSessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(votingSession));
        when(voteService.findAllByVotingSessionId(sessionId)).thenReturn(List.of());

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            votingSessionService.generateResult(sessionId);
        });

        assertEquals("No votes found for this session", exception.getMessage());
        verify(votingSessionRepository, times(1)).findById(sessionId);
        verify(voteService, times(1)).findAllByVotingSessionId(sessionId);
    }

    @Test
    @DisplayName("Should throw exception when no winner can be determined (tie)")
    void testGenerateResult_Tie() {
        // Arrange
        String sessionId = "session123";

        VotingSession votingSession = VotingSession.builder()
                .id(sessionId)
                .topicId("topic123")
                .cooperativeId("cooperative123")
                .startAt(LocalDateTime.now().minusMinutes(10))
                .endAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(votingSessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(votingSession));

        List<Vote> mockVotes = List.of(
                Vote.builder().voteChoice(VoteChoice.YES).build(),
                Vote.builder().voteChoice(VoteChoice.NO).build()
        );

        when(voteService.findAllByVotingSessionId(sessionId)).thenReturn(mockVotes);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            votingSessionService.generateResult(sessionId);
        });

        assertEquals("Could not determine winner", exception.getMessage());
        verify(votingSessionRepository, times(1)).findById(sessionId);
        verify(voteService, times(1)).findAllByVotingSessionId(sessionId);
    }
}
