package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.AssociateResponseDTO;
import com.stan.quick_vote.dto.VoteRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.enums.VoteChoice;
import com.stan.quick_vote.exceptions.NotFoundException;
import com.stan.quick_vote.model.Vote;
import com.stan.quick_vote.model.VotingSession;
import com.stan.quick_vote.repository.VoteRepository;
import com.stan.quick_vote.repository.VotingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private VotingSessionService votingSessionService;

    @Mock
    private AssociateService associateService;

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private VoteService voteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should register vote successfully")
    void testRegisterVote_Success() throws Exception {
        // Arrange
        String votingSessionId = "session123";
        String associateId = "associate123";
        String topicId = "topic123";
        String cooperativeId = "cooperative123";

        VoteRequestDTO voteRequestDTO = VoteRequestDTO.builder()
                .associateId(associateId)
                .voteChoice(VoteChoice.YES)
                .build();

        VotingSession votingSession = VotingSession.builder()
                .id(votingSessionId)
                .startAt(LocalDateTime.now().minusMinutes(1))
                .endAt(LocalDateTime.now().plusMinutes(5))
                .topicId(topicId)
                .cooperativeId(cooperativeId) // Adiciona cooperativeId
                .build();

        AssociateResponseDTO associateResponseDTO = AssociateResponseDTO.builder()
                .id(associateId)
                .name("John Doe")
                .cooperativeId(cooperativeId) // Adiciona cooperativeId
                .build();

        when(votingSessionService.findById(votingSessionId)).thenReturn(VotingSessionResponseDTO.builder()
                .id(votingSessionId)
                .startAt(votingSession.getStartAt())
                .endAt(votingSession.getEndAt())
                .topicId(topicId)
                .cooperativeId(cooperativeId)
                .build());
        when(voteRepository.existsByTopicIdAndAssociateId(topicId, associateId)).thenReturn(false);
        when(associateService.findById(associateId)).thenReturn(associateResponseDTO);
        when(votingSessionRepository.findById(votingSessionId)).thenReturn(Optional.of(votingSession));

        // Act
        voteService.registerVote(voteRequestDTO, votingSessionId);

        // Assert
        ArgumentCaptor<Runnable> captor = ArgumentCaptor.forClass(Runnable.class);
        verify(executorService).submit(captor.capture());
        Runnable task = captor.getValue();

        task.run();

        ArgumentCaptor<Vote> voteCaptor = ArgumentCaptor.forClass(Vote.class);
        verify(voteRepository).save(voteCaptor.capture());
        Vote savedVote = voteCaptor.getValue();

        assertNotNull(savedVote);
        assertEquals(votingSessionId, savedVote.getVotingSessionId());
        assertEquals(associateId, savedVote.getAssociateId());
        assertEquals(topicId, savedVote.getTopicId());
        assertEquals(VoteChoice.YES, savedVote.getVoteChoice());
    }

    @Test
    void testRegisterVote_Fail_AlreadyVoted() {
        // Arrange
        String votingSessionId = "session123";
        String associateId = "associate123";
        String topicId = "topic123";

        VoteRequestDTO voteRequestDTO = VoteRequestDTO.builder()
                .associateId(associateId)
                .voteChoice(VoteChoice.YES)
                .build();

        VotingSession votingSession = VotingSession.builder()
                .id(votingSessionId)
                .startAt(LocalDateTime.now().minusMinutes(1))
                .endAt(LocalDateTime.now().plusMinutes(5))
                .topicId(topicId)
                .build();

        when(votingSessionRepository.findById(votingSessionId)).thenReturn(Optional.of(votingSession));

        when(voteRepository.existsByTopicIdAndAssociateId(topicId, associateId)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            voteService.handleVote(voteRequestDTO, votingSessionId);
        });

        assertEquals("Associate has already voted in this topic", exception.getMessage());

        verify(voteRepository, times(1)).existsByTopicIdAndAssociateId(topicId, associateId);
        verify(voteRepository, never()).save(any(Vote.class));
        verify(associateService, never()).findById(any());
    }


    @Test
    @DisplayName("Should fail to register vote when associate is not found")
    void testRegisterVote_Fail_AssociateNotFound() {
        // Arrange
        String votingSessionId = "session123";
        String associateId = "associate123";
        String topicId = "topic123";

        VoteRequestDTO voteRequestDTO = VoteRequestDTO.builder()
                .associateId(associateId)
                .voteChoice(VoteChoice.YES)
                .build();

        VotingSession votingSession = VotingSession.builder()
                .id(votingSessionId)
                .startAt(LocalDateTime.now().minusMinutes(1))
                .endAt(LocalDateTime.now().plusMinutes(5))
                .topicId(topicId)
                .cooperativeId("cooperative123")
                .build();

        when(votingSessionRepository.findById(votingSessionId)).thenReturn(java.util.Optional.of(votingSession));
        when(voteRepository.existsByTopicIdAndAssociateId(topicId, associateId)).thenReturn(false);
        when(associateService.findById(associateId)).thenThrow(new NotFoundException("Associate not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            voteService.handleVote(voteRequestDTO, votingSessionId);
        });

        assertEquals("Associate not found", exception.getMessage());

        verify(votingSessionRepository, times(1)).findById(votingSessionId);
        verify(voteRepository, times(1)).existsByTopicIdAndAssociateId(topicId, associateId);
        verify(associateService, times(1)).findById(associateId);
        verify(voteRepository, never()).save(any(Vote.class));
    }
}
