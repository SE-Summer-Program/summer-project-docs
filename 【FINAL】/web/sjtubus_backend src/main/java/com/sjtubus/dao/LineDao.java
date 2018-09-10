package com.sjtubus.dao;

import com.sjtubus.entity.Line;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LineDao extends MongoRepository<Line,String> {
	Line findByName(String name);
}