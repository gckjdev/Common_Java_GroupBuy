package com.orange.groupbuy.manager;

import java.util.HashMap;
import java.util.Map;

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
		
		String loc = product.getLoc();
		String city = product.getCity();
		if (isProductExist(mongoClient, loc, city))
			return false;
		
		return mongoClient.insert(DBConstants.T_PRODUCT, product);
	}
}
