package com.stan.quick_vote.repository;

import com.stan.quick_vote.model.Cooperative;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CooperativeRepository extends MongoRepository<Cooperative, String> {

}
