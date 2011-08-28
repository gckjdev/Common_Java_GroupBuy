package com.orange.groupbuy.dao;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class RecommendItem extends CommonData {
    public RecommendItem(DBObject dbObject) {
        super(dbObject);
    }

    public BasicDBList getProductList() {
        return (BasicDBList)dbObject.get(DBConstants.F_RECOMMENDLIST);
    }

    
}
