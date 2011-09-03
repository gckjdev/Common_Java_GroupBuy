package com.orange.groupbuy.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class ShoppingCategory extends CommonData {
    

    
    
    public ShoppingCategory(DBObject dbObject) {
        super(dbObject);
        // TODO Auto-generated constructor stub
    }

    public String getCategoryName() {
        return this.getString(DBConstants.F_CATEGORY_NAME);
    }
    
    public BasicDBList getSubCategoryList() {
        return (BasicDBList)dbObject.get(DBConstants.F_SUB_CATEGORY);
    }
 
    

}
