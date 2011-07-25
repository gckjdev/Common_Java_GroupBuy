package com.orange.groupbuy.dao;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;

public class ProductAddress extends CommonData {
	
	public ProductAddress(DBObject obj) {
		super(obj);
	}

	public ProductAddress() {
		super();
	}
	
	public void setGPS(double longitude, double latitude){
		List<Double> gps = new ArrayList<Double>();
		gps.add(Double.valueOf(latitude));
		gps.add(Double.valueOf(longitude));
		
		getDbObject().put(DBConstants.F_GPS, gps);
	}
	
	public String getCity() {
		return this.getString(DBConstants.F_CITY);
	}
	
	public String getAddress() {
		return this.getString(DBConstants.F_ADDRESS);
	}
	
}
	
