package com.orange.groupbuy.manager;

import java.util.Date;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.PushMessage;
import com.orange.groupbuy.dao.User;

/**
 * The Class PushMessageManager.
 */
public class PushMessageManager {

    public static final Logger log = Logger.getLogger(PushMessageManager.class.getName());
    
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
        DBObject obj = mongoClient.findAndModifyUpsert(DBConstants.T_PUSH_MESSAGE,
                                                 DBConstants.F_PUSH_MESSAGE_STATUS,
                                                 DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING,
                                                 DBConstants.C_PUSH_MESSAGE_STATUS_RUNNING);
        if (obj != null) {
            return new PushMessage(obj);
        }
        return null;
    }

    public static void savePushMessage(final MongoDBClient mongoClient, Product product, User user) {

        String userId = user.getUserId();
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_PRODUCTID, product.getId());

//        obj.put(DBConstants.F_PUSH_MESSAGE_SUBJECT, product.getTitle());
//        obj.put(DBConstants.F_PUSH_MESSAGE_BODY, builder.toString());

        String iPhoneMessage = buildMessageForIPhone(product, user);

        BasicDBObject obj = new BasicDBObject();
        obj.put(DBConstants.F_DEVICETOKEN, user.getDeviceToken());
        obj.put(DBConstants.F_PRODUCTID, product.getId());
        obj.put(DBConstants.F_FOREIGN_USER_ID, userId);
        obj.put(DBConstants.F_PUSH_MESSAGE_IPHONE, iPhoneMessage);
        obj.put(DBConstants.F_PUSH_MESSAGE_TYPE, DBConstants.C_PUSH_TYPE_IPHONE);
        obj.put(DBConstants.F_START_DATE, new Date());

        BasicDBObject update = new BasicDBObject();
        update.put("$set", obj);

        log.debug("update push, query=" + query.toString() + ", value=" + update.toString());

        mongoClient.updateOrInsert(DBConstants.T_PUSH_MESSAGE, query, update);
    }

    private static final int MAX_IPHONE_LEN = 80;

    private static String buildMessageForIPhone(Product product, User user) {
        StringBuilder builder = new StringBuilder();
        builder.append("有满足您需求的团购推荐了！【").append(product.getSiteName()).append("】 ").
                append(product.getTitle());

        String message = builder.toString();
        int len = message.length();
        if (len > MAX_IPHONE_LEN) {
            len = MAX_IPHONE_LEN;
            return message.substring(0, len).concat("...");
        }
        else {
            return message;
        }

    }

    /**
     * Push close.
     *
     * @param mongoClient the mongo client
     * @param pushMessage the push message
     */
    public static void pushMessageClose(final MongoDBClient mongoClient, final PushMessage pushMessage) {
        pushMessage.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_CLOSE);
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, pushMessage.getDbObject());   
    }
    
    public static void pushMessageFailure(final MongoDBClient mongoClient, final PushMessage pushMessage) {
        pushMessage.put(DBConstants.F_PUSH_MESSAGE_STATUS, Integer.valueOf(DBConstants.C_PUSH_MESSAGE_STATUS_FAILURE));
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, pushMessage.getDbObject());   
    }
}
