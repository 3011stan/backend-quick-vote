package com.stan.quick_vote.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VotingSessionResponseDTO {
    private String id;
    private String topicId;
    private String name;
    private String cooperativeId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
