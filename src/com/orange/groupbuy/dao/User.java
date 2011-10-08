package com.orange.groupbuy.dao;

import java.util.Date;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;

public class User extends CommonData {

    public User(DBObject dbObject) {
        super(dbObject);
    }

    public boolean addShoppingItem(String itemId, String categoryName, String subCategoryName, String keywords,
            String city, double maxPrice, double minRebate, Date expireDate) {

        BasicDBObject item = new BasicDBObject();

        if (!StringUtil.isEmpty(itemId)) {
            item.put(DBConstants.F_ITEM_ID, itemId);
        }
        if (!StringUtil.isEmpty(categoryName)) {
            item.put(DBConstants.F_CATEGORY_NAME, categoryName);
        }
        if (!StringUtil.isEmpty(subCategoryName)) {
            item.put(DBConstants.F_SUB_CATEGORY_NAME, subCategoryName);
        }
        if (!StringUtil.isEmpty(keywords)) {
            item.put(DBConstants.F_KEYWORD, keywords);
        }
        if (!StringUtil.isEmpty(city)) {
            item.put(DBConstants.F_CITY, city);
        }
        if (maxPrice >= 0.0f) {
            item.put(DBConstants.F_MAX_PRICE, maxPrice);
        }
        if (minRebate >= 0.0f) {
            item.put(DBConstants.F_MIN_REBATE, minRebate);
        }

        item.put(DBConstants.F_EXPIRE_DATE, new Date());
        item.put(DBConstants.F_CREATE_DATE, new Date());

        BasicDBList shoppingList = (BasicDBList) dbObject.get(DBConstants.F_SHOPPING_LIST);
        if (shoppingList == null) {
            shoppingList = new BasicDBList();
            dbObject.put(DBConstants.F_SHOPPING_LIST, shoppingList);
        }

        shoppingList.add(item);
        return true;
    }

    public BasicDBList getShoppingItem() {

        BasicDBList shoppingList = (BasicDBList) dbObject.get(DBConstants.F_SHOPPING_LIST);
        if (shoppingList != null) {
            return shoppingList;
        }
        return null;
    }

    public String getUserId() {
        return getObjectId().toString();
    }

    public String getDeviceToken() {
        return this.getString(DBConstants.F_DEVICETOKEN);
    }

    public void setDeviceToke(String deviceToken) {
        this.getDbObject().put(DBConstants.F_DEVICETOKEN, deviceToken);
    }

    public void setRecommendStatus(int status) {
        this.getDbObject().put(DBConstants.F_RECOMMEND_STATUS, status);
    }

    public Date getPushDate() {
        return getDate(DBConstants.F_PUSH_DATE);
    }

    public void setPushDate(Date date) {
        this.getDbObject().put(DBConstants.F_PUSH_DATE, date);
    }

    public int getPushCount() {
        return getInt(DBConstants.F_PUSH_COUNT);
    }

    public void setPushCount(int count) {
        this.getDbObject().put(DBConstants.F_PUSH_COUNT, count);
    }

    public void setNickName(String nickName) {
        this.put(DBConstants.F_NICKNAME, nickName);
    }

    public void setPassword(String newPassword) {
        this.put(DBConstants.F_PASSWORD, newPassword);
    }

    public String getNickName() {
        return this.getString(DBConstants.F_NICKNAME);
    }

    public String getEmail() {
        return this.getString(DBConstants.F_EMAIL);
    }

    public String getAvatar() {
        return this.getString(DBConstants.F_AVATAR);
    }

    public String getQQAccessToken() {
        return this.getString(DBConstants.F_QQ_ACCESS_TOKEN);
    }

    public String getQQAccessTokenSecret() {
        return this.getString(DBConstants.F_QQ_ACCESS_TOKEN_SECRET);
    }

    public String getSinaAccessToken() {
        return this.getString(DBConstants.F_SINA_ACCESS_TOKEN);
    }

    public String getSinaAccessTokenSecret() {
        return this.getString(DBConstants.F_SINA_ACCESS_TOKEN_SECRET);
    }

    public String getPassword() {
        return this.getString(DBConstants.F_PASSWORD);
    }
    
    public String getSinaID() {
        return this.getString(DBConstants.F_SINAID);
    }

    public String getQQID() {
        return this.getString(DBConstants.F_QQID);
    }

    public void setDeviceId(String deviceId) {
        this.put(DBConstants.F_DEVICEID, deviceId);
    }

    public void setAvatar(String avatarURL) {
        this.put(DBConstants.F_AVATAR, avatarURL);
    }
}
