package com.sjtubus.dao;

//import com.sjtubus.entity.Collection;
//import org.springframework.data.mongodb.repository.MongoRepository;
//
//public interface CollectionDao extends MongoRepository<Collection,String> {
//
//    Collection findByUseridAndUsername(int userid, String username);
//}

import com.sjtubus.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionDao extends JpaRepository<Collection,Integer> {

    List<Collection> findByUsername(String username);

    Collection findByUseridAndUsernameAndShiftid(int userid, String username, String shiftid);

}




