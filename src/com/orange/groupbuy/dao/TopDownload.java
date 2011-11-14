package com.orange.groupbuy.dao;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class TopDownload extends CommonData {
    public TopDownload(DBObject dbObject) {
        super(dbObject);
    }

    public TopDownload(String appId, String deviceId, String countryCode, String language, String fileType,
            String fileURL, int fileSize, String fileName, String siteURL, String siteName) {
        
        BasicDBObject dbObject = new BasicDBObject();
        
        dbObject.put(DBConstants.F_APPID, appId);
        dbObject.put(DBConstants.F_DEVICEID, deviceId);
        dbObject.put(DBConstants.F_LANGUAGE, language);
        dbObject.put(DBConstants.F_COUNTRYCODE, countryCode);
        dbObject.put(DBConstants.F_CREATE_DATE, new Date());
        dbObject.put(DBConstants.F_CREATE_SOURCE_ID, appId);
        
        dbObject.put(DBConstants.F_FILE_TYPE, fileType);
        dbObject.put(DBConstants.F_FILE_URL, fileURL);
        dbObject.put(DBConstants.F_FILE_SIZE, fileSize);
        dbObject.put(DBConstants.F_FILE_NAME, fileName);
        dbObject.put(DBConstants.F_SITE_URL, siteURL);
        dbObject.put(DBConstants.F_SITE_NAME, siteName);    

        dbObject.put(DBConstants.F_DOWNLOAD_COUNT, 1);            
        this.dbObject = dbObject;
    }

    public void incDownloadCount() {
        int count = this.getDownloadCount();
        setDownloadCount(count + 1);
    }

    private void setDownloadCount(int i) {
        this.put(DBConstants.F_DOWNLOAD_COUNT, i);
    }

    private int getDownloadCount() {
        return this.getInt(DBConstants.F_DOWNLOAD_COUNT);
    }

    public void updateModifyDate() {
        this.put(DBConstants.F_MODIFY_DATE, new Date());
    }
}
