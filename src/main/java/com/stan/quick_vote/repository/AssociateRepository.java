package com.stan.quick_vote.repository;

import com.stan.quick_vote.model.Associate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssociateRepository extends MongoRepository<Associate, String> {

}
