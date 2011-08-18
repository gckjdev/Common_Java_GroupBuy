package com.orange.groupbuy.dao;

import java.util.Iterator;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class HotKeyword extends CommonData {
	
	String keyword;
	String queryString;
	
	public HotKeyword(DBObject dbObject, String keyword) {
		super(dbObject);
		this.keyword = keyword;
	}
	
	public String getQueryString(){

		if (keyword == null)
			return null;
		
		if (queryString == null){
			BasicDBList obj = (BasicDBList)dbObject.get(DBConstants.F_KEYWORD);
			if (obj == null)
				return null;
			
			Iterator iter = obj.iterator();
			while (iter.hasNext()){
				BasicDBObject keyValue = (BasicDBObject)iter.next();
				if (obj != null){
					String keyword = keyValue.getString(DBConstants.F_KEYWORD_NAME);
					String query = keyValue.getString(DBConstants.F_KEYWORD_QUERY);
					if (keyword != null && keyword.equalsIgnoreCase(this.keyword)){
						this.queryString = query;
						break;
					}
				}
			}
		}
		
		return queryString;
				
	}
	
	
}
