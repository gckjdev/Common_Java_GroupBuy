package com.orange.common.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Gps;
import com.orange.groupbuy.dao.Subscription;
import com.orange.groupbuy.manager.UserManager;


public class MongoDBClientTest {

	MongoDBClient mongoClient;
	static String userId;
	static String deviceId;
	private Random seed;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() {
		mongoClient = new MongoDBClient("localhost", "groupbuy", "", "");
		seed = new Random();
	}

	@Test
	public void testCreateDeviceUser() {
		String testDeviceId = String.format("device_id_%d", (long)(Math.random() * 1000));
		BasicDBObject user = UserManager.createDeviceUser(mongoClient, "test_app", "iphone", testDeviceId, "iOS", "token", "zh_Hans", "CN");
		Assert.assertNotNull(user);
		System.out.println("testCreateDeviceUser, user="+user.toString());
		Assert.assertNotNull(user.getString(MongoDBClient.ID));		
		userId = user.getString(MongoDBClient.ID);
		deviceId = user.getString(DBConstants.F_DEVICEID);
		System.out.println("testCreateDeviceUser, userId="+userId);
		System.out.println("testCreateDeviceUser, deviceId="+deviceId);
	}
	
	@Test
	public void testFindUserByDevice(){
		
		if (deviceId == null)
			Assert.fail();
		
		DBObject user = UserManager.findUserByDeviceId(mongoClient, deviceId);
		Assert.assertNotNull(user);
		System.out.println("testFindUserByDevice, user="+user.toString());
		Assert.assertNull(UserManager.findUserByDeviceId(mongoClient, "not_found_device_id"));
	}
	
	@Test
	public void testInsertSubscription(){
		Gps gps = new Gps(12.5, 45.2);
		Gps gps2 = new Gps(23.6, 34.3);
		BasicDBObject object = new BasicDBObject();
		List<String> gList = new ArrayList<String>();
		List<String> pList = new ArrayList<String>();
		List<Gps> gpsList = new ArrayList<Gps>();
		gList.add("g1");
		gList.add("g2");
		
		pList.add("p1");
		pList.add("p2");
		
		gpsList.add(gps);
		gpsList.add(gps2);
		
		Subscription subscription = new Subscription(gList, pList, gpsList);
		object.put("id", "test");
		//object.put("subscription", subscription);
		mongoClient.insert("test", object);
		

		System.out.println("object = "+object.toString());
		
		Map<String, Object> map =  new HashMap<String, Object>();
		map.put("_id", "4e2559d58b644df2d1523560");
		Map<String,Object>map2 = new HashMap<String, Object>();
		map2.put("test", "test");
		map2.put("subscription", subscription);
		mongoClient.findAndModify("test", map, map2);
		//"4e2550db9508bea4323d7f04"


	}
	
//	public static void main(String[] arg) {
//		MongoDBClientTest test = new MongoDBClientTest();
//		test.setUp();
//		test.testInsertSubscription();
//		
//		
//	}
}
