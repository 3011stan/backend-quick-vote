package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.AssociateResponseDTO;
import com.stan.quick_vote.dto.VoteRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.model.Vote;
import com.stan.quick_vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final VotingSessionService votingSessionService;
    private final AssociateService associateService;
    private final ExecutorService executorService;

    private final Map<String, Object> locks = new ConcurrentHashMap<>();

    public void registerVote(VoteRequestDTO voteRequestDTO, String votingSessionId) {
        executorService.submit(() -> {
            try {
                handleVote(voteRequestDTO, votingSessionId);
            } catch (Exception e) {
                log.error("Unexpected error while registering vote for session {}: {}", votingSessionId, e.getMessage(), e);
            }
        });
    }

    void handleVote(VoteRequestDTO voteRequestDTO, String votingSessionId) {
        String associateId = voteRequestDTO.getAssociateId();

        Object lock = locks.computeIfAbsent(votingSessionId, k -> new Object());
        synchronized (lock) {
            try {
                VotingSessionResponseDTO votingSession = votingSessionService.findById(votingSessionId);

                validateSession(votingSession);

                validateVoteAlreadyRegistered(associateId, votingSession.getTopicId());

                AssociateResponseDTO associate = associateService.findById(associateId);

                Vote vote = Vote.builder()
                        .votingSessionId(votingSessionId)
                        .associateId(associate.getId())
                        .topicId(votingSession.getTopicId())
                        .voteChoice(voteRequestDTO.getVoteChoice())
                        .createdAt(LocalDateTime.now())
                        .build();

                voteRepository.save(vote);
                log.info("Vote registered: {}", vote);
            } finally {
                locks.remove(votingSessionId);
            }
        }
    }

    private void validateSession(VotingSessionResponseDTO votingSession) {
        if (LocalDateTime.now().isBefore(votingSession.getStartAt())
                || LocalDateTime.now().isAfter(votingSession.getEndAt())) {
            log.warn("Voting session is not active");
            throw new IllegalStateException("Voting session is not active");
        }
    }

    private void validateVoteAlreadyRegistered(String associateId, String topicId) {
        boolean alreadyVoted = voteRepository.existsByTopicIdAndAssociateId(topicId, associateId);

        if (alreadyVoted) {
            log.warn("Associate has already voted in this topic");
            throw new IllegalStateException("Associate has already voted in this topic");
        }
    }
}
