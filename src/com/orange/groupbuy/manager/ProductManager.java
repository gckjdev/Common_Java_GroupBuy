package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
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

	public static List<?> findProductByCategory(MongoDBClient mongoClient,
			String city, int category) {
		return null;
	}

	private static List<Product> getAllProductsWithField(
			MongoDBClient mongoClient, String fieldName, String city,
			boolean sortAscending, String startOffset, String maxCount) {

		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);
		List<String> cityList = null;
		if (city != null && city.trim().length() > 0) {
			cityList = new ArrayList<String>();
			cityList.add(city);
			if (!city.equals(DBConstants.V_NATIONWIDE)) {
				cityList.add(DBConstants.V_NATIONWIDE);
			}
		}
		DBCursor cursor = mongoClient.findByFieldInValues(
				DBConstants.T_PRODUCT, DBConstants.F_CITY, cityList, fieldName,
				sortAscending, offset, count);
		List<Product> list = getProduct(cursor);
		cursor.close();
		return list;

	}

	public static List<Product> getAllProductWithPrice(
			MongoDBClient mongoClient, String city, boolean sortAscending,
			String startOffset, String maxCount) {
		List<Product> list = getAllProductsWithField(mongoClient,
				DBConstants.F_PRICE, city, sortAscending, startOffset, maxCount);
		return list;
	}

	public static List<Product> getAllProductWithBought(
			MongoDBClient mongoClient, String city, boolean sortAscending,
			String startOffset, String maxCount) {
		List<Product> list = getAllProductsWithField(mongoClient,
				DBConstants.F_BOUGHT, city, sortAscending, startOffset,
				maxCount);
		return list;
	}

	public static List<Product> getAllProductWithRebate(
			MongoDBClient mongoClient, String city, boolean sortAscending,
			String startOffset, String maxCount) {
		List<Product> list = getAllProductsWithField(mongoClient,
				DBConstants.F_REBATE, city, sortAscending, startOffset,
				maxCount);
		return list;
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

	public static List<Product> getAllProductWithLocation(
			MongoDBClient mongoClient, String latitude, String longitude,
			String startOffset, String maxCount) {

		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);

		double latitudeD = getLatitude(latitude);
		double longitudeD = getLongitude(longitude);
		
		DBCursor idCursor = mongoClient.findNearby(
				DBConstants.T_IDX_PRODUCT_GPS, DBConstants.F_GPS, latitudeD,
				longitudeD, offset, count);
		
		if (idCursor == null || idCursor.size() < 1) {
			return null;
		}
		
		List<Object> productIdList = new ArrayList<Object>();
		while (idCursor.hasNext()) {
			DBObject productObject = idCursor.next();
			Object productId = productObject.get(DBConstants.F_PRODUCTID);
			if (productId != null) {
				productIdList.add(productId);
			}
		}
	
		idCursor.close();
		
		if (productIdList == null || productIdList.size() < 1) {
			return null;
		}
		
		DBCursor productCursor = mongoClient.findByFieldInValues(DBConstants.T_PRODUCT,
				DBConstants.F_ID, productIdList, offset, count);
		
		List<Product> productList = getProduct(productCursor);
		productCursor.close();
		
		if (productList == null || productList.size() < 1) {
			return null;
		}
		return sortByProductId(productIdList, productList);
	}

	private static List<Product> sortByProductId(List<Object> productIdList,
			List<Product> productList) {
		Map<Object, Product> map = new HashMap<Object, Product>();
		for (Product product : productList) {
			map.put(product.getObjectId(), product);
		}
		List<Product> products = new ArrayList<Product>();
		for (Object id : productIdList) {
			if (map.containsKey(id)) {
				products.add(map.get(id));
				map.remove(id);
			}
		}
		return products;
	}

}
