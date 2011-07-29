package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Coordinate;
import com.orange.groupbuy.dao.ProductAddress;

public class AddressManager {

	public static Coordinate findCoordinateByAddress(MongoDBClient mongoClient, String address){
		return null;
	}
	
	public static boolean createAddress(MongoDBClient mongoClient, String productId, String address, String city, List<Double> gps){
		if (productId == null ||
			address == null ||
			city == null){
			return false;
		}
		
		BasicDBObject docObject = new BasicDBObject();
		
		docObject.put(DBConstants.F_ADDRESS, address);
		docObject.put(DBConstants.F_GPS, gps);
		docObject.put(DBConstants.F_CITY, city);
		
		List<ObjectId> productList = new LinkedList<ObjectId>();
		ObjectId productObjectId = new ObjectId(productId);		
		productList.add(productObjectId);
		docObject.put(DBConstants.F_PRODUCTID, productList);
		
		return mongoClient.insert(DBConstants.T_IDX_PRODUCT_GPS, docObject);
	}

	public static List<ProductAddress> findAddressForGPSUpdate(MongoDBClient mongoClient, int limit){
		
		DBCursor cursor = mongoClient.find(DBConstants.T_IDX_PRODUCT_GPS, DBConstants.F_GPS, null, limit);
		if (cursor == null)
			return null;

		try{
			List<ProductAddress> list = new ArrayList<ProductAddress>();
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				list.add(new ProductAddress(obj));
			}
			cursor.close();		
			return list;
		}catch(Exception e){
			cursor.close();		
			e.printStackTrace();	// TODO to be improved
			return null;			
		}			
	}
	
	public static boolean findAndUpdateGPS(MongoDBClient mongoClient, List<ProductAddress> productAddressList){
		if (productAddressList == null)
			return false;
		
		for (ProductAddress address : productAddressList){
			mongoClient.save(DBConstants.T_IDX_PRODUCT_GPS, address.getDbObject());
		}
		
		return true;
	}

	public static ProductAddress findAddress(
			MongoDBClient mongoClient, String addr) {
		
		DBCursor cursor = mongoClient.find(DBConstants.T_IDX_PRODUCT_GPS, DBConstants.F_ADDRESS, addr, 1);
		if (cursor != null && cursor.hasNext()){
			DBObject obj = cursor.next();
			ProductAddress address = new ProductAddress(obj);
			cursor.close();
			return address;
		}
		
		return null;
	}
	
}
