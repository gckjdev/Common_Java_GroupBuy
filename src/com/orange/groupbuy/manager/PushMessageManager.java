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
import com.orange.groupbuy.dao.RecommendItem;
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

        BasicDBObject pushQuery = new BasicDBObject();
        pushQuery.put(DBConstants.F_PUSH_MESSAGE_TRYCOUNT, new BasicDBObject("$lt", DBConstants.C_PUSH_MESSAGE_TRY_COUNT_LIMIT));
        pushQuery.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_FAILURE);
        values.add(pushQuery);
        values.add(new BasicDBObject(DBConstants.F_PUSH_MESSAGE_STATUS, null));
        values.add(new BasicDBObject(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_NOT_RUNNING));

        query.put("$or", values);

        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_PUSH_MESSAGE_STATUS, DBConstants.C_PUSH_MESSAGE_STATUS_RUNNING);
        update.put("$set", updateValue);
        
        log.debug("<findMessageForPush> query="+query.toString()+", update="+update.toString());
        DBObject obj =  mongoClient.findAndModifyNew(DBConstants.T_PUSH_MESSAGE, query, update);

        if (obj != null) {
            PushMessage message =  new PushMessage(obj);
            User user = findAndModifyUserByMessage(mongoClient, message);
            if (UserManager.checkPushCount(mongoClient, user)) {
                return message;
            } else {
                int userPushCounter = user.getPushCount();
                failPushMessage(mongoClient, message, DBConstants.C_PUSH_MESSAGE_FAIL_REACH_USER_LIMIT);
                log.info("<findMessageForPush> push message exceed daily limit of user="+user.getUserId() + ", push count = "+userPushCounter);
                return null;
            }
        }
        return null;
    }

    private static void failPushMessage(MongoDBClient mongoClient, PushMessage message, int reason) {
        message.setStatus(DBConstants.C_PUSH_MESSAGE_STATUS_FAILURE);
        message.setReason(reason);
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, message.getDbObject());
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

    public static void savePushMessage(final MongoDBClient mongoClient, Product product, User user, RecommendItem item) {

        saveIphonePushMessage(mongoClient,product,user, item);
        // saveEmailPushMessage(mongoClient,product,user, item);
    }

    private static void saveEmailPushMessage(MongoDBClient mongoClient, Product product, User user, RecommendItem item) {
        
        int titlelen = 60;
        String userId = user.getUserId();
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_PRODUCTID, product.getId());
        
        String emailMessage = buildMessageForEmail(product,user);
        String emailTitle = emailMessage.substring(0, titlelen)+"...";
        BasicDBObject obj = new BasicDBObject();
        obj.put(DBConstants.F_PRODUCTID, product.getId());
        obj.put(DBConstants.F_FOREIGN_USER_ID, userId);
        obj.put(DBConstants.F_PUSH_MESSAGE_SUBJECT, emailTitle);
        obj.put(DBConstants.F_PUSH_MESSAGE_BODY, emailMessage);
        obj.put(DBConstants.F_PUSH_MESSAGE_IMAGE, product.getImage());
        
        obj.put(DBConstants.F_ITEM_ID, item.getItemId());
        obj.put(DBConstants.F_PUSH_MESSAGE_TYPE, DBConstants.C_PUSH_TYPE_EMAIL);
        obj.put(DBConstants.F_START_DATE, new Date());

        BasicDBObject update = new BasicDBObject();
        update.put("$set", obj);

        log.debug("update push, query=" + query.toString() + ", value=" + update.toString());

        mongoClient.updateOrInsert(DBConstants.T_PUSH_MESSAGE, query, update);
        // TODO Auto-generated method stub
        
    }


    public static void saveIphonePushMessage(final MongoDBClient mongoClient, Product product, User user, RecommendItem item) {

        String userId = user.getUserId();
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_PRODUCTID, product.getId());

