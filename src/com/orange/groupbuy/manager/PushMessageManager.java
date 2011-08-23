package com.orange.groupbuy.manager;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.PushMessage;

/**
 * The Class PushMessageManager.
 */
public class PushMessageManager {

    /**
     * Reset all running message.
     *
     * @param mongoClient the mongo client
     */
    public static void resetAllRunningMessage(final MongoDBClient mongoClient) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject();
        
        BasicDBObject value = new BasicDBObject();
        value.put("$ne", DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING);
        query.put(DBConstants.F_PUSH_MESSAGE_STATUS, value);

        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING);
        update.put("$set", updateValue);
        
        mongoClient.updateAll(DBConstants.T_PUSH_MESSAGE, query, update);
    }

    /**
     * Find message for push.
     *
     * @param mongoClient the mongo client
     * @param limit the limit
     * @return the list
     */
    public static PushMessage findMessageForPush(final MongoDBClient mongoClient) {
        DBObject obj = mongoClient.findAndModify(DBConstants.T_PUSH_MESSAGE, 
                                                 DBConstants.F_PUSH_MESSAGE_STATUS,
                                                 DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING,
                                                 DBConstants.C_PUSH_MESSAGE_STATUS_RUNNING);
        if (obj != null) {
            return new PushMessage(obj);
        }
        return null;
    }

    /**
     * Push close.
     *
     * @param mongoClient the mongo client
     * @param pushMessage the push message
     */
    public static void pushMessageClose(final MongoDBClient mongoClient, final PushMessage pushMessage) {
        pushMessage.put(DBConstants.F_TASK_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_CLOSE);
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, pushMessage.getDbObject());   
    }
    
    public static void pushMessageFailure(final MongoDBClient mongoClient, final PushMessage pushMessage) {
        pushMessage.put(DBConstants.F_TASK_STATUS, Integer.valueOf(DBConstants.C_PUSH_MESSAGE_STATUS_FAILURE));
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, pushMessage.getDbObject());   
    }
}
