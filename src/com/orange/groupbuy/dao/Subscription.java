package com.orange.groupbuy.dao;

import java.util.List;

import com.mongodb.BasicDBObject;
import com.orange.groupbuy.constant.DBConstants;

public class Subscription extends BasicDBObject {
	
	public Subscription(List<String> categories, List<String> keywords,
			List<Gps> gps) {
		super();
		
		put(DBConstants.F_CATEGORY,categories);
		put(DBConstants.F_KEYWORD,keywords);
		put(DBConstants.F_GPS,gps);
	}
	
	public List<String> getCategories() {
		return (List<String>)get(DBConstants.F_CATEGORY);
	}
	public void setCategories(List<String> categories) {
		put(DBConstants.F_CATEGORY,categories);
	}
	public List<String> getKeywords() {
		return (List<String>)get(DBConstants.F_KEYWORD);
	}
	public void setKeywords(List<String> keywords) {
		put(DBConstants.F_KEYWORD,keywords);
	}
	public List<Gps> getGps() {
		return (List<Gps>)get(DBConstants.F_GPS);
	}
	public void setGps(List<Gps> gps) {
		put(DBConstants.F_GPS,gps);
	}
	public String getId() {
		return this.getString("_id");
	}
	
}
