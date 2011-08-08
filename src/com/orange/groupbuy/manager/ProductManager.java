package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.DateUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.ProductAddress;

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
		boolean result = mongoClient.insert(DBConstants.T_PRODUCT, product.getDbObject());
		if (!result)
			return false;
		
		// insert address into product address index table
		List<String> addressList = product.getAddress();
		List<List<Double>> gpsList = product.getGPS();
		if (addressList == null)
			return true;

		String productId = product.getId();
		String city = product.getCity();
		int i=0;
		int gpsLen = (gpsList == null) ? 0 : gpsList.size();
		boolean needUpdate = false;
		for (String addr : addressList){
			if (addr == null)
				continue;
			
			List<Double> gps = null;
			if (gpsList != null && i < gpsLen)
				gps = gpsList.get(i);
			
			ProductAddress address = AddressManager.findAddress(mongoClient, addr);
			if (address == null){			
//				log.info("<debug> create new address for productId "+productId+", address "+addr);
				AddressManager.createAddress(mongoClient, productId, addr, city, gps);
			}
			else{				
				// address found, check GPS data
				List<Double> gpsInAddress = address.getGPS();
				if (gpsInAddress != null && gpsInAddress.size() > 0){
//					log.info("<debug> address "+addr+" found, gps=("+gpsInAddress.get(0)+","+gpsInAddress.get(1)+"), update product "+product.getId());
					product.addGPS(gpsInAddress);
					needUpdate = true;
				}
				else{
					// append productId in existing list
//					log.info("<debug> append productId "+productId+" into address "+addr);
					address.addProductId(productId);
					mongoClient.save(DBConstants.T_IDX_PRODUCT_GPS, address.getDbObject());
				}
			}
			
			i++;
		}
		
		if (needUpdate){
			mongoClient.save(DBConstants.T_PRODUCT, product.getDbObject());
		}
		
		return true;
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

	private static boolean addCityIntoQuery(DBObject query, String city){
		if (city != null && city.length() > 0) {
			List<String> cityList = new ArrayList<String>();
			cityList.add(city);
			if (!city.equals(DBConstants.V_NATIONWIDE)) {
				cityList.add(DBConstants.V_NATIONWIDE);
			}
			
			DBObject in = new BasicDBObject();
			in.put("$in", cityList);
			query.put(DBConstants.F_CITY, in);
		}
		
		return true;
	}
	
	private static boolean addCategoryIntoQuery(DBObject query, List<Integer> categoryList){
		if (categoryList != null && categoryList.size() > 0) {			
			DBObject in = new BasicDBObject();
			in.put("$in", categoryList);
			query.put(DBConstants.F_CATEGORY, in);
		}
		
		return true;
	}
		
	// only query those product which are not expired
	private static boolean addExpirationIntoQuery(DBObject query){
		Date date = new Date();
		DBObject endDateCondition = new BasicDBObject();
		endDateCondition.put("$gte", date);		
		query.put(DBConstants.F_END_DATE, endDateCondition);
		
		return true;
	}
	
	private static boolean addGpsIntoQuery(DBObject query, double longitude, double latitude, double maxDistance){

		List<Double> gpsList = new ArrayList<Double>();
		gpsList.add(latitude);
		gpsList.add(longitude);

//		DBObject near = new BasicDBObject();
//		near.put("$near", gpsList);
//		near.put("$maxDistance", maxDistance);
//		query.put(DBConstants.F_GPS, near);

		DBObject within = new BasicDBObject();
		DBObject center = new BasicDBObject();
		BasicBSONList centerObj = new BasicBSONList();
		centerObj.add(gpsList);
		centerObj.add(maxDistance);
		
		center.put("$center", centerObj);
		within.put("$within", center);
		query.put(DBConstants.F_GPS, within);
		
		return true;		
	}

	// only query those product which starts from today
	private static boolean addTodayIntoQuery(DBObject query){
		Date date = DateUtil.getDateOfToday();
		DBObject startDateCondition = new BasicDBObject();
		startDateCondition.put("$gte", date);		
		query.put(DBConstants.F_START_DATE, startDateCondition);		
		return true;
	}		
	
	private static void addFieldIntoOrder(DBObject orderBy, String fieldName, boolean sortAscending){
		if (sortAscending) {
			orderBy.put(fieldName, 1);
		} else {
			orderBy.put(fieldName, -1);
		}		
	}
	
	public static void addPriceIntoOrder(DBObject orderBy, boolean sortAscending){
		addFieldIntoOrder(orderBy, DBConstants.F_PRICE, sortAscending);
	}
	
	public static void addRebateIntoOrder(DBObject orderBy, boolean sortAscending){
		addFieldIntoOrder(orderBy, DBConstants.F_REBATE, sortAscending);
	}

	public static void addBoughtIntoOrder(DBObject orderBy, boolean sortAscending){
		addFieldIntoOrder(orderBy, DBConstants.F_BOUGHT, sortAscending);
	}
	
	
	public static List<Product> getProducts(MongoDBClient mongoClient, String city, 
			List<Integer> categoryList, boolean todayOnly, 
			boolean gpsQuery, double latitude, double longitude, double maxDistance, 
			int sortBy, int startOffset, int maxCount){
		
		DBObject query = new BasicDBObject();
		DBObject orderBy = new BasicDBObject();
		
		// set query
		addCityIntoQuery(query, city);
		addCategoryIntoQuery(query, categoryList);
		addExpirationIntoQuery(query);
		if (gpsQuery){
			double degreeDistance = maxDistance * 1000 / 6371;
			addGpsIntoQuery(query, longitude, latitude, degreeDistance);
		}
		
		if (todayOnly){
			addTodayIntoQuery(query);
		}
		
		// set order by
//		if (!gpsQuery){
			switch (sortBy){
				case DBConstants.SORT_BY_PRICE:
					addFieldIntoOrder(orderBy, DBConstants.F_PRICE, true);
					break;
	
				case DBConstants.SORT_BY_REBATE:
					addFieldIntoOrder(orderBy, DBConstants.F_REBATE, true);
					break;
					
				case DBConstants.SORT_BY_BOUGHT:
					addFieldIntoOrder(orderBy, DBConstants.F_BOUGHT, false);
					break;
			}
			addFieldIntoOrder(orderBy, DBConstants.F_START_DATE, false);
//		}
		
		log.info("<getProducts> query = "+query.toString()+" , orderBy = "+
				orderBy+" startOffset = "+startOffset+", maxCount = "+maxCount);
			
		DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query, orderBy, startOffset, maxCount);
		return getProduct(cursor);		
	}
	
	
	public static List<Product> getAllProductsWithPrice(
			MongoDBClient mongoClient, String city, boolean sortAscending,
			int startOffset, int maxCount) {
				
		DBObject query = new BasicDBObject();
		DBObject orderBy = new BasicDBObject();
		
		// set query
		addCityIntoQuery(query, city);
		addExpirationIntoQuery(query);
		
		// set order by
		addPriceIntoOrder(orderBy, true);
		
		DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query, orderBy, startOffset, maxCount);
		return getProduct(cursor);
	}

	public static List<Product> getAllProductsWithBought(
			MongoDBClient mongoClient, String city, boolean sortAscending,
			String startOffset, String maxCount) {
		List<Product> list = getAllProductsWithField(mongoClient,
				DBConstants.F_BOUGHT, city, sortAscending, startOffset,
				maxCount);
		return list;
	}

	public static List<Product> getAllProductsWithRebate(
			MongoDBClient mongoClient, String city, boolean sortAscending,
			String startOffset, String maxCount) {
		List<Product> list = getAllProductsWithField(mongoClient,
				DBConstants.F_REBATE, city, sortAscending, startOffset,
				maxCount);
		return list;
	}

	private static List<Product> getProduct(DBCursor cursor) {
		if (cursor == null) {
			return null;
		}
		
		if (cursor.size() == 0){
			cursor.close();
			return null;
		}
		
		List<Product> productList = new ArrayList<Product>();
		while (cursor.hasNext()) {
			DBObject obj = cursor.next();
			if (obj != null) {
				Product product = new Product(obj);
				productList.add(product);
			}
		}
		
		cursor.close();
		return productList;
	}

	public static List<Product> getAllProductsWithLocation(
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

	public static List<Product> getAllProductsByCategory(
			MongoDBClient mongoClient, String city, String category,
			String startOffset, String maxCount){
		
		if (category == null)
			return null;
		
		List<String> list = new LinkedList<String>();
		list.add(category);
		return getAllProductsWithCategory(mongoClient, city, list, startOffset, maxCount);
	}
	
	public static List<Product> getAllProductsWithCategory(
			MongoDBClient mongoClient, String city, List<String> categoryList,
			String startOffset, String maxCount) {
		
		List<Object> cityList = null;
		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);
		
		if (city != null && city.trim().length() > 0) {
			cityList = new ArrayList<Object>();
			cityList.add(city);
			if (!city.equals(DBConstants.V_NATIONWIDE)) {
				cityList.add(DBConstants.V_NATIONWIDE);
			}
		}
		
		List<Object> categories = null;
		if (categoryList != null && categoryList.size() > 0) {
			categories = new ArrayList<Object>();
			for(String category : categoryList){
				Integer categoryInteger = Integer.valueOf(category);				
				categories.add(categoryInteger);
			}
		}
		Map<String, List<Object>> map = new HashMap<String, List<Object>>();
		if (cityList != null && cityList.size() > 0) {
			map.put(DBConstants.F_CITY, cityList);
		}
		if (categories !=null && categories.size() > 0) {
			map.put(DBConstants.F_CATEGORY, categories);
		}	
		
		DBCursor cursor = mongoClient.findByFieldsInValues(DBConstants.T_PRODUCT, map,  offset, count);
		List<Product> list = getProduct(cursor);
		return list;
	}

	public static List<String> getAllCategories(){
		List<String> categoryList = new ArrayList<String>();
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_EAT));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_FACE));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_FUN));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_SHOPPING));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_KEEPFIT));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_LIFE));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_UNKNOWN));		
		return categoryList;
	}
	
	public static List<Product> getAllProductsGroupByCategory(
			MongoDBClient mongoClient, String city, String topCount) {

		
		return null;
	}

	public static List<String> getAllCategoryNames() {
		List<String> categoryList = new ArrayList<String>();
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_EAT));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_FACE));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_FUN));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_SHOPPING));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_KEEPFIT));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_LIFE));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_UNKNOWN));		
		return categoryList;
	}

	
	
}
