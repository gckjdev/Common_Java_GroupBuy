package com.orange.groupbuy.manager;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;

public class KeywordManager {

	public static boolean upsertKeyword(MongoDBClient mongoClient, String keyword){
		if (keyword == null)
			return false;
		
		BasicDBObject value = new BasicDBObject();
		value.put(DBConstants.F_COUNT, 1);

		DBObject incValue = new BasicDBObject();
		incValue.put("$inc", value);

		DBObject query = new BasicDBObject();
		query.put(DBConstants.F_KEYWORD, keyword);
		
		System.out.println("<upsertKeyword> query="+query.toString()+",value="+incValue);
		mongoClient.updateOrInsert(DBConstants.T_KEYWORD_STAT, query, incValue);

		
		return true;
	}
}
