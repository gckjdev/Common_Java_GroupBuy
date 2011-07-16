package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.Rows;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.cassandra.CassandraClient;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.DateUtil;
import com.orange.groupbuy.constant.DBConstants;

public class UserManager {
	
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
}
