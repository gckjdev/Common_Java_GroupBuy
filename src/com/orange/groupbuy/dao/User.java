package com.orange.groupbuy.dao;

import java.util.Date;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;

public class User extends CommonData {

	public User(DBObject dbObject){
		super(dbObject);
	}

	public boolean addShoppingItem(String itemId, String categoryName, String subCategoryName,
			String keywords, String city, double maxPrice, double minRebate) {
		
		BasicDBObject item = new BasicDBObject();		
		
		if (!StringUtil.isEmpty(itemId))
            item.put(DBConstants.F_ITEM_ID, itemId);
		if (!StringUtil.isEmpty(categoryName))
			item.put(DBConstants.F_CATEGORY_NAME, categoryName);
		if (!StringUtil.isEmpty(subCategoryName))
			item.put(DBConstants.F_SUB_CATEGORY_NAME, subCategoryName);
		if (!StringUtil.isEmpty(keywords))
			item.put(DBConstants.F_KEYWORD, keywords);
		if (!StringUtil.isEmpty(city))
            item.put(DBConstants.F_CITY, city);
		if (maxPrice >= 0.0f)
			item.put(DBConstants.F_MAX_PRICE, maxPrice);
		if (minRebate >= 0.0f)
			item.put(DBConstants.F_MIN_REBATE, minRebate);
		
		item.put(DBConstants.F_CREATE_DATE, new Date());
		
		BasicDBList shoppingList = (BasicDBList)dbObject.get(DBConstants.F_SHOPPING_LIST);
		if (shoppingList == null){
			shoppingList = new BasicDBList();
			dbObject.put(DBConstants.F_SHOPPING_LIST, shoppingList);
		}
		
		shoppingList.add(item);				
		return true;
	}
	
	   public BasicDBList getShoppingItem() {
	       
	       BasicDBList shoppingList = (BasicDBList)dbObject.get(DBConstants.F_SHOPPING_LIST);
	       if(shoppingList != null) {
	           return shoppingList;
	       }
	       return null;
	    }
	
	
	public String getCity() {
	    return this.getString(DBConstants.F_CITY);
	}
	
	public void setCity(String city) {
	    this.put(DBConstants.F_CITY, city);
	}
	
	public String getCategory() {
        return this.getString(DBConstants.F_CATEGORY_NAME);
    }
	
	public void  setCategory(String category) {
	    this.put(DBConstants.F_CATEGORY_NAME, category);
    }
	
	public String getSubCategory() {
        return this.getString(DBConstants.F_SUB_CATEGORY_NAME);
    }
	
	public void setSubCategory(String subcategory) {
	    this.put(DBConstants.F_SUB_CATEGORY_NAME, subcategory);
	}
	
	public double getMaxPrice() {
        return StringUtil.doubleFromString(this.getString(DBConstants.F_MAX_PRICE));
    }
    
    public void setMaxPrice(String maxPrice) {
        this.put(DBConstants.F_MAX_PRICE, maxPrice);
    }
    
    public double getRebate() {
        return StringUtil.doubleFromString(this.getString(DBConstants.F_REBATE));
    }
    
    public void setRebate(String rebate) {
        this.put(DBConstants.F_REBATE, rebate);
    }
	
}
