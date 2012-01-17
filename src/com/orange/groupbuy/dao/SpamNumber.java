package com.orange.groupbuy.dao;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class SpamNumber extends CommonData {
    
        String deviceId;
        String mobile;
        String type;
    
        public SpamNumber(DBObject dbObject) {
            super(dbObject);
        }

        public SpamNumber(String mobile, String deviceId, String type){
            BasicDBObject dbObject = new BasicDBObject();
            
            dbObject.put(DBConstants.F_DEVICEID, deviceId);
            dbObject.put(DBConstants.F_MOBILE, mobile);
            dbObject.put(DBConstants.F_TYPE, type);
            dbObject.put(DBConstants.F_CREATE_DATE, new Date());
            
            this.dbObject = dbObject;
        }
}
