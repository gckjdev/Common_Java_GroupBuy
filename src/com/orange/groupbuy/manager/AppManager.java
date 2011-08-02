package com.orange.groupbuy.manager;

import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.App;

public class AppManager extends CommonManager {
	public static App getApp(MongoDBClient mongoClient, String appId) {
		DBObject object = mongoClient.findOne(DBConstants.T_APP, DBConstants.F_APPID, appId);
		if (object == null ) {
			return null;
		}
		String version = (String) object.get(DBConstants.F_VERSION);
		String appUrl = (String) object.get(DBConstants.F_APPURL);
		App app =  new App(version, appUrl, appId);
		return app;
	}
}
