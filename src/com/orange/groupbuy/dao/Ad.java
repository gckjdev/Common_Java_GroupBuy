package com.orange.groupbuy.dao;

import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class Ad extends CommonData {

    public Ad(DBObject dbObject) {
        super(dbObject);
    }

    public String getAdId() {
        return this.getStringObjectId();
    }
    
    public String getAdText() {
        return this.getString(DBConstants.F_AD_TEXT);
    }

    public String getAdImage() {
        return this.getString(DBConstants.F_AD_IMAGE);
    }

    public String getAdLink() {
        return this.getString(DBConstants.F_AD_LINK);
    }
}
