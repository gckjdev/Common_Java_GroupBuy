package com.orange.groupbuy.manager;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.TopDownload;

public class TopDownloadManager extends CommonManager {

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
        // item.recalcuateScore();
        
        // update modify date
        item.updateModifyDate();                            
        if (!found){
            mongoClient.insert(DBConstants.T_TOP_DOWNLOAD, item.getDbObject());
        }
        else{
            mongoClient.save(DBConstants.T_TOP_DOWNLOAD, item.getDbObject());
        }
    }

}
