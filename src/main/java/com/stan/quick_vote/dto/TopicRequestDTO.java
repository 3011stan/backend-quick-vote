package com.stan.quick_vote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TopicRequestDTO {

    @NotNull(message = "Name is mandatory")
    private String name;

    private String description;

    @NotNull(message = "Cooperative ID is mandatory")
    private String cooperativeId;

}
