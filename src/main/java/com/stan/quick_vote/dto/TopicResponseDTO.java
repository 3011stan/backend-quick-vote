package com.stan.quick_vote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class TopicResponseDTO {
    private String id;
    private String name;
    private String description;
    private String cooperativeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}