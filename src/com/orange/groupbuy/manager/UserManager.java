package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.urbanairship.RegisterService;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.PushNotificationConstants;
import com.orange.groupbuy.dao.Gps;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.RecommendItem;
import com.orange.groupbuy.dao.User;

public class UserManager extends CommonManager {

    public static final Logger log = Logger.getLogger(UserManager.class.getName());

    public static DBObject findUserByDeviceId(MongoDBClient mongoClient, String deviceId) {
        if (mongoClient == null || deviceId == null || deviceId.length() <= 0)
            return null;

        DBObject obj = mongoClient.findOne(DBConstants.T_USER, DBConstants.F_DEVICEID, deviceId);
        return obj;
    }

    public static DBObject findUserByEmail(MongoDBClient mongoClient, String email) {
        if (mongoClient == null || email == null || email.length() <= 0)
            return null;

        return mongoClient.findOne(DBConstants.T_USER, DBConstants.F_EMAIL, email);

    }

    public static DBObject findUserByVerifyCode(MongoDBClient mongoClient, String vcd) {
        if (mongoClient == null || vcd == null || vcd.length() <= 0)
            return null;

        return mongoClient.findOne(DBConstants.T_USER, DBConstants.F_VERIFYCODE, vcd);

    }

    public static void updateStatusByVerifyCode(MongoDBClient mongoClient, String sta, String vcd) {
        if (mongoClient == null || sta == null || sta.length() <= 0 || vcd == null || vcd.length() <= 0)
            return;

        Map<String, Object> equalCondition = new HashMap<String, Object>();
        Map<String, Object> updateMap = new HashMap<String, Object>();
        equalCondition.put(DBConstants.F_VERIFYCODE, vcd);
        updateMap.put(DBConstants.F_STATUS, sta);

        mongoClient.findAndModify(DBConstants.T_USER, equalCondition, updateMap);
    }

    public static void updateEmail(MongoDBClient mongoClient, String email, String new_email) {
        if (mongoClient == null || email == null || email.length() <= 0 || new_email == null || new_email.length() <= 0)
            return;

        mongoClient.findAndModify(DBConstants.T_USER, DBConstants.F_EMAIL, email, new_email);
    }

    public static void updatePassword(MongoDBClient mongoClient, String mail, String new_pwd) {
        if (mongoClient == null || mail == null || mail.length() <= 0 || new_pwd == null || new_pwd.length() <= 0)
            return;

        Map<String, Object> equalCondition = new HashMap<String, Object>();
        Map<String, Object> updateMap = new HashMap<String, Object>();
        equalCondition.put(DBConstants.F_EMAIL, mail);
        updateMap.put(DBConstants.F_PASSWORD, new_pwd);

        mongoClient.findAndModify(DBConstants.T_USER, equalCondition, updateMap);

    }

    public static BasicDBObject createDeviceUser(MongoDBClient mongoClient, String appId, String deviceModel,
            String deviceId, String deviceOS, String deviceToken, String language, String countryCode) {

        BasicDBObject user = new BasicDBObject();
        user.put(DBConstants.F_APPID, appId);
        user.put(DBConstants.F_DEVICEMODEL, deviceModel);
        user.put(DBConstants.F_DEVICEID, deviceId);
        user.put(DBConstants.F_DEVICEOS, deviceOS);
        user.put(DBConstants.F_DEVICETOKEN, deviceToken);
        user.put(DBConstants.F_LANGUAGE, language);
        user.put(DBConstants.F_COUNTRYCODE, countryCode);
        user.put(DBConstants.F_CREATE_DATE, new Date()); // DateUtil.currentDate());
        user.put(DBConstants.F_CREATE_SOURCE_ID, appId);
        user.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);

        boolean result = mongoClient.insert(DBConstants.T_USER, user);
        if (result)
            return user;
        else
            return null;
    }

    public static BasicDBObject createUserByEmail(MongoDBClient mongoClient, String appId, String email,
            String password, boolean isVerification) {

        BasicDBObject user = new BasicDBObject();
        user.put(DBConstants.F_APPID, appId);
        user.put(DBConstants.F_CREATE_SOURCE_ID, appId);
        user.put(DBConstants.F_EMAIL, email);
        user.put(DBConstants.F_PASSWORD, password);
        user.put(DBConstants.F_VERIFYCODE, StringUtil.randomUUID());
        user.put(DBConstants.F_CREATE_DATE, new Date()); // DateUtil.currentDate());
        if (isVerification)
            user.put(DBConstants.F_STATUS, DBConstants.STATUS_TO_VERIFY);
        else
            user.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);

        boolean result = mongoClient.insert(DBConstants.T_USER, user);
        if (result)
            return user;
        else
            return null;
    }

    public static void addSearchKeyword(MongoDBClient mongoClient, String deviceId, String keywords) {
        addSearchKeyword(mongoClient, deviceId, keywords, false, 0.0f, 0.0f);
    }

    public static void addSearchKeyword(MongoDBClient mongoClient, String deviceId, String keywords, double longitude,
            double latitude) {
        addSearchKeyword(mongoClient, deviceId, keywords, true, longitude, latitude);
    }

    public static void addSearchKeyword(MongoDBClient mongoClient, String deviceId, String keyword,
            boolean hasLocation, double longitude, double latitude) {

        if (deviceId == null || keyword == null || keyword.isEmpty())
            return;

        // user found, add keywords into user history
        BasicDBObject value = new BasicDBObject();
        value.put(DBConstants.F_SEARCH_HISTORY, keyword);

        DBObject addToSet = new BasicDBObject();
        addToSet.put("$addToSet", value);

        DBObject query = new BasicDBObject();
        query.put(DBConstants.F_DEVICEID, deviceId);

        log.info("<addSearchKeyword> query=" + query.toString() + ",value=" + addToSet);
        mongoClient.updateAll(DBConstants.T_USER, query, addToSet);

    }

    public static void addSearchKeywordDetailRecord(MongoDBClient mongoClient, String deviceId, String[] keywords,
            boolean hasLocation, double longitude, double latitude) {

        if (deviceId == null || keywords == null || keywords.length == 0)
            return;

        // user found, add keywords into user history
        BasicDBObject searchRecord = new BasicDBObject();

        // set date
        searchRecord.put(DBConstants.F_DATE, new Date());

        // set keywords
        BasicDBList keywordList = new BasicDBList();
        for (int i = 0; i < keywords.length; i++) {
            keywordList.add(keywords[i]);
        }
        searchRecord.put(DBConstants.F_KEYWORD, keywordList);

        if (hasLocation) {
            Gps gps = new Gps(latitude, longitude);
            searchRecord.put(DBConstants.F_GPS, gps.toDoubleList());
        }

        DBObject value = new BasicDBObject();
        value.put(DBConstants.F_SEARCH_HISTORY, searchRecord);

        DBObject pushValue = new BasicDBObject();
        pushValue.put("$push", value);

        DBObject query = new BasicDBObject();
        query.put(DBConstants.F_DEVICEID, deviceId);

        System.out.println("<addSearchKeyword> query=" + query.toString() + ",value=" + pushValue);
        mongoClient.updateAll(DBConstants.T_USER, query, pushValue);
    }

    public static User findUserByUserId(MongoDBClient mongoClient, String userId) {
        if (mongoClient == null || userId == null || userId.length() <= 0)
            return null;

        DBObject obj = mongoClient.findOne(DBConstants.T_USER, DBConstants.F_USERID, new ObjectId(userId));
        if (obj == null)
            return null;

        return new User(obj);
    }

    public static boolean save(MongoDBClient mongoClient, User user) {
        mongoClient.save(DBConstants.T_USER, user.getDbObject());
        return true;
    }

    private static BasicDBObject createItemForAdd(String itemId, String categoryName, String subCategoryName,
            String keywords, String city, double maxPrice, double minRebate, Date expireDate) {

        if (StringUtil.isEmpty(itemId))
            return null;

        BasicDBObject item = new BasicDBObject();
        item.put(DBConstants.F_ITEM_ID, itemId);
        item.put(DBConstants.F_CREATE_DATE, new Date());

        if (!StringUtil.isEmpty(categoryName))
            item.put(DBConstants.F_CATEGORY_NAME, categoryName);
        if (!StringUtil.isEmpty(subCategoryName))
            item.put(DBConstants.F_SUB_CATEGORY_NAME, subCategoryName);
        if (!StringUtil.isEmpty(keywords))
            item.put(DBConstants.F_KEYWORD, keywords);
        if (!StringUtil.isEmpty(city))
            item.put(DBConstants.F_CITY, city);
        if (maxPrice >= 0.0f)
            item.put(DBConstants.F_MAX_PRICE, maxPrice);
        if (minRebate >= 0.0f)
            item.put(DBConstants.F_MIN_REBATE, minRebate);

        item.put(DBConstants.F_EXPIRE_DATE, expireDate);

        return item;
    }

    private static BasicDBObject createItemForUpdate(String itemId, String categoryName, String subCategoryName,
            String keywords, String city, double maxPrice, double minRebate, Date expireDate) {

        if (StringUtil.isEmpty(itemId))
            return null;

        // key should like "xxx.$.xxx" for array update
        String prefix = DBConstants.F_SHOPPING_LIST.concat(".$.");

        BasicDBObject item = new BasicDBObject();

        item.put(prefix.concat(DBConstants.F_CATEGORY_NAME), categoryName);
        item.put(prefix.concat(DBConstants.F_SUB_CATEGORY_NAME), subCategoryName);
        item.put(prefix.concat(DBConstants.F_KEYWORD), keywords);
        item.put(prefix.concat(DBConstants.F_CITY), city);
        item.put(prefix.concat(DBConstants.F_EXPIRE_DATE), expireDate);

        if (maxPrice >= 0.0f)
            item.put(prefix.concat(DBConstants.F_MAX_PRICE), maxPrice);
        else
            item.put(prefix.concat(DBConstants.F_MAX_PRICE), null);

        if (minRebate >= 0.0f)
            item.put(prefix.concat(DBConstants.F_MIN_REBATE), minRebate);
        else
            item.put(prefix.concat(DBConstants.F_MIN_REBATE), null);

        return item;
    }

    public static boolean addUserShoppingItem(MongoDBClient mongoClient, String userId, String itemId,
            String categoryName, String subCategoryName, String keywords, String city, double maxPrice,
            double minRebate, Date expireDate) {

        BasicDBObject item = createItemForAdd(itemId, categoryName, subCategoryName, keywords, city, maxPrice,
                minRebate, expireDate);
        if (item == null)
            return false;

        BasicDBObject query = new BasicDBObject();
        ObjectId id = new ObjectId(userId);
        query.put(DBConstants.F_USERID, id);

        BasicDBObject pushValue = new BasicDBObject();
        pushValue.put(DBConstants.F_SHOPPING_LIST, item);

        BasicDBObject update = new BasicDBObject();
        update.put("$push", pushValue);

        mongoClient.updateAll(DBConstants.T_USER, query, update);
        return true;
    }

    private static String getItemArrayKey() {
        return DBConstants.F_SHOPPING_LIST.concat(".").concat(DBConstants.F_ITEM_ID);
    }

    public static boolean updateUserShoppingItem(MongoDBClient mongoClient, String userId, String itemId,
            String categoryName, String subCategoryName, String keywords, String city, double maxPrice,
            double minRebate, Date expireDate) {

        BasicDBObject item = createItemForUpdate(itemId, categoryName, subCategoryName, keywords, city, maxPrice,
                minRebate, expireDate);
        if (item == null)
            return false;

        BasicDBObject query = new BasicDBObject();
        ObjectId id = new ObjectId(userId);
        query.put(DBConstants.F_USERID, id);
        String queryKeyForItem = getItemArrayKey();
        query.put(queryKeyForItem, itemId);

        BasicDBObject update = new BasicDBObject();
        update.put("$set", item);

        mongoClient.updateAll(DBConstants.T_USER, query, update);
        
        //delete joint recommend productlist
        RecommendItemManager.deleteRecommendProductList(mongoClient, userId, itemId);
        return true;
    }

    public static boolean deleteUserShoppingItem(MongoDBClient mongoClient, String userId, String itemId) {

        if (itemId == null || userId == null)
            return false;

        BasicDBObject query = new BasicDBObject();
        ObjectId id = new ObjectId(userId);
        query.put(DBConstants.F_USERID, id);
        
        mongoClient.pullArrayKey(DBConstants.T_USER, query, DBConstants.F_SHOPPING_LIST, DBConstants.F_ITEM_ID, itemId);
        
      //delete joint recommend item
        RecommendItemManager.deleteRecommendItem(mongoClient, userId, itemId);

        return true;
    }

    public static BasicDBObject findUserShoppingItem(MongoDBClient mongoClient, User user, String itemId) {
        if (itemId == null || user == null)
            return null;

        BasicDBList shoppingList = user.getShoppingItem();

        Iterator<Object> iter = shoppingList.iterator();

        while (iter.hasNext()) {
            BasicDBObject obj = (BasicDBObject) iter.next();
            if (obj != null) {
                String item_id = obj.getString(DBConstants.F_ITEM_ID);
                if (item_id.equals(itemId)) {
                    return obj;
                }
            }
        }
        return null;
    }

    public static Map<String, Integer> getUserRecommendItemCountMap(MongoDBClient mongoClient, String userId,
            String[] itemIdArray) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        BasicDBList shoppingList = new BasicDBList();

        User user = findUserByUserId(mongoClient, userId);

        if (user == null) {
            return null;
        }
        
        if (itemIdArray == null || itemIdArray.length == 0) {
            shoppingList = user.getShoppingItem();
            if (shoppingList == null || shoppingList.size() == 0) {
                return null;
            }
            Iterator<Object> iter = shoppingList.iterator();
            while (iter.hasNext()) {
                BasicDBObject obj = (BasicDBObject) iter.next();
                if (obj != null) {
                    String itemId = obj.getString(DBConstants.F_ITEM_ID);
                    RecommendItem item = RecommendItemManager.findRecommendItem(mongoClient, userId, itemId);
                    if (item != null) {
                        int count = item.getRecommendCount();
                        map.put(itemId, count);
                    }
                }
            }

        } 
        else {
            for (int i = 0; i < itemIdArray.length; i++) {
                RecommendItem item = RecommendItemManager.findRecommendItem(mongoClient, userId, itemIdArray[i]);
                if (item != null) {
                    int count = item.getRecommendCount();
                    map.put(itemIdArray[i], count);
                }
            }
        }

        return map;
    }

    public static void registerUserDeviceToken(String userId, String deviceToken) {
        RegisterService registerService = RegisterService.createService(PushNotificationConstants.APPLICATION_KEY,
                PushNotificationConstants.APPLICATION_SECRET, PushNotificationConstants.APPLICATION_MASTER_SECRET,
                userId, deviceToken);
        registerService.handleServiceRequest();
    }

    public static User findUserForRecommend(final MongoDBClient mongoClient) {

        DBObject obj = mongoClient.findAndModifyUpsert(DBConstants.T_USER, DBConstants.F_RECOMMEND_STATUS,
                DBConstants.C_RECOMMEND_STATUS_NOT_RUNNING, DBConstants.C_RECOMMEND_STATUS_RUNNING);
        if (obj != null) {
            return new User(obj);
        }

        return null;

    }

    public static void addPushCount(User user) {
        user.setPushCount(user.getPushCount() + 1);
    }

    public static boolean checkPushCount(MongoDBClient mongoClient, User user) {

        TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(new Date());
        Calendar lastPushCalendar = Calendar.getInstance(timeZone);
        Date lastPushDate = user.getPushDate();

        if (lastPushDate == null) {
            return true;
        }

        lastPushCalendar.setTime(lastPushDate);

        int pushCount = user.getPushCount();

        if (pushCount > DBConstants.C_PUSH_DAILY_LIMIT) {
            if (now.get(Calendar.DAY_OF_MONTH) > lastPushCalendar.get(Calendar.DAY_OF_MONTH)) {
                user.setPushCount(0);
                mongoClient.save(DBConstants.T_USER, user.getDbObject());
            } else {
                user.setPushCount(pushCount - 1);
                mongoClient.save(DBConstants.T_USER, user.getDbObject());
                return false;
            }
        }
        return true;
    }

    public static void recommendClose(final MongoDBClient mongoClient, final User user) {
        user.put(DBConstants.F_RECOMMEND_STATUS, DBConstants.C_RECOMMEND_STATUS_COLSE);
        mongoClient.save(DBConstants.T_USER, user.getDbObject());
    }

    public static void recommendFailure(final MongoDBClient mongoClient, final User user) {
        user.put(DBConstants.F_RECOMMEND_STATUS, Integer.valueOf(DBConstants.C_RECOMMEND_STATUS_FAILURE));
        mongoClient.save(DBConstants.T_USER, user.getDbObject());
    }

    public static void setRecommendItem(final MongoDBClient mongoClient, User user, Product product, String itemId) {

        float score = product.getScore();

        if (score >= DBConstants.MIN_SCORE_TO_RECOMMEND) {
            BasicDBObject item = new BasicDBObject();
            item.put(DBConstants.F_PRODUCTID, product.getStringObjectId());
            item.put(DBConstants.F_SCORE, product.getScore());
            item.put(DBConstants.F_START_DATE, product.getStartDate());
            item.put(DBConstants.F_END_DATE, product.getStartDate());

            BasicDBObject query = new BasicDBObject();
            query.put(DBConstants.F_FOREIGN_USER_ID, user.getStringObjectId());
            query.put(DBConstants.F_ITEM_ID, itemId);

            String productIdKey = DBConstants.F_RECOMMEND_LIST.concat(".").concat(DBConstants.F_PRODUCTID);
            query.put(productIdKey, product.getId());

            BasicDBObject pushValue = new BasicDBObject();
            pushValue.put(DBConstants.F_RECOMMEND_LIST, item);

            BasicDBObject update = new BasicDBObject();
            update.put("$addToSet", pushValue);

            mongoClient.upsertAll(DBConstants.T_RECOMMEND, query, update);
        }
    }

    public static void resetAllRunningMessage(final MongoDBClient mongoClient) {
        BasicDBObject query = new BasicDBObject();
        BasicDBObject update = new BasicDBObject();

        BasicDBObject value = new BasicDBObject();
        value.put("$ne", DBConstants.C_RECOMMEND_STATUS_NOT_RUNNING);
        query.put(DBConstants.F_RECOMMEND_STATUS, value);

        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_RECOMMEND_STATUS, DBConstants.C_RECOMMEND_STATUS_NOT_RUNNING);
        update.put("$set", updateValue);

        mongoClient.updateAll(DBConstants.T_USER, query, update);

    }
}
