package com.orange.groupbuy.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;

public class ProductManager {
	
	public static boolean isProductExist(MongoDBClient mongoClient, String productURL, String city){
		Map<String, String> fieldValues = new HashMap<String, String>();
		fieldValues.put(DBConstants.F_LOC, productURL);
		fieldValues.put(DBConstants.F_CITY, city);
		return (mongoClient.findOne(DBConstants.T_PRODUCT, fieldValues) != null);
	}

	public static boolean createProduct(MongoDBClient mongoClient, Product product){
		
//		String loc = product.getLoc();
//		String city = product.getCity();
//		if (isProductExist(mongoClient, loc, city))
//			return false;
		product.calculateRebate();		
		return mongoClient.insert(DBConstants.T_PRODUCT, product.getDbObject());
	}

	public static Product findProduct(MongoDBClient mongoClient, String productURL, String city) {
		Map<String, String> fieldValues = new HashMap<String, String>();
		fieldValues.put(DBConstants.F_LOC, productURL);
		fieldValues.put(DBConstants.F_CITY, city);
		DBObject obj = mongoClient.findOne(DBConstants.T_PRODUCT, fieldValues);
		if (obj != null){
			Product product = new Product(obj);
			return product;
		}
		else{
			return null;
		}
	}

	public static void save(MongoDBClient mongoClient, Product product) {
		product.calculateRebate();
		mongoClient.save(DBConstants.T_PRODUCT, product.getDbObject());
	}
	
	public static List<?> findProductByCategory(MongoDBClient mongoClient, String city, int category){
		return null;
	}
}
