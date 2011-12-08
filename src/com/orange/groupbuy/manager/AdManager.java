package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Ad;

public class AdManager extends CommonManager {

    public static List<Ad> findAd(MongoDBClient mongoClient, String requestFrom) {
        
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_AD_TARGET, requestFrom);
        
        DBCursor cursor = mongoClient.findAll(DBConstants.T_AD);
        if (cursor == null)
            return Collections.emptyList();
        
        List<Ad> adList = new ArrayList<Ad>();
        Iterator<?> iter = cursor.iterator();
        if( iter == null){
            cursor.close();
            return Collections.emptyList();
        }
        
        while (iter.hasNext()){
            BasicDBObject obj = (BasicDBObject)iter.next();
            Ad ad = new Ad(obj);
            adList.add(ad);
        }
        
        cursor.close();        
        return adList;
    }


}
