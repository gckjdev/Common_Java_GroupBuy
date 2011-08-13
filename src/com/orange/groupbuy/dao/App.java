package com.orange.groupbuy.dao;

import java.util.List;

import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class App extends CommonData {

	public App(DBObject dbObject) {
		super(dbObject);
	}

	public String getAppId() {
		String appId = (String) dbObject.get(DBConstants.F_APPID);
		return appId;
	}

	public String getVersion() {
		String version = (String) dbObject.get(DBConstants.F_VERSION);
		return version;
	}

	public String getAppUrl() {
		String appUrl = (String) dbObject.get(DBConstants.F_APPURL);
		return appUrl;
	}

	public List<String> getAppKeywordList() {		
		return getStringList(DBConstants.F_KEYWORD);
	}

}
