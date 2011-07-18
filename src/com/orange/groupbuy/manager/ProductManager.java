package com.orange.groupbuy.manager;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;

public class ProductManager {

	public static boolean createProduct(MongoDBClient mongoClient, Product product){		
		return mongoClient.insert(DBConstants.T_PRODUCT, product);
	}
}
