package com.stan.quick_vote.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssociateResponseDTO {
    private String id;
    private String name;
    private String documentNumber;
    private String cooperativeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
