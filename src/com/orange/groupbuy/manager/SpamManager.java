package com.orange.groupbuy.manager;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.SpamNumber;

public class SpamManager extends CommonManager {

    public static void reportSpam(MongoDBClient mongoClient, String mobile, String deviceId, String type){
        SpamNumber item = new SpamNumber(mobile, deviceId, type);
        mongoClient.insert(DBConstants.T_SPAM_NUMBER, item.getDbObject());
    }
}
