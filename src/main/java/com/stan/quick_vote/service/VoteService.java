package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.AssociateResponseDTO;
import com.stan.quick_vote.dto.VoteRequestDTO;
import com.stan.quick_vote.exceptions.ForbiddenException;
import com.stan.quick_vote.exceptions.NotFoundException;
import com.stan.quick_vote.model.Vote;
import com.stan.quick_vote.model.VotingSession;
import com.stan.quick_vote.repository.VoteRepository;
import com.stan.quick_vote.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    private final VoteRepository voteRepository;
    private final VotingSessionRepository votingSessionRepository;
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
                VotingSession votingSession = votingSessionRepository.findById(votingSessionId)
                        .orElseThrow(() -> new NotFoundException("Voting session not found"));

                validateSession(votingSession);

                validateVoteAlreadyRegistered(associateId, votingSession.getTopicId());

                AssociateResponseDTO associate = associateService.findById(associateId);

                if (!votingSession.getCooperativeId().equals(associate.getCooperativeId())) {
                    log.warn("Associate is not part of the cooperative");
                    throw new ForbiddenException("Associate is not part of the cooperative");
                }

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

    private void validateSession(VotingSession votingSession) {
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

    public List<Vote> findAllByVotingSessionId(String votingSessionId) {
        return voteRepository.findAllByVotingSessionId(votingSessionId);
    }

}
