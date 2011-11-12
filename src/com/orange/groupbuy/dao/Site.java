package com.orange.groupbuy.dao;

import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class Site extends CommonData {
    
    public Site(DBObject dbObject) {
        super(dbObject);
    }

    public String getFileType() {
        return this.getString(DBConstants.F_FILE_TYPE);
    }

    public String getSiteURL() {
        return this.getString(DBConstants.F_SITE_URL);
    }

    public int getType() {
        return this.getInt(DBConstants.F_TYPE);
    }

    public String getName() {
        return this.getString(DBConstants.F_SITE_NAME);
    }

    public int getDownloadCount() {
        return this.getInt(DBConstants.F_DOWNLOAD_COUNT);
    }

    public String getCountryCode() {
        return this.getString(DBConstants.F_COUNTRYCODE);
    }

}
