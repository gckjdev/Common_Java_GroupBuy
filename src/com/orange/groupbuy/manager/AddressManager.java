package com.orange.groupbuy.manager;

import com.orange.common.mongodb.MongoDBClient;
import com.orange.groupbuy.dao.Coordinate;

public class AddressManager {

	public static Coordinate findCoordinateByAddress(MongoDBClient mongoClient, String address){
		return null;
	}
	
	public static boolean insertAddress(MongoDBClient mongoClient, String address, String productId){
		return true;
	}

	
}
