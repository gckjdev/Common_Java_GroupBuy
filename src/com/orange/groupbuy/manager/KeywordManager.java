package com.orange.groupbuy.manager;

import org.apache.log4j.Logger;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.HotKeyword;

public class KeywordManager {
    
    public static final Logger log = Logger.getLogger(KeywordManager.class.getName());

	public static boolean upsertKeyword(MongoDBClient mongoClient, String keyword){
		if (keyword == null)
			return false;
		
		BasicDBObject value = new BasicDBObject();
		value.put(DBConstants.F_COUNT, 1);

		DBObject incValue = new BasicDBObject();
		incValue.put("$inc", value);

		DBObject query = new BasicDBObject();
		query.put(DBConstants.F_KEYWORD, keyword);
		
		log.info("<upsertKeyword> query="+query.toString()+",value="+incValue);
		mongoClient.updateOrInsert(DBConstants.T_KEYWORD_STAT, query, incValue);

		
		return true;
	}

	public static HotKeyword findHotKeyword(MongoDBClient mongoClient,
			String keyword) {
		
		if (keyword == null || keyword.isEmpty())
			return null;
		
		DBObject query = new BasicDBObject();
		String key = DBConstants.F_KEYWORD.concat(".").concat(DBConstants.F_KEYWORD_NAME);
		query.put(key, keyword);
		
		log.info("<findHotKeyword> query="+query.toString());		
		DBObject obj = mongoClient.findOne(DBConstants.T_APP, query);
		if (obj == null)
			return null;
		else
			return new HotKeyword(obj, keyword);
	}
}
