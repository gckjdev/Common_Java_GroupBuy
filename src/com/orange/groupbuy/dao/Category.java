package com.orange.groupbuy.dao;

import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class Category extends CommonData {

    public Category(DBObject dbObject) {
        super(dbObject);
    }

    public String getCategoryName() {
        return this.getString(DBConstants.F_CATEGORY_NAME);
    }

    public Object getCategoryId() {
        return this.getString(DBConstants.F_CATEGORY_ID);
    }
    
    

}


