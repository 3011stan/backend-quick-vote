package com.stan.quick_vote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssociateRequestDTO {
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Document number is required")
    private String documentNumber;
    @NotNull(message = "Cooperative ID is required")
    private String cooperativeId;
}
