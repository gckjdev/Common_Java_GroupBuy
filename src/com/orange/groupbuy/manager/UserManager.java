package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import antlr.StringUtils;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.cassandra.CassandraClient;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.urbanairship.RegisterService;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.constant.PushNotificationConstants;
import com.orange.groupbuy.dao.Gps;
import com.orange.groupbuy.dao.User;

public class UserManager extends CommonManager{
	
	public static DBObject findUserByDeviceId(MongoDBClient mongoClient, String deviceId){
		if (mongoClient == null || deviceId == null || deviceId.length() <= 0)
			return null;
		
		DBObject obj = mongoClient.findOne(DBConstants.T_USER, DBConstants.F_DEVICEID, deviceId);
		return obj;
	}
	
	public static DBObject findUserByEmail(MongoDBClient mongoClient, String email) {
		if (mongoClient == null || email == null || email.length() <= 0)
			return null;
		
		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_EMAIL, email);
		DBCollection collection = mongoClient.getDb().getCollection(DBConstants.T_USER);
		DBCursor cursor = collection.find(query);
		if (cursor == null || cursor.hasNext() == false)
			return null;
		
		return cursor.next();
	}

	public static DBObject findUserByVerifyCode(MongoDBClient mongoClient, String vcd) {
		if (mongoClient == null || vcd == null || vcd.length() <= 0)
			return null;
		
		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_VERIFYCODE, vcd);
		DBCollection collection = mongoClient.getDb().getCollection(DBConstants.T_USER);
		DBCursor cursor = collection.find(query);
		if (cursor == null || cursor.hasNext() == false)
			return null;				
		
		return cursor.next();
	}
	
	public static void updateStatusByVerifyCode(MongoDBClient mongoClient, String sta, String vcd){
		if (mongoClient == null || sta == null || sta.length() <= 0 || vcd == null || vcd.length() <= 0)
			return;				
		
		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_VERIFYCODE, vcd);
		
		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		updateValue.put(DBConstants.F_STATUS, sta);
		update.put("$set", updateValue);
		
		DBCollection collection = mongoClient.getDb().getCollection(DBConstants.T_USER);
		collection.findAndModify(query, null, null, false, update, true,false);
	}
	
	public static void updateEmail(MongoDBClient mongoClient, String email, String new_email){
		if (mongoClient == null || email == null || email.length() <= 0 || new_email == null || new_email.length() <= 0 )
			return;
		
		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_EMAIL, email);
		
		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		updateValue.put(DBConstants.F_EMAIL, new_email);
		update.put("$set", updateValue);
		
		DBCollection collection = mongoClient.getDb().getCollection(DBConstants.T_USER);
		collection.findAndModify(query, null, null, false, update, true,false);
		
		return;
	}
	
	public static void updatePassword(MongoDBClient mongoClient, String mail, String new_pwd){
		if (mongoClient == null || mail == null || mail.length() <= 0 || new_pwd == null || new_pwd.length() <= 0 )
			return;
		
		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_EMAIL, mail);
		
		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		updateValue.put(DBConstants.F_PASSWORD, new_pwd);
		update.put("$set", updateValue);
		
		DBCollection collection = mongoClient.getDb().getCollection(DBConstants.T_USER);
		collection.findAndModify(query, null, null, false, update, true,false);
		
		return;
	}
	
	public static BasicDBObject createDeviceUser(MongoDBClient mongoClient, String appId,
			String deviceModel, String deviceId, String deviceOS,
			String deviceToken, String language, String countryCode) {
		
		BasicDBObject user = new BasicDBObject();
		user.put(DBConstants.F_APPID, appId);
		user.put(DBConstants.F_DEVICEMODEL, deviceModel);
		user.put(DBConstants.F_DEVICEID, deviceId);
		user.put(DBConstants.F_DEVICEOS, deviceOS);
		user.put(DBConstants.F_DEVICETOKEN, deviceToken);
		user.put(DBConstants.F_LANGUAGE, language);
		user.put(DBConstants.F_COUNTRYCODE, countryCode);
		user.put(DBConstants.F_CREATE_DATE, new Date()); //DateUtil.currentDate());
		user.put(DBConstants.F_CREATE_SOURCE_ID, appId);
		user.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);
		
		boolean result = mongoClient.insert(DBConstants.T_USER, user);
		if (result)
			return user;
		else
			return null;
	}
	
	public static BasicDBObject createUserByEmail(MongoDBClient mongoClient, String appId,
			String email, String password, boolean isVerification) {
		
		BasicDBObject user = new BasicDBObject();
		user.put(DBConstants.F_APPID, appId);		
		user.put(DBConstants.F_CREATE_SOURCE_ID, appId);
		user.put(DBConstants.F_EMAIL, email);
		user.put(DBConstants.F_PASSWORD, password);
		user.put(DBConstants.F_VERIFYCODE, StringUtil.randomUUID());
		user.put(DBConstants.F_CREATE_DATE, new Date()); //DateUtil.currentDate());
		if (isVerification)
			user.put(DBConstants.F_STATUS, DBConstants.STATUS_TO_VERIFY);
		else
			user.put(DBConstants.F_STATUS, DBConstants.STATUS_NORMAL);
		
		boolean result = mongoClient.insert(DBConstants.T_USER, user);
		if (result)
			return user;
		else
			return null;
	}

	public static void addSearchKeyword(MongoDBClient mongoClient, String deviceId, String keywords) {
		addSearchKeyword(mongoClient, deviceId, keywords, false, 0.0f, 0.0f);
	}

	public static void addSearchKeyword(MongoDBClient mongoClient,
			String deviceId, String keywords, double longitude, double latitude) {
		addSearchKeyword(mongoClient, deviceId, keywords, true, longitude, latitude);
	}
	
	public static void addSearchKeyword(MongoDBClient mongoClient,
			String deviceId, String keyword, boolean hasLocation, double longitude, double latitude) {
		
		if (deviceId == null || keyword == null || keyword.isEmpty())
			return;
		
		// user found, add keywords into user history
		BasicDBObject value = new BasicDBObject();
		value.put(DBConstants.F_SEARCH_HISTORY, keyword);

		DBObject addToSet = new BasicDBObject();
		addToSet.put("$addToSet", value);

		DBObject query = new BasicDBObject();
		query.put(DBConstants.F_DEVICEID, deviceId);
		
		System.out.println("<addSearchKeyword> query="+query.toString()+",value="+addToSet);
		mongoClient.updateAll(DBConstants.T_USER, query, addToSet);
		
	}
	
	public static void addSearchKeywordDetailRecord(MongoDBClient mongoClient,
			String deviceId, String[] keywords, boolean hasLocation, double longitude, double latitude) {
		
		if (deviceId == null || keywords == null || keywords.length == 0)
			return;
		
		// user found, add keywords into user history
		BasicDBObject searchRecord = new BasicDBObject();
		
		// set date
		searchRecord.put(DBConstants.F_DATE, new Date());

		// set keywords
		BasicDBList keywordList = new BasicDBList();
		for (int i=0; i<keywords.length; i++){
			keywordList.add(keywords[i]);
		}
		searchRecord.put(DBConstants.F_KEYWORD, keywordList);

		if (hasLocation){
			Gps gps = new Gps(latitude, longitude);
			searchRecord.put(DBConstants.F_GPS, gps.toDoubleList());
		}
		
		DBObject value = new BasicDBObject();
		value.put(DBConstants.F_SEARCH_HISTORY, searchRecord);
		
		DBObject pushValue = new BasicDBObject();
		pushValue.put("$push", value);

		DBObject query = new BasicDBObject();
		query.put(DBConstants.F_DEVICEID, deviceId);
		
		System.out.println("<addSearchKeyword> query="+query.toString()+",value="+pushValue);
		mongoClient.updateAll(DBConstants.T_USER, query, pushValue);
	}

	public static User findUserByUserId(MongoDBClient mongoClient, String userId) {
		if (mongoClient == null || userId == null || userId.length() <= 0)
			return null;
		
		DBObject obj = mongoClient.findOne(DBConstants.T_USER, DBConstants.F_USERID, userId);
		if (obj == null)
			return null;
		
		return new User(obj);
	}

	public static boolean save(MongoDBClient mongoClient, User user) {
		mongoClient.save(DBConstants.T_USER, user.getDbObject());
		return true;
	}
	
	private static BasicDBObject createItemForAdd(String itemId,
			String categoryName, String subCategoryName, String keywords,
			double maxPrice, double minRebate){
		
		if (StringUtil.isEmpty(itemId))
			return null;

		BasicDBObject item = new BasicDBObject();		
		item.put(DBConstants.F_ITEM_ID, itemId);
		item.put(DBConstants.F_CREATE_DATE, new Date());	
		
		if (!StringUtil.isEmpty(categoryName))
			item.put(DBConstants.F_CATEGORY_NAME, categoryName);
		if (!StringUtil.isEmpty(subCategoryName))
			item.put(DBConstants.F_SUB_CATEGORY_NAME, subCategoryName);
		if (!StringUtil.isEmpty(keywords))
			item.put(DBConstants.F_KEYWORD, keywords);
		if (maxPrice >= 0.0f)
			item.put(DBConstants.F_MAX_PRICE, maxPrice);
		if (minRebate >= 0.0f)
			item.put(DBConstants.F_MIN_REBATE, minRebate);
		
		return item;
	}

	private static BasicDBObject createItemForUpdate(String itemId,
			String categoryName, String subCategoryName, String keywords,
			double maxPrice, double minRebate){
						
		if (StringUtil.isEmpty(itemId))
			return null;
		
		// key should like "xxx.$.xxx" for array update
		String prefix = DBConstants.F_SHOPPING_LIST.concat(".$.");

		BasicDBObject item = new BasicDBObject();		
		
		item.put(prefix.concat(DBConstants.F_CATEGORY_NAME), categoryName);
		item.put(prefix.concat(DBConstants.F_SUB_CATEGORY_NAME), subCategoryName);
		item.put(prefix.concat(DBConstants.F_KEYWORD), keywords);

		if (maxPrice >= 0.0f)
			item.put(prefix.concat(DBConstants.F_MAX_PRICE), maxPrice);
		else
			item.put(prefix.concat(DBConstants.F_MAX_PRICE), null);			
			
		if (minRebate >= 0.0f)
			item.put(prefix.concat(DBConstants.F_MIN_REBATE), minRebate);
		else
			item.put(prefix.concat(DBConstants.F_MIN_REBATE), null);
		
		return item;
	}

	
	public static boolean addUserShoppingItem(MongoDBClient mongoClient, String userId, String itemId,
			String categoryName, String subCategoryName, String keywords,
			double maxPrice, double minRebate) {
		
		BasicDBObject item = createItemForAdd(itemId, categoryName, 
				subCategoryName, keywords, maxPrice, minRebate);
		if (item == null)
			return false;

		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_USERID, userId);

		BasicDBObject pushValue = new BasicDBObject();		
		pushValue.put(DBConstants.F_SHOPPING_LIST, item);

		BasicDBObject update = new BasicDBObject();				
		update.put("$push", pushValue);
		
		mongoClient.updateAll(DBConstants.T_USER, query, update);		
		return true;
	}

	private static String getItemArrayKey(){
		return DBConstants.F_SHOPPING_LIST.concat(".").concat(DBConstants.F_ITEM_ID);
	}
	
	public static boolean updateUserShoppingItem(MongoDBClient mongoClient,
			String userId, String itemId, String categoryName,
			String subCategoryName, String keywords, double maxPrice,
			double minRebate) {

		BasicDBObject item = createItemForUpdate(itemId, categoryName, 
				subCategoryName, keywords, maxPrice, minRebate);
		if (item == null)
			return false;

		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_USERID, userId);
		String queryKeyForItem = getItemArrayKey();
		query.put(queryKeyForItem, itemId);

		BasicDBObject update = new BasicDBObject();
		update.put("$set", item);
		
		mongoClient.updateAll(DBConstants.T_USER, query, update);		
		return true;
	}

	public static boolean deleteUserShoppingItem(MongoDBClient mongoClient,
			String userId, String itemId) {

		if (itemId == null || userId == null)
			return false;
		
		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_USERID, userId);
		String queryKeyForItem = getItemArrayKey();
		query.put(queryKeyForItem, itemId);

		BasicDBObject update = new BasicDBObject();
		BasicDBObject unsetValue = new BasicDBObject();
		String unsetKey = getItemArrayKey();
		unsetValue.put(unsetKey, 1);			// remove the key found
		update.put("$unset", unsetValue);
		
		mongoClient.updateAll(DBConstants.T_USER, query, update);		
		return false;
	}

	public static void registerUserDeviceToken(String userId, String deviceToken) {
		RegisterService registerService = RegisterService.createService(
				PushNotificationConstants.APPLICATION_KEY, 
				PushNotificationConstants.APPLICATION_SECRET,
				PushNotificationConstants.APPLICATION_MASTER_SECRET,
				userId,
				deviceToken);
		registerService.handleServiceRequest();
	}
}
