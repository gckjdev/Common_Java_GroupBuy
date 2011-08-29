package com.orange.groupbuy.manager;

import java.util.Iterator;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.RecommendItem;

public class RecommendItemManager {

    public static RecommendItem findRecommendItem(MongoDBClient mongoClient, String userId, String itemId) {
        
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_ITEM_ID, itemId);
       
        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_FOREIGN_USER_ID, userId);
        updateValue.put(DBConstants.F_ITEM_ID, itemId);

        BasicDBObject update = new BasicDBObject();
        update.put("$set", updateValue);

        DBObject obj = mongoClient.findAndModifyInsert(DBConstants.T_RECOMMEND, query, update);
        if (obj == null)
            return null;
        else
            return new RecommendItem(obj);
    }

    public static boolean addOrUpdateProduct( RecommendItem recommendItem, Product product) {
        
        if (product.getScore() < DBConstants.MIN_SCORE_TO_RECOMMEND)
            return false;
        
        BasicDBList existProductList = recommendItem.getProductList();
        if (existProductList == null){
            existProductList = new BasicDBList();
            recommendItem.put(DBConstants.F_RECOMMENDLIST, existProductList);
        }
        
        boolean found = false;
        String productId = product.getId();
        Iterator iter = existProductList.iterator();
        while (iter.hasNext()){
            BasicDBObject recommendProduct = (BasicDBObject)iter.next();
            String recommendProductId = recommendProduct.getString(DBConstants.F_PRODUCTID);
            if (recommendProductId.equalsIgnoreCase(productId)){
                recommendProduct.put(DBConstants.F_SCORE, product.getScore());
                found = true;
                break;
            }
            
        }
        
        if (!found){
           addRecommendProduct(existProductList, product);
        }
        
        return true;
    }

    private static void addRecommendProduct(BasicDBList existProductList, Product product) {
        BasicDBObject item = new BasicDBObject();
        item.put(DBConstants.F_PRODUCTID, product.getStringObjectId());
        item.put(DBConstants.F_SCORE, product.getScore());
        item.put(DBConstants.F_START_DATE, product.getStartDate());
        item.put(DBConstants.F_END_DATE, product.getStartDate());
        item.put(DBConstants.F_ITEM_SENT_STATUS, DBConstants.C_ITEM_NOT_SENT);

        existProductList.add(item);
    }

}
