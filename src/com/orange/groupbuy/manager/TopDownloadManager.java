package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.RankUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Site;
import com.orange.groupbuy.dao.TopDownload;

public class TopDownloadManager extends CommonManager {

    public static List<TopDownload> findAllTopDownloadItems(MongoDBClient mongoClient, String countryCode,
            int offset, int maxCount){
       
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
       orderBy.put(DBConstants.F_SCORE, -1);
               
       cursor = mongoClient.find(DBConstants.T_TOP_DOWNLOAD, query, orderBy, offset, maxCount);
       if (cursor == null){
           return Collections.emptyList();
       }

       try{
           List<TopDownload> list = new ArrayList<TopDownload>();
           while (cursor.hasNext()) {
               DBObject obj = cursor.next();
               list.add(new TopDownload(obj));
           }
           cursor.close();     
           return list;
       }catch(Exception e){
           cursor.close();     
           log.error("<findAllTopDownloadItems> but catch exception = " + e.toString(), e);            
           return Collections.emptyList();            
       }           
   }

    
    public static TopDownload findTopDownloadByURL(MongoDBClient mongoClient, String url){
        DBObject obj = mongoClient.findOne(DBConstants.T_TOP_DOWNLOAD, DBConstants.F_FILE_URL, url);
        if (obj == null)
            return null;
        else
            return new TopDownload(obj);
    }
    
    public static void createOrUpdateTopDownload(MongoDBClient mongoClient, String appId, String deviceId, String countryCode,
            String language, String fileType, String fileURL, int fileSize, String fileName, String siteURL,
            String siteName) {
        
        // find item by URL
        TopDownload item = findTopDownloadByURL(mongoClient, fileURL);
        boolean found = false;
        
        if (item == null){
            // not found, create new record, and calcuate score
            found = false;
            item = new TopDownload(appId, deviceId, countryCode, language,
                    fileType, fileURL, fileSize, fileName, siteURL, siteName);
        }
        
        else{
            // found, update the record count, score, and modify date
            found = true;            
            item.incDownloadCount();
        }
        
        // TODO later need to use findAndModify to avoid concurrent issue
        // mongoClient.findAndModifyInsert(DBConstants.T_TOP_DOWNLOAD, query, update);
        
        // re-calcuate score
        recalcuateScore(item);
        
        // update modify date
        item.updateModifyDate();                            
        if (!found){
            mongoClient.insert(DBConstants.T_TOP_DOWNLOAD, item.getDbObject());
        }
        else{
            mongoClient.save(DBConstants.T_TOP_DOWNLOAD, item.getDbObject());
        }
    }
    
    private static void recalcuateScore(TopDownload item) {
        double score = RankUtil.calcTopScore_2(item.getDownloadCount(), item.getCreateDate());
        item.setScore(score);
    }
    
}
