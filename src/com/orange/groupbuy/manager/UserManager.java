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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.cassandra.CassandraClient;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Gps;

public class UserManager extends CommonManager{
	
	public static DBObject findUserByDeviceId(MongoDBClient mongoClient, String deviceId){
		if (mongoClient == null || deviceId == null || deviceId.length() <= 0)
			return null;
		
		BasicDBObject query = new BasicDBObject();
		query.put(DBConstants.F_DEVICEID, deviceId);
		DBCollection collection = mongoClient.getDb().getCollection(DBConstants.T_USER);
		DBCursor cursor = collection.find(query);
		if (cursor == null || cursor.hasNext() == false)
			return null;
		
		return cursor.next();		
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
			String email, String password) {
		
		BasicDBObject user = new BasicDBObject();
		user.put(DBConstants.F_APPID, appId);
		user.put(DBConstants.F_EMAIL, email);
		user.put(DBConstants.F_PASSWORD, StringUtil.md5base64encode(password));
		user.put(DBConstants.F_VERIFYCODE, StringUtil.randomUUID());
		user.put(DBConstants.F_CREATE_DATE, new Date()); //DateUtil.currentDate());
		user.put(DBConstants.F_STATUS, DBConstants.STATUS_TO_VERIFY);
		
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
}
