package com.sjtubus.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.sjtubus.dao.CollectionDao;
import com.sjtubus.entity.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionService {

    @Autowired
    private CollectionDao collectionDao;

    /**
      * @description: 添加用户的班次收藏
      * @date: 2018/08/08 14:36
      * @params:
      * @return:
      */
    public boolean addCollection(int userid, String username, String shiftid) {
        Collection collection = new Collection();
        collection.setUserid(userid);
        collection.setUsername(username);
        collection.setShiftid(shiftid);
        collection.setFrequence(0);
        collectionDao.save(collection);
        return true;
    }

    /**
     * @description: 显示的班次收藏
     * @date: 2018/09/03 17:23
     * @params:
     * @return:
     */
    public List<Collection> getCollection(String username) {
        List<Collection> collections = collectionDao.findByUsername(username);

        if(collections == null || collections.size()==0) {
            return new ArrayList<>();
        }
        return collections;
    }

    /**
     * @description: 取消班次收藏
     * @date: 2018/09/03 21:15
     * @params:
     * @return:
     */
    public String deleteCollection(int userid, String username, String shiftid) {
        Collection oldcollection = collectionDao.findByUseridAndUsernameAndShiftid(userid, username, shiftid);
        if(oldcollection != null ){
            collectionDao.delete(oldcollection);
            return "success";
        }
        else {
            return "fail";
        }
    }

//    private static final String MONGO_HOST = "localhost";
//    private static final Integer MONGO_PORT = 27017;
//    private static final String MONGO_DB_NAME = "sjtu_bus";
//    private static final String MONGO_COLLECTION_NAME = "collection";
//
//    /**
//     * @description: 添加用户的班次收藏
//     * @date: 2018/08/08 14:36
//     * @params:
//     * @return:
//     */
//    public String addCollection(int userid, String username, String shiftid){
//
//        MongoClient mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
//        MongoDatabase db = mongoClient.getDatabase(MONGO_DB_NAME);
//        DBCollection dbCollection = (DBCollection) db.getCollection(MONGO_COLLECTION_NAME);
//
//        Collection collection = collectionDao.findByUseridAndUsername(userid, username);
//        if (collection == null){
//            //插入一条文档
//            BasicDBObject document = new BasicDBObject();
//            document.put("userid", userid);
//            document.put("username", username);
//            List<String> shiftList = new ArrayList<>();
//            shiftList.add(shiftid);
//            document.put("shiftList", shiftList);
//            dbCollection.insert(document);
//        } else {
//            //查询一条文档
////            BasicDBObject searchObj = new BasicDBObject();
////            searchObj.put("userid", userid);
////            DBCursor cursor = dbCollection.find(searchObj);
////            List<String> shiftList = searchObj.g
////            //修改一条文档
////            BasicDBObject newDocument = new BasicDBObject();
////            newDocument.put()
//        }
//
//        return "success";
//    }
}
