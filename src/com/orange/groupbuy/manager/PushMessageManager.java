package com.orange.groupbuy.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.DateUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.PushMessage;
import com.orange.groupbuy.dao.User;

/**
 * The Class PushMessageManager.
 */
public class PushMessageManager {

    public static final Logger log = Logger.getLogger(PushMessageManager.class.getName());
    private static final int MAX_IPHONE_LEN = 60;


    /**
     * Reset all running message.
     *
     * @param mongoClient the mongo client
     */
    public static void resetAllRunningMessage(final MongoDBClient mongoClient) {
        DBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject();

        BasicDBList values = new BasicDBList();

        BasicDBObject query_trycount = new BasicDBObject();
        query_trycount.put(DBConstants.F_PUSH_MESSAGE_TRYCOUNT, new BasicDBObject("$lt", DBConstants.C_PUSH_MESSAGE_TRY_COUNT_LIMIT));
        query_trycount.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_FAILURE);
        values.add(query_trycount);

        values.add(new BasicDBObject(DBConstants.F_PUSH_MESSAGE_STATUS, null));
        values.add(new BasicDBObject(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_RUNNING));


        query.put("$or", values);

        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING);
        update.put("$set", updateValue);

        mongoClient.updateAll(DBConstants.T_PUSH_MESSAGE, query, update);
    }

    public static PushMessage findMessageForPush(final MongoDBClient mongoClient) {

        //find p_status:null OR failure but try_cnt < limit OR not_running

        DBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject();
        BasicDBList values = new BasicDBList();

        BasicDBObject query_trycount = new BasicDBObject();
        query_trycount.put(DBConstants.F_PUSH_MESSAGE_TRYCOUNT, new BasicDBObject("$lt", DBConstants.C_PUSH_MESSAGE_TRY_COUNT_LIMIT));
        query_trycount.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_FAILURE);
        values.add(query_trycount);
        values.add(new BasicDBObject(DBConstants.F_PUSH_MESSAGE_STATUS, null));
        values.add(new BasicDBObject(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING));

        query.put("$or", values);

        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_RUNNING);
        update.put("$set", updateValue);

        DBObject obj =  mongoClient.findAndModifyNew(DBConstants.T_PUSH_MESSAGE, query, update);


        if (obj != null) {
            PushMessage message =  new PushMessage(obj);
            User user = findAndModifyUserByMessage(mongoClient, message);
            if (UserManager.checkPushCount(mongoClient, user)) {
                return message;
            } else {
                obj.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING);
                mongoClient.save(DBConstants.T_PUSH_MESSAGE, obj);
                log.info("push message exceed daily limit!");
                return null;
            }
        }
        return null;
    }

    public static User findAndModifyUserByMessage(MongoDBClient mongoClient, PushMessage message) {
        String userId = message.getUserId();

        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_USERID, new ObjectId(userId));

        BasicDBObject update = new BasicDBObject();
        BasicDBObject incValue = new BasicDBObject();
        incValue.put(DBConstants.F_PUSH_COUNT, 1);
        update.put("$inc", incValue);

        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_PUSH_DATE, DateUtil.getGMT8Date());
        update.put("$set", updateValue);

        DBObject obj = mongoClient.findAndModifyNew(DBConstants.T_USER, query, update);

        if (obj != null) {
            return new User(obj);
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
        pushMessage.put(DBConstants.F_PUSH_MESSAGE_TRYCOUNT, pushMessage.getTryCount() + 1);
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, pushMessage.getDbObject());
    }
}
