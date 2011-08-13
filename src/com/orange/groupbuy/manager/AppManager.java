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
		App app =  new App(object);
		return app;
	}
}
