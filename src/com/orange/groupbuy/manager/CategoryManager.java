package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Category;

public class CategoryManager extends CommonManager {

    public static List<Category> findAllCategory(MongoDBClient mongoClient) {
        
        DBCursor cursor = mongoClient.findAll(DBConstants.T_CATEGORY);
        if (cursor == null)
            return null;
        
        List<Category> categoryList = new ArrayList<Category>();
        Iterator<?> iter = cursor.iterator();
        while (iter.hasNext()){
            BasicDBObject obj = (BasicDBObject)iter.next();
            Category category = new Category(obj);
            categoryList.add(category);
        }
        
        return categoryList;
    }

}
