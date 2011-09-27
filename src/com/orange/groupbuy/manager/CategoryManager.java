package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Category;
import com.orange.groupbuy.dao.ShoppingCategory;

public class CategoryManager extends CommonManager {

    public static List<Category> findAllCategory(MongoDBClient mongoClient) {
        
        DBCursor cursor = mongoClient.findAll(DBConstants.T_CATEGORY);
        if (cursor == null)
            return null;
        
        List<Category> categoryList = new ArrayList<Category>();
        Iterator<?> iter = cursor.iterator();
        if( iter == null)
            return null;
        while (iter.hasNext()){
            BasicDBObject obj = (BasicDBObject)iter.next();
            Category category = new Category(obj);
            categoryList.add(category);
        }
        
        cursor.close();
        
        return categoryList;
    }

    public static List<ShoppingCategory> findShoppingCategory(MongoDBClient mongoClient) {
        
        DBCursor cursor = mongoClient.findAll(DBConstants.T_SHOPPING_CATEGORY);
        if (cursor == null)
            return null;
        
        List<ShoppingCategory> shoppingcategoryList = new ArrayList<ShoppingCategory>();
        Iterator<?> iter = cursor.iterator();
        if (iter == null)
            return null;
        
        while (iter.hasNext()){
            BasicDBObject obj = (BasicDBObject)iter.next();
            ShoppingCategory ShoppingCategory = new ShoppingCategory(obj);
            shoppingcategoryList.add(ShoppingCategory);
        }
        
        cursor.close();
        return shoppingcategoryList;
    }

    public static List<ShoppingCategory> findSubShoppingCategory(MongoDBClient mongoClient) {
        
        DBCursor cursor = mongoClient.findAll(DBConstants.T_SHOPPING_CATEGORY);
        if (cursor == null)
            return null;
        
        List<ShoppingCategory> shoppingcategoryList = new ArrayList<ShoppingCategory>();
        Iterator<?> iter = cursor.iterator();
        if (iter == null)
            return null;
        
        while (iter.hasNext()){
            BasicDBObject obj = (BasicDBObject)iter.next();
            ShoppingCategory ShoppingCategory = new ShoppingCategory(obj);
            shoppingcategoryList.add(ShoppingCategory);
        }
        
        cursor.close();
        return shoppingcategoryList;
    }
}
