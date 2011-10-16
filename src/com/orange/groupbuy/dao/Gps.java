package com.orange.groupbuy.dao;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.orange.groupbuy.constant.DBConstants;

public class Gps extends BasicDBObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -315320850898331276L;
	double latitude;
	double longitude;

	public Gps(double latitude, double longitude) {
		super();
		put(DBConstants.F_LATITUDE, latitude);
		put(DBConstants.F_LONGITUDE, longitude);
	}

	public Gps(String latitude, String longitude) {
		super();
		this.latitude = Double.valueOf(latitude).doubleValue();
		this.longitude = Double.valueOf(longitude).doubleValue();

		put(DBConstants.F_LATITUDE, this.latitude);
		put(DBConstants.F_LONGITUDE, this.longitude);
	}
	
	public List<Double> toDoubleList(){
		List<Double> list = new LinkedList<Double>(); 
		list.add(Double.valueOf(longitude));
        list.add(Double.valueOf(latitude));
		return list;
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

    public static Gps fromObject(List<Double> list) {
        if (list == null || list.size() < 2)
            return null;
        double lat = list.get(0);
        double lng = list.get(1);
        if (0 == lat || 0 == lng) 
            return null;
        // range of china
        if (lat < 4 || lat > 53)
            return null;
        if (lng < 73 || lng > 136)
            return null;
        
        return new Gps(lat, lng);
    }
    
    public String toString() {
        return this.getLatitude() + "," + this.getLongitude();
    }

}
