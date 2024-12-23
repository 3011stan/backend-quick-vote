package com.stan.quick_vote.repository;

import com.stan.quick_vote.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<Vote, String> {
    boolean existsByTopicIdAndAssociateId(String topicId, String associateId);

}
