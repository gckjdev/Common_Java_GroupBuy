package com.orange.groupbuy.dao;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import net.sf.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CommonData {

	DBObject dbObject;	
	
	public CommonData(){
		dbObject = new BasicDBObject();
	}
	
	public CommonData(DBObject dbObject){
		this.dbObject = dbObject;
	}	

	
	
	public DBObject getDbObject() {
		return dbObject;
	}

	public void setDbObject(DBObject dbObject) {
		this.dbObject = dbObject;
	}

	public String toString() {
		return dbObject.toMap().toString();
	}
	
	public void put(String key, String value){
		dbObject.put(key, value);
	}
	
	public void put(String key, int value){
		dbObject.put(key, Integer.valueOf(value));
	}
	
	public void put(String key, double value){
		dbObject.put(key, Double.valueOf(value));
	}
	
	public void put(String key, Date value){
		dbObject.put(key, value);
	}
	
	public void put(String key, List<?> list){
		dbObject.put(key, list);
	}
	
	public void put(String key, boolean value) {
		dbObject.put(key, value);
	}

	public String getString(String key){
		return (String)dbObject.get(key);
	}
	
	public int getInt(String key){
		Integer value = (Integer)dbObject.get(key);
		if (value != null)
			return value.intValue();
		else {
			return 0;
		}
	}
	
	public double getDouble(String key){
		Double value = (Double)dbObject.get(key);
		return value.doubleValue();		
	}
	
	public float getFloat(String key){
	    Float value = (Float)dbObject.get(key);
        return value.floatValue();     
    }
	
	public Date getDate(String key){
		Date value = (Date)dbObject.get(key);
		return value;		
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getStringList(String key){
		return (List<String>)dbObject.get(key);		
	}
	
	public Object getObject(String key){
		return dbObject.get(key);
	}
	
	public String getStringObjectId(){
		return dbObject.get("_id").toString();
	}
	
	public ObjectId getObjectId(){
		return (ObjectId)dbObject.get("_id");
	}
	
	public JSONObject toJsonObject() {
		JSONObject object = new JSONObject();
		object.putAll(dbObject.toMap());
		return object;
	}
}
