package com.orange.groupbuy.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;

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
	
	public List<Double> getGPS(){
		return (List<Double>)getDbObject().get(DBConstants.F_GPS);
	}
	
	public String getCity() {
		return this.getString(DBConstants.F_CITY);
	}
	
	public String getAddress() {
		return this.getString(DBConstants.F_ADDRESS);
	}
	
	public void addProductId(String productId){
		if (productId == null || productId.length() == 0)
			return;
				
		List<ObjectId> list = (List<ObjectId>)getDbObject().get(DBConstants.F_PRODUCTID);
		if (!getDbObject().containsField(DBConstants.F_PRODUCTID)){
			list = new LinkedList<ObjectId>();
			list.add(new ObjectId(productId));
			getDbObject().put(DBConstants.T_PRODUCT, list);
		}
		else{		
			list.add(new ObjectId(productId));
		}
	}
	
}
	
