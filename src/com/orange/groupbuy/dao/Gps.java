package com.orange.groupbuy.dao;

import com.mongodb.BasicDBObject;
import com.orange.groupbuy.constant.DBConstants;

public class Gps extends BasicDBObject{

	double latitude;
	double longitude;

	public Gps(double latitude, double longitude) {
		super();
		put(DBConstants.F_LATITUDE, latitude);
		put(DBConstants.F_LONGITUDE, longitude);
	}

	public double getLatitude() {
		return getDouble(DBConstants.F_LATITUDE);
	}

	public void setLatitude(double latitude) {
		put(DBConstants.F_LATITUDE, latitude);
	}

	public double getLongitude() {
		return getDouble(DBConstants.F_LONGITUDE);
	}

	public void setLongitude(double longitude) {
		put(DBConstants.F_LONGITUDE, longitude);
	}

}
