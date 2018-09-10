package com.sjtubus.controller;

import com.sjtubus.dao.CollectionDao;
import com.sjtubus.entity.Collection;
import com.sjtubus.model.response.CollectionResponse;
import com.sjtubus.model.response.HttpResponse;
import com.sjtubus.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    /*
     * @description: 添加班次收藏
     * @date: 2018/8/8 14:32
     * @params:
     * @return:
     */

    @RequestMapping(value = "/addCollect", method = RequestMethod.POST)
    public HttpResponse addCollections(int userid,
                                      String username,
                                      String shiftid){
        HttpResponse response = new HttpResponse();
        boolean result = collectionService.addCollection(userid, username, shiftid);
        if(result){
            response.setMsg("收藏成功!");
            response.setError(0);
            return response;
        }else{
            response.setMsg("收藏失败!");
            response.setError(1);
            return response;
        }
    }

    /*
     * @description: 显示班次收藏
     * @date: 2018/9/3 17:19
     * @params:
     * @return:
     */
    @RequestMapping(value = "/infos", method = RequestMethod.GET)
    public CollectionResponse getCollections(String username){
        CollectionResponse response = new CollectionResponse();
        List<Collection> collection = collectionService.getCollection(username);
        response.setCollections(collection);
        return response;
    }

    /*
     * @description: 取消班次收藏
     * @date: 2018/9/3 21:10
     * @params:
     * @return:
     */

    @RequestMapping(value = "/deleteCollect", method = RequestMethod.POST)
    public HttpResponse deleteCollections(int userid,
                                       String username,
                                       String shiftid){
        HttpResponse response = new HttpResponse();
        String result = collectionService.deleteCollection(userid, username, shiftid);
        response.setMsg(result);
        return response;
    }
}
