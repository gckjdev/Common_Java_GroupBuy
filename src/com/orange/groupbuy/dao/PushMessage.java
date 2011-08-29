package com.orange.groupbuy.dao;

import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;

/**
 * The Class PushMessage.
 */
public class PushMessage extends CommonData {

    /**
     * Instantiates a new push message.
     *
     * @param dbObject the db object
     */
    public PushMessage(final DBObject dbObject) {
        super(dbObject);
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public final int getStatus() {
        return this.getInt(DBConstants.F_PUSH_MESSAGE_STATUS);
    }

    /**
     * Gets the device token.
     *
     * @return the device token
     */
    public final String getDeviceToken() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_DEVICETOKEN);
    }

    /**
     * Gets the device os.
     *
     * @return the device os
     */
    public final String getDeviceOS() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_DEVICEOS);
    }

    /**
     * Gets the push type.
     *
     * @return the push type
     */
    public final String getPushType() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_TYPE);
    }

    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public final String getErrorCode() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_ERROR_CODE);
    }

    /**
     * Gets the push subject.
     *
     * @return the push subject
     */
    public final String getPushSubject() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_SUBJECT);
    }

    /**
     * Gets the push body.
     *
     * @return the push body
     */
    public final String getPushBody() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_BODY);
    }

    /**
     * Gets the push iphone.
     *
     * @return the push iphone
     */
    public final String getPushIphone() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_IPHONE);
    }

    /**
     * Gets the push android.
     *
     * @return the push android
     */
    public final String getPushAndroid() {
        return this.getString(DBConstants.F_PUSH_MESSAGE_ANDROID);
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public final String getUserId() {
        return this.getString(DBConstants.F_FOREIGN_USER_ID);
    }

    public String getProductId() {
        return this.getString(DBConstants.F_PRODUCTID);
    }

}
