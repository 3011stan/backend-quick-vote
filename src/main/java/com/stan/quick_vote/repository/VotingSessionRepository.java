package com.stan.quick_vote.repository;

import com.stan.quick_vote.model.VotingSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VotingSessionRepository extends MongoRepository<VotingSession, String> {
    Optional<VotingSession> findById(String id);
}
