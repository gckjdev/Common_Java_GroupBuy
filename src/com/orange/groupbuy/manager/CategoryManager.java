package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Category;
import com.orange.groupbuy.dao.ShoppingCategory;

public class CategoryManager extends CommonManager {
        
    public static List<Category> findAllCategoryByType(MongoDBClient mongoClient, int categoryType){
        switch (categoryType){
        case DBConstants.C_CATEGORY_TAOBAO_MIAOSHA:
            return findAllTaobaoMiaoshaCategory(mongoClient);
        case DBConstants.C_CATEGORY_TAOBAO_ZHEKOU:
            return findAllTaobaoZhekouCategory(mongoClient);
        default:
            return findAllCategory(mongoClient);
        }
    }
    
    public static List<Category> findAllCategory(MongoDBClient mongoClient, String tableName) {
        
        DBCursor cursor = mongoClient.findAll(tableName);
        if (cursor == null)
            return Collections.emptyList();
        
        List<Category> categoryList = new ArrayList<Category>();
        Iterator<?> iter = cursor.iterator();
        if( iter == null){
            cursor.close();
            return Collections.emptyList();
        }
        
        while (iter.hasNext()){
            BasicDBObject obj = (BasicDBObject)iter.next();
            Category category = new Category(obj);
            categoryList.add(category);
        }
        
        cursor.close();        
        return categoryList;
    }
    
    public static List<Category> findAllCategory(MongoDBClient mongoClient) {        
        return findAllCategory(mongoClient, DBConstants.T_CATEGORY);
    }
    
    public static List<Category> findAllTaobaoMiaoshaCategory(MongoDBClient mongoClient) {
        return findAllCategory(mongoClient, DBConstants.T_TAOBAO_MIAOSHA_CATEGORY);        
    }

    public static List<Category> findAllTaobaoZhekouCategory(MongoDBClient mongoClient) {
        return findAllCategory(mongoClient, DBConstants.T_TAOBAO_ZHEKOU_CATEGORY);        
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