//        obj.put(DBConstants.F_PUSH_MESSAGE_SUBJECT, product.getTitle());
//        obj.put(DBConstants.F_PUSH_MESSAGE_BODY, builder.toString());

        String iPhoneMessage = buildMessageForIPhone(product, user);
        //String emailMessage = buildMessageForEmail(product,user);
        //String androidMessage = buildMessageForAndroid(product,user);
        //String weiboMessage = buildMessageForWeibo(product,user);

        BasicDBObject obj = new BasicDBObject();
        obj.put(DBConstants.F_DEVICETOKEN, user.getDeviceToken());
        obj.put(DBConstants.F_PRODUCTID, product.getId());
        obj.put(DBConstants.F_FOREIGN_USER_ID, userId);
        obj.put(DBConstants.F_PUSH_MESSAGE_IPHONE, iPhoneMessage);
        //obj.put(DBConstants.F_PUSH_MESSAGE_ANDROID, androidMessage);
        //obj.put(DBConstants.F_PUSH_MESSAGE_EMAIL, emailMessage);
        //obj.put(DBConstants.F_PUSH_MESSAGE_WEIBO, weiboMessage);
        
        obj.put(DBConstants.F_PUSH_MESSAGE_TYPE, DBConstants.C_PUSH_TYPE_IPHONE);
        obj.put(DBConstants.F_START_DATE, new Date());
        obj.put(DBConstants.F_ITEM_ID, item.getItemId());

        BasicDBObject update = new BasicDBObject();
        update.put("$set", obj);

        log.debug("update push, query=" + query.toString() + ", value=" + update.toString());

        mongoClient.updateOrInsert(DBConstants.T_PUSH_MESSAGE, query, update);
    }
    private static String buildMessageForWeibo(Product product, User user) {
        
        // TODO Auto-generated method stub
        return null;
    }


    private static String buildMessageForAndroid(Product product, User user) {
        // TODO Auto-generated method stub
        return null;
    }


    private static String buildMessageForEmail(Product product, User user) {
        
        StringBuilder builder = new StringBuilder();
        String imageUrl = product.getImage();
        String loc = product.getLoc();
        builder.append("【").append(product.getSiteName()).append("】 ").
                append(product.getTitle());
        
        String contactUrl = "<br> 点击了解详细内容：<br><a href='"    
                + loc + "'>"+ loc + "</a><br>" ;
        
        
        String image = "<br><img src="+imageUrl+"width=\"60\" height=\"45\" border=\"0\">";
        String message = builder.toString();
        String   html   = 
                " <IMG   SRC="+imageUrl+"   width=80%   height=60%> <br> "+ 
                " <b>   end   of   jpg </b> ";

            return message+contactUrl+imageUrl;
// TODO Auto-generated method stub
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
    public static void pushMessageClose(final MongoDBClient mongoClient, final PushMessage pushMessage, int reason) {        
        pushMessage.setReason(reason);
        pushMessage.setStatus(DBConstants.C_PUSH_MESSAGE_STATUS_CLOSE);
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, pushMessage.getDbObject());
    }

    public static void pushMessageFailure(final MongoDBClient mongoClient, final PushMessage pushMessage, int reason) {
        pushMessage.incTryCount();
        pushMessage.setStatus(DBConstants.C_PUSH_MESSAGE_STATUS_FAILURE);
        pushMessage.setReason(reason);
        mongoClient.save(DBConstants.T_PUSH_MESSAGE, pushMessage.getDbObject());

        String userId = pushMessage.getUserId();
        mongoClient.inc(DBConstants.T_USER, DBConstants.F_USERID, new ObjectId(userId), DBConstants.F_PUSH_COUNT, -1);
    }

    public static User findUserByMessage(final MongoDBClient mongoClient, final PushMessage pushMessage) {
        String userId = pushMessage.getUserId();
        DBObject obj = mongoClient.findOne(DBConstants.T_USER, DBConstants.F_USERID, new ObjectId(userId));
        if (obj != null) {
            return new User(obj);
        }
        return null;
    }
}
