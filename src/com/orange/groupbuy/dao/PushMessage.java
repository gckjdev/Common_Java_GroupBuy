package com.orange.groupbuy.dao;

import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;

public class PushMessage extends CommonData {


    public PushMessage(final DBObject dbObject) {
        super(dbObject);
    }

    public final int getStatus() {
        return this.getInt(DBConstants.F_PUSH_MESSAGE_STATUS);
    }

    public final String getDeviceToken() {
        return this.getString(DBConstants.F_DEVICETOKEN);
    }

    public final int getPushType() {
        return this.getInt(DBConstants.F_PUSH_MESSAGE_TYPE);
    }


    public final String getErrorCode() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_ERROR_CODE);
    }


    public final String getPushSubject() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_SUBJECT);
    }


    public final String getPushBody() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_BODY);
    }

    public final String getPushIphone() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_IPHONE);
    }


    public final String getPushAndroid() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_ANDROID);
    }

    public final String getUserId() {
        return this.getString(DBConstants.F_FOREIGN_USER_ID);
    }

    public String getProductId() {
        return this.getString(DBConstants.F_PRODUCTID);
    }

    public void incTryCount() {
        int currentTryCount = getTryCount();
        dbObject.put(DBConstants.F_PUSH_MESSAGE_TRYCOUNT, ++currentTryCount);
    }

    public int getTryCount() {
        return this.getInt(DBConstants.F_PUSH_MESSAGE_TRYCOUNT);
    }

    public void setStatus(int status) {
        dbObject.put(DBConstants.F_PUSH_MESSAGE_STATUS, status);
    }

    public void setReason(int reason) {
        dbObject.put(DBConstants.F_PUSH_MESSAGE_REASON, reason);
    }

    public String getItemId() {
        return (String)dbObject.get(DBConstants.F_ITEM_ID);
    }

}
