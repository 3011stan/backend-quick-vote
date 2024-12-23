package com.stan.quick_vote.model;

import com.stan.quick_vote.enums.VoteChoice;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "votes")
@Builder
public class Vote {

    @Id
    private String id;

    private String topicId;

    private String associateId;

    private String votingSessionId;

    private VoteChoice voteChoice;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
