package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;

public class ProductManager extends CommonManager {

	public static boolean isProductExist(MongoDBClient mongoClient,
			String productURL, String city) {
		Map<String, String> fieldValues = new HashMap<String, String>();
		fieldValues.put(DBConstants.F_LOC, productURL);
		fieldValues.put(DBConstants.F_CITY, city);
		return (mongoClient.findOne(DBConstants.T_PRODUCT, fieldValues) != null);
	}

	public static boolean createProduct(MongoDBClient mongoClient,
			Product product) {

		// String loc = product.getLoc();
		// String city = product.getCity();
		// if (isProductExist(mongoClient, loc, city))
		// return false;
		product.calculateRebate();
		return mongoClient.insert(DBConstants.T_PRODUCT, product.getDbObject());
	}

	public static Product findProduct(MongoDBClient mongoClient,
			String productURL, String city) {
		Map<String, String> fieldValues = new HashMap<String, String>();
		fieldValues.put(DBConstants.F_LOC, productURL);
		fieldValues.put(DBConstants.F_CITY, city);
		DBObject obj = mongoClient.findOne(DBConstants.T_PRODUCT, fieldValues);
		if (obj != null) {
			Product product = new Product(obj);
			return product;
		} else {
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

	public static List<Product> getAllProductWithPrice(
			MongoDBClient mongoClient, String city, boolean range,
			String startOffset, String maxCount) {
		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);
		List<String> cityList = new ArrayList<String>();
		if (city == null || city.trim().length() < 1) {
			cityList.add(city);
			if (!city.equals(DBConstants.V_NATIONWIDE)) {
				cityList.add(DBConstants.V_NATIONWIDE);
			}			
		}
		DBCursor result = mongoClient.findAll(DBConstants.T_PRODUCT,
				DBConstants.F_PRICE, DBConstants.F_CITY, cityList, range,
				offset, count);
		return getProduct(result);
	}

	public static List<Product> getAllProductWithBought(
			MongoDBClient mongoClient, String city, boolean range,
			String startOffset, String maxCount) {
		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);
		List<String> cityList = new ArrayList<String>();
		if (city == null || city.trim().length() < 1) {
			cityList.add(city);
			if (!city.equals(DBConstants.V_NATIONWIDE)) {
				cityList.add(DBConstants.V_NATIONWIDE);
			}			
		}
		DBCursor result = mongoClient.findAll(DBConstants.T_PRODUCT,
				DBConstants.F_BOUGHT, DBConstants.F_CITY, cityList, range,
				offset, count);
		return getProduct(result);
	}

	public static List<Product> getAllProductWithRebate(
			MongoDBClient mongoClient, String city, boolean range,
			String startOffset, String maxCount) {
		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);
		List<String> cityList = new ArrayList<String>();
		if (city == null || city.trim().length() < 1) {
			cityList.add(city);
			if (!city.equals(DBConstants.V_NATIONWIDE)) {
				cityList.add(DBConstants.V_NATIONWIDE);
			}			
		}
		DBCursor result = mongoClient.findAll(DBConstants.T_PRODUCT,
				DBConstants.F_REBATE, DBConstants.F_CITY, cityList, range,
				offset, count);
		return getProduct(result);
	}

	private static List<Product> getProduct(DBCursor result) {
		if (result == null || result.size() < 1) {
			return null;
		}
		List<Product> productList = new ArrayList<Product>();
		while (result.hasNext()) {
			DBObject obj = result.next();
			if (obj != null) {
				Product product = new Product(obj);
				productList.add(product);
			}
		}
		return productList;
	}

}
