package com.orange.groupbuy.dao;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
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
		
		BasicDBList list = (BasicDBList)dbObject.get(DBConstants.F_KEYWORD);
		if (list == null)
			return null;
		
		List<String> retList = new LinkedList<String>();
		
		Iterator iter = list.iterator();
		while (iter.hasNext()){
			BasicDBObject obj = (BasicDBObject) iter.next();
			String key = obj.getString(DBConstants.F_KEYWORD_NAME);
			if (key != null){
				retList.add(key);
			}
		}
		
		return retList;
		
		
//		return getStringList(DBConstants.F_KEYWORD);
	}

    public String getPushAppKey() {
        return (String)dbObject.get(DBConstants.F_PUSH_APP_KEY);
    }

    public String getPushAppSecret() {
        return (String)dbObject.get(DBConstants.F_PUSH_APP_SECRET);
    }

    public String getPushAppMasterSecret() {
        return (String)dbObject.get(DBConstants.F_PUSH_APP_MASTER_SECRET);
    }
    
    public String getDevCertificateFileName() {
        return (String)dbObject.get(DBConstants.F_PUSH_APP_DEV_CERTIFICATE);
    }
    
    public String getDevCertPassword() {
        return (String)dbObject.get(DBConstants.F_PUSH_APP_DEV_CERTIFICATE_PASSWORD);
    }
    
    public String getProductCertificateFileName() {
        return (String)dbObject.get(DBConstants.F_PUSH_APP_PRODUCT_CERTIFICATE);
    }
    
    public String getProductCertPassword() {
        return (String)dbObject.get(DBConstants.F_PUSH_APP_PRODUCT_CERTIFICATE_PASSWORD);
    }
    
}
