package com.stan.quick_vote.service;

import com.stan.quick_vote.dto.TopicResponseDTO;
import com.stan.quick_vote.dto.VotingSessionRequestDTO;
import com.stan.quick_vote.dto.VotingSessionResponseDTO;
import com.stan.quick_vote.dto.VotingSessionResultDTO;
import com.stan.quick_vote.enums.VoteChoice;
import com.stan.quick_vote.model.Vote;
import com.stan.quick_vote.model.VotingSession;
import com.stan.quick_vote.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class VotingSessionService {
    private final VotingSessionRepository votingSessionRepository;
    private final VoteService voteService;
    private final TopicService topicService;

    public VotingSessionResponseDTO findById(String id) {
        VotingSession session = votingSessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voting session not found"));

        return toResponseDTO(session);
    }

    public VotingSessionResultDTO generateResult(String sessionId) {
        VotingSession session = votingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Voting session not found"));

        List<Vote> votes = voteService.findAllByVotingSessionId(sessionId);

        if (votes.isEmpty()) {
            throw new IllegalStateException("No votes found for this session");
        }

        long yesVotes = votes.stream().filter(vote -> vote.getVoteChoice() == VoteChoice.YES).count();
        long noVotes = votes.stream().filter(vote -> vote.getVoteChoice() == VoteChoice.NO).count();

        if (yesVotes == noVotes) {
            throw new IllegalStateException("Could not determine winner");
        }

        String winnerChoice = yesVotes > noVotes ? "YES" : "NO";

        Map<String, Long> voteCounts = Map.of(
                "YES", yesVotes,
                "NO", noVotes
        );

        VotingSessionResultDTO resultDTO = VotingSessionResultDTO.builder()
                .sessionId(sessionId)
                .topicId(session.getTopicId())
                .startAt(session.getStartAt())
                .endAt(session.getEndAt())
                .totalVotes(votes.size())
                .voteCounts(voteCounts)
                .winnerChoice(winnerChoice)
                .build();

        log.info("Voting session result generated for sessionId {}: {}", sessionId, resultDTO);

        return resultDTO;
    }


    public VotingSessionResponseDTO createVotingSession(VotingSessionRequestDTO dto) {
        TopicResponseDTO topicResponseDTO = topicService.findById(dto.getTopicId());

        LocalDateTime startAt = Optional.ofNullable(dto.getStartAt()).orElse(LocalDateTime.now());
        LocalDateTime endAt = Optional.ofNullable(dto.getEndAt()).orElse(startAt.plusMinutes(1));

        VotingSession session = VotingSession.builder()
                .topicId(dto.getTopicId())
                .name(dto.getName())
                .startAt(startAt)
                .endAt(endAt)
                .cooperativeId(topicResponseDTO.getCooperativeId())
                .build();

        session = votingSessionRepository.save(session);

        return toResponseDTO(session);
    }

    public VotingSessionResponseDTO toResponseDTO(VotingSession session) {
        return VotingSessionResponseDTO.builder()
                .id(session.getId())
                .topicId(session.getTopicId())
                .cooperativeId(session.getCooperativeId())
                .startAt(session.getStartAt())
                .name(session.getName())
                .endAt(session.getEndAt())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }

}
