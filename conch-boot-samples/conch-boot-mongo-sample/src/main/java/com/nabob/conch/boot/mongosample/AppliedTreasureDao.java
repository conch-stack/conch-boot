package com.nabob.conch.boot.mongosample;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class AppliedTreasureDao {

    @Resource
    private MongoTemplate mongoTemplate;

    public void save(AppliedTreasure appliedTreasure) {
        AppliedTreasure save = mongoTemplate.save(appliedTreasure);
        System.out.println("/********************save:: " + save);
    }

    public long count() {
        return mongoTemplate.count(new Query(), AppliedTreasure.class);
    }

    public List<AppliedTreasure> findByPages(long begin, int limit) {
        Query query = new Query().skip(begin).limit(limit);
        return mongoTemplate.find(query, AppliedTreasure.class);
    }
}
