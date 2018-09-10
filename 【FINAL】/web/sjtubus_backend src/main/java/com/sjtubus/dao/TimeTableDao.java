package com.sjtubus.dao;

import com.sjtubus.entity.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeTableDao extends MongoRepository<TimeTable,String> {
	TimeTable findByStationAndType(String station, String type);
	TimeTable findByStation(String station);
}
