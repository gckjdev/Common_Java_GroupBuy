package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;

import com.mongodb.DB;
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

	public static  List<Product> getAllProductWithLocation(MongoDBClient mongoClient,
			String latitude, String longitude, String startOffset,
			String maxCount) {

		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);

		double latitudeD = getLatitude(latitude);
		double longitudeD = getLongitude(longitude);
		DBCursor productIdResult = mongoClient.findNearby(
				DBConstants.T_IDX_PRODUCT_GPS, DBConstants.F_GPS, latitudeD,
				longitudeD, offset, count);
		if (productIdResult == null || productIdResult.size() < 1) {
			return null;
		}
		List<Object> productIdList = new ArrayList<Object>();
		while (productIdResult.hasNext()) {
			DBObject productObject = productIdResult.next();
			Object productId = productObject.get(DBConstants.F_PRODUCTID);
			if (productId != null) {
				productIdList.add(productId);
			}
		}

		DBCursor productResult = mongoClient.findAll(DBConstants.T_PRODUCT,
				DBConstants.F_ID, productIdList, offset, count);
		if (productIdList == null || productIdList.size() < 1) {
			return null;
		}
		
		List<Product> productList = getProduct(productResult);
		if (productList == null || productList.size() < 1) {
			return null;
		}
		return sortByProductId(productIdList,productList);
	}

	private static List<Product> sortByProductId(List<Object> productIdList,
			List<Product> productList) {
		
		//System.out.println("id List: "+productIdList);
		//System.out.println("product List: "+productList);
		Map<Object,Product> map = new HashMap<Object, Product>();
		for(Product product : productList){
			//System.out.println("id="+product.getStringObjectId()+", product="+product);
			map.put(product.getObjectId(), product);
		}
		//System.out.println("map ="+ map);
		List<Product>products = new ArrayList<Product>();
		for(Object id : productIdList){
			if (map.containsKey(id)) {
				products.add(map.get(id));
				map.remove(id);
				//products.remove(id);
			}
		}
		return products;
	}

}
