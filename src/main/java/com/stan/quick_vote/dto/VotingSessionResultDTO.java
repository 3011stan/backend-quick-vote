package com.stan.quick_vote.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class VotingSessionResultDTO {
    private String sessionId;
    private String topicId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private long totalVotes;
    private Map<String, Long> voteCounts;
    private String winnerChoice;
}
