package com.stan.quick_vote.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "voting_sessions")
public class VotingSession {

    @Id
    private String id;

    private String topicId;

    private String cooperativeId;

    private LocalDateTime startAt = LocalDateTime.now();

    private LocalDateTime endAt = LocalDateTime.now().plusMinutes(1);

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
