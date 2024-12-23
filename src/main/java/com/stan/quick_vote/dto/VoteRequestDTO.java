package com.stan.quick_vote.dto;

import com.stan.quick_vote.enums.VoteChoice;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteRequestDTO {

    @NotNull(message = "Associate ID is required")
    private String associateId;

    @NotNull(message = "Vote choice is required")
    private VoteChoice voteChoice;
}