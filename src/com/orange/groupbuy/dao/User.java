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
			String keywords, double maxPrice, double minRebate) {
		
		BasicDBObject item = new BasicDBObject();		
		
		if (!StringUtil.isEmpty(categoryName))
			item.put(DBConstants.F_CATEGORY_NAME, categoryName);
		if (!StringUtil.isEmpty(subCategoryName))
			item.put(DBConstants.F_SUB_CATEGORY_NAME, subCategoryName);
		if (!StringUtil.isEmpty(keywords))
			item.put(DBConstants.F_KEYWORD, keywords);
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
	
	
}
