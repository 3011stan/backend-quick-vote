package com.stan.quick_vote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VotingSessionRequestDTO {
    @NotNull(message = "Topic ID is required")
    private String topicId;
    @NotNull(message = "Name is required")
    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Long durationInMinutes;
}
