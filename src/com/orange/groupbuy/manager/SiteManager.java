package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Site;


public class SiteManager extends CommonManager {
    public static List<Site> findAllSites(MongoDBClient mongoClient, String countryCode, int offset, int maxCount){
        
        DBCursor cursor = null;
        
        BasicDBObject query = new BasicDBObject();        
        if (!StringUtil.isEmpty(countryCode)){
            
            List<String> countryList = new ArrayList<String>();
            countryList.add(countryCode);
            countryList.add(DBConstants.C_ALL_COUNTRY);

            DBObject in = new BasicDBObject();
            in.put("$in", countryList);
            query.put(DBConstants.F_COUNTRYCODE, in);
        }
        
        DBObject orderBy = new BasicDBObject();
        orderBy.put(DBConstants.F_DOWNLOAD_COUNT, 1);
                
        cursor = mongoClient.find(DBConstants.T_DOWNLOAD_SITE, query, orderBy, offset, maxCount);
        if (cursor == null)
            return Collections.emptyList();

        try{
            List<Site> list = new ArrayList<Site>();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                list.add(new Site(obj));
            }
            cursor.close();     
            return list;
        }catch(Exception e){
            cursor.close();     
            log.error("<findAllSites> but catch exception = " + e.toString(), e);            
            return Collections.emptyList();            
        }           
    }
}
