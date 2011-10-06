package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;
import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.utils.DateUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Gps;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.ProductAddress;
import com.orange.common.solr.SolrClient;
import com.sun.xml.internal.bind.v2.runtime.NameList;

public class ProductManager extends CommonManager {

    public static boolean isProductExist(MongoDBClient mongoClient, String productURL, String city) {
        Map<String, String> fieldValues = new HashMap<String, String>();
        fieldValues.put(DBConstants.F_LOC, productURL);
        fieldValues.put(DBConstants.F_CITY, city);
        return (mongoClient.findOne(DBConstants.T_PRODUCT, fieldValues) != null);
    }

    public static boolean createProduct(MongoDBClient mongoClient, Product product) {

        // String loc = product.getLoc();
        // String city = product.getCity();
        // if (isProductExist(mongoClient, loc, city))
        // return false;
        product.calculateRebate();
        boolean result = mongoClient.insert(DBConstants.T_PRODUCT, product.getDbObject());
        if (!result)
            return false;

        // insert address into product address index table
        List<String> addressList = product.getAddress();
        List<List<Double>> gpsList = product.getGPS();
        if (addressList == null)
            return true;

        String productId = product.getId();
        String city = product.getCity();
        int i = 0;
        int gpsLen = (gpsList == null) ? 0 : gpsList.size();
        boolean needUpdate = false;
        for (String addr : addressList) {
            if (addr == null)
                continue;

            List<Double> gps = null;
            if (gpsList != null && i < gpsLen)
                gps = gpsList.get(i);

            ProductAddress address = AddressManager.findAddress(mongoClient, addr);
            if (address == null) {
                // log.info("<debug> create new address for productId "+productId+", address "+addr);
                AddressManager.createAddress(mongoClient, productId, addr, city, gps);
            } else {
                // address found, check GPS data
                List<Double> gpsInAddress = address.getGPS();
                if (gpsInAddress != null && gpsInAddress.size() > 0) {
                    // log.info("<debug> address "+addr+" found, gps=("+gpsInAddress.get(0)+","+gpsInAddress.get(1)+"), update product "+product.getId());
                    product.addGPS(gpsInAddress);
                    needUpdate = true;
                } else {
                    // append productId in existing list
                    // log.info("<debug> append productId "+productId+" into address "+addr);
                    address.addProductId(productId);
                    mongoClient.save(DBConstants.T_IDX_PRODUCT_GPS, address.getDbObject());
                }
            }

            i++;
        }

        if (needUpdate) {
            mongoClient.save(DBConstants.T_PRODUCT, product.getDbObject());
        }

        return true;
    }

    public static void createSolrIndex(Product product, boolean commitNow) {

        try {

            CommonsHttpSolrServer server = SolrClient.getSolrServer();

            SolrInputDocument doc = new SolrInputDocument();
            doc.addField(DBConstants.F_INDEX_ID, product.getId(), 1.0f);
            doc.addField(DBConstants.F_TITLE, product.getTitle(), 2.0f);
            doc.addField(DBConstants.F_CITY, product.getCity(), 1.0f);
            Date startDate = product.getStartDate();
            long startDateLong = startDate.getTime();
            doc.addField(DBConstants.F_START_DATE, startDateLong, 1.0f);
            Date endDate = product.getEndDate();
            long endDateLong = endDate.getTime();
            doc.addField(DBConstants.F_END_DATE, endDateLong, 1.0f);
            int category = product.getCategory();
            doc.addField(DBConstants.F_CATEGORY, category, 1.0f);
            double price = product.getPrice();
            doc.addField(DBConstants.F_PRICE, price, 1.0f);
            long bought = product.getBought();
            doc.addField(DBConstants.F_BOUGHT, bought, 1.0f);
            // TODO rem for deploy in server
            int cnt = 1;
            List<List<Double>> list = product.getGPS();
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    Gps gps = Gps.fromObject(list.get(i));
                    if (gps != null) {
                        String gpsString = gps.toString();
                        String gpsField = DBConstants.F_GPS + "_" + cnt + "_p";
                        doc.addField(gpsField, gpsString);
                        /*if (cnt == 1) {
                            String gpsFieldSingal = DBConstants.F_GPS;
                            doc.addField(gpsFieldSingal, gpsString);
                        }*/
                        cnt++;
                    }
                }
            }

            String description = product.getDescription();
            String detail = product.getDetail();
            List<String> shopList = product.getShop();
            List<String> addressList = product.getAddress();
            List<String> tagList = product.getTag();
            String s_name = product.getSiteName();
            if (description != null)
                doc.addField(DBConstants.F_DESCRIPTION, description, 1.5f);
            if (detail != null)
                doc.addField(DBConstants.F_DETAIL, detail, 1.5f);
            if (s_name != null)
                doc.addField(DBConstants.F_SITE_NAME, s_name, 1.0f);
            if (shopList != null && shopList.size() > 0)
                doc.addField(DBConstants.F_SHOP, shopList, 1.0f);
            // TODO how to change the weight
            if (addressList != null && addressList.size() > 0)
                doc.addField(DBConstants.F_ADDRESS, addressList, 0.2f);

            if (tagList != null && tagList.size() > 0)
                doc.addField(DBConstants.F_TAG, tagList, 1.0f);

            String siteId = product.getSiteId();
            if (siteId != null)
                doc.addField(DBConstants.F_SITE_ID, siteId);

            Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
            docs.add(doc);
            log.info("<createSolrIndex> doc=" + doc.toString());

            server.add(docs);

        } catch (Exception e) {
            log.error("<createSolrIndex> but catch exception = " + e.toString(), e);
        }
    }


    public static Product findProduct(MongoDBClient mongoClient, String productURL, String city) {

        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_LOC, productURL);

        if (!city.equals(DBConstants.C_NATIONWIDE)) {
            List<String> cityList = new ArrayList<String>();
            cityList.add(city);
            cityList.add(DBConstants.C_NATIONWIDE);
            BasicDBObject in = new BasicDBObject();
            in.put("$in", cityList);
            query.put(DBConstants.F_CITY, in);
        }

        DBObject obj = mongoClient.findOne(DBConstants.T_PRODUCT, query);
        if (obj != null) {
            Product product = new Product(obj);
            return product;
        } else {
            return null;
        }
    }

    public static void save(MongoDBClient mongoClient, Product product) {
        product.calculateRebate();
        mongoClient.save(DBConstants.T_PRODUCT, product.getDbObject());
    }

    public static List<?> findProductByCategory(MongoDBClient mongoClient, String city, int category) {
        return null;
    }

    private static List<Product> getAllProductsWithField(MongoDBClient mongoClient, String fieldName, String city,
            boolean sortAscending, String startOffset, String maxCount) {

        int count = getMaxcount(maxCount);
        int offset = getOffset(startOffset);
        List<String> cityList = null;
        if (city != null && city.trim().length() > 0) {
            cityList = new ArrayList<String>();
            cityList.add(city);
            if (!city.equals(DBConstants.C_NATIONWIDE)) {
                cityList.add(DBConstants.C_NATIONWIDE);
            }
        }
        DBCursor cursor = mongoClient.findByFieldInValues(DBConstants.T_PRODUCT, DBConstants.F_CITY, cityList,
                fieldName, sortAscending, offset, count);
        List<Product> list = getProduct(cursor);
        cursor.close();
        return list;

    }

    private static boolean addCityIntoQuery(DBObject query, String city) {
        if (city != null && city.length() > 0) {
            List<String> cityList = new ArrayList<String>();
            cityList.add(city);
            if (!city.equals(DBConstants.C_NATIONWIDE)) {
                cityList.add(DBConstants.C_NATIONWIDE);
            }

            DBObject in = new BasicDBObject();
            in.put("$in", cityList);
            query.put(DBConstants.F_CITY, in);
        }

        return true;
    }

    private static boolean addCategoryIntoQuery(DBObject query, List<Integer> categoryList) {
        if (categoryList != null && categoryList.size() > 0) {
            DBObject in = new BasicDBObject();
            in.put("$in", categoryList);
            query.put(DBConstants.F_CATEGORY, in);
        }

        return true;
    }

    // only query those product which are not expired
    private static boolean addExpirationIntoQuery(DBObject query) {
        Date date = new Date();
        DBObject endDateCondition = new BasicDBObject();
        endDateCondition.put("$gte", date);
        query.put(DBConstants.F_END_DATE, endDateCondition);

        return true;
    }

    private static DBObject createDBObjectForRegexQuery(String fieldName, String expression) {

        DBObject regexCondition = new BasicDBObject();
        regexCondition.put("$regex", expression);

        DBObject obj = new BasicDBObject();
        obj.put(fieldName, regexCondition);
        return obj;
    }

    private static boolean addKeywordIntoQuery(DBObject query, String[] keywordList) {

        if (keywordList == null || keywordList.length == 0)
            return false;

        StringBuilder builder = new StringBuilder();
        int len = keywordList.length;
        for (int i = 0; i < len; i++) {
            if (keywordList[i] != null && keywordList[i].trim().length() > 0) {
                builder.append("(?=.*?");
                builder.append(keywordList[i].trim());
                builder.append(")");
            }
        }
        String expression = builder.toString();

        // add fields for query
        BasicDBList array = new BasicDBList();
        array.add(createDBObjectForRegexQuery(DBConstants.F_TITLE, expression));
        array.add(createDBObjectForRegexQuery(DBConstants.F_DESC, expression));
        array.add(createDBObjectForRegexQuery(DBConstants.F_DETAIL, expression));
        array.add(createDBObjectForRegexQuery(DBConstants.F_TAG, expression));

        query.put("$or", array);
        return true;
    }

    private static boolean addGpsIntoQuery(DBObject query, double longitude, double latitude, double maxDistance) {

        List<Double> gpsList = new ArrayList<Double>();
        gpsList.add(latitude);
        gpsList.add(longitude);

        // DBObject near = new BasicDBObject();
        // near.put("$near", gpsList);
        // near.put("$maxDistance", maxDistance);
        // query.put(DBConstants.F_GPS, near);

        DBObject within = new BasicDBObject();
        DBObject center = new BasicDBObject();
        BasicBSONList centerObj = new BasicBSONList();
        centerObj.add(gpsList);
        centerObj.add(maxDistance);

        center.put("$center", centerObj);
        within.put("$within", center);
        query.put(DBConstants.F_GPS, within);

        return true;
    }

    // only query those product which starts from today
    private static boolean addTodayIntoQuery(DBObject query) {
        Date date = DateUtil.getDateOfToday();
        DBObject startDateCondition = new BasicDBObject();
        startDateCondition.put("$gte", date);
        query.put(DBConstants.F_START_DATE, startDateCondition);
        return true;
    }

    private static boolean addNonZeroPriceIntoQuery(DBObject query) {
        DBObject priceCondition = new BasicDBObject();
        priceCondition.put("$gt", 0.0f);
        query.put(DBConstants.F_PRICE, priceCondition);
        return true;
    }

    private static void addFieldIntoOrder(DBObject orderBy, String fieldName, boolean sortAscending) {
        if (sortAscending) {
            orderBy.put(fieldName, 1);
        } else {
            orderBy.put(fieldName, -1);
        }
    }

    public static void addPriceIntoOrder(DBObject orderBy, boolean sortAscending) {
        addFieldIntoOrder(orderBy, DBConstants.F_PRICE, sortAscending);
    }

    public static void addRebateIntoOrder(DBObject orderBy, boolean sortAscending) {
        addFieldIntoOrder(orderBy, DBConstants.F_REBATE, sortAscending);
    }

    public static void addTopScoreIntoOrder(DBObject orderBy, boolean sortAscending) {
        addFieldIntoOrder(orderBy, DBConstants.F_TOP_SCORE, sortAscending);
    }

    public static void addBoughtIntoOrder(DBObject orderBy, boolean sortAscending) {
        addFieldIntoOrder(orderBy, DBConstants.F_BOUGHT, sortAscending);
    }

	public static List<Product> getAllProductsWithLocation1(
			MongoDBClient mongoClient, String latitude, String longitude,
			String startOffset, String maxCount) {

		int count = getMaxcount(maxCount);
		int offset = getOffset(startOffset);

		double latitudeD = getLatitude(latitude);
		double longitudeD = getLongitude(longitude);

		DBCursor idCursor = mongoClient.findNearby(
				DBConstants.T_IDX_PRODUCT_GPS, DBConstants.F_GPS, latitudeD,
				longitudeD, offset, count);

		if (idCursor == null || idCursor.size() < 1) {
			return null;
		}

		List<Object> productIdList = new ArrayList<Object>();
		while (idCursor.hasNext()) {
			DBObject productObject = idCursor.next();
			Object productId = productObject.get(DBConstants.F_PRODUCTID);
			if (productId != null) {
				productIdList.add(productId);
			}
		}

		idCursor.close();

		if (productIdList == null || productIdList.size() < 1) {
			return null;
		}

		DBCursor productCursor = mongoClient.findByFieldInValues(
				DBConstants.T_PRODUCT, DBConstants.F_ID, productIdList, offset,
				count);

		List<Product> productList = getProduct(productCursor);
		productCursor.close();

		if (productList == null || productList.size() < 1) {
			return null;
		}
		return sortByProductId1(productIdList, productList);
	}

	private static List<Product> sortByProductId1(List<Object> productIdList,
			List<Product> productList) {
		Map<Object, Product> map = new HashMap<Object, Product>();
		for (Product product : productList) {
			map.put(product.getObjectId(), product);
		}
		List<Product> products = new ArrayList<Product>();
		for (Object id : productIdList) {
			if (map.containsKey(id)) {
				products.add(map.get(id));
				map.remove(id);
			}
		}
		return products;
	}

	public static List<Product> getAllProductsByCategory1(
			MongoDBClient mongoClient, String city, String category,
			String startOffset, String maxCount) {

		if (category == null)
			return null;

		List<String> list = new LinkedList<String>();
		list.add(category);
		return getAllProductsWithCategory(mongoClient, city, list, startOffset,
				maxCount);
	}
	
	public static Long getProductsNumberByCategory(MongoDBClient mongoClient, String category, String city) {
	    if (category == null)
            return null;
	    
	    DBObject query = new BasicDBObject();
        query.put(DBConstants.F_CATEGORY, Integer.parseInt(category));
        addExpirationIntoQuery(query);
        addCityIntoQuery(query, city);
        return mongoClient.count(DBConstants.T_PRODUCT, query);
	}	

	public static List<Product> getAllProductsGroupByCategory(
			MongoDBClient mongoClient, String city, String topCount) {

		return null;
	}

	public static List<String> getAllCategoryNames() {
		List<String> categoryList = new ArrayList<String>();
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_EAT));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_FACE));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_FUN));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_SHOPPING));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_KEEPFIT));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_LIFE));
		categoryList.add(String.valueOf(DBConstants.C_CATEGORY_NAME_UNKNOWN));
		return categoryList;
	}

	public static List<Product> searchProductBySolr() {
		return null;
	}

	public static List<Product> searchProductByMongoDB(
			MongoDBClient mongoClient, String city, List<Integer> categoryList,
			boolean todayOnly, String[] keywordList, int startOffset,
			int maxCount) {

		DBObject query = new BasicDBObject();
		DBObject orderBy = new BasicDBObject();

		// set query
		addCityIntoQuery(query, city);
		addCategoryIntoQuery(query, categoryList);
		addExpirationIntoQuery(query);
		if (todayOnly) {
			addTodayIntoQuery(query);
		}

		addKeywordIntoQuery(query, keywordList);

		log.info("search product, query=" + query.toString());

		DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query,
				orderBy, startOffset, maxCount);
		return getProduct(cursor);
	}

	public static List<Product> searchProductBySolr(SolrClient solrClient,
			MongoDBClient mongoClient, String city, List<Integer> categoryList,
			boolean todayOnly, String keyword, Double price, int startOffset, int maxCount) {

		SolrQuery query = new SolrQuery();
		if (keyword == null || keyword.isEmpty()){
		    log.warn("<searchProductBySolr> but keyword is null or empty");
			return null;
		}
		query.setQuery(keyword);

		if (city != null && !city.isEmpty()) {
		    if (city.equalsIgnoreCase(DBConstants.C_NATIONWIDE)) {
		        addOrIntoFilterQuery(query, DBConstants.F_CITY, city);
		    }
		    else {
                addOrIntoFilterQuery(query, DBConstants.F_CITY, city, DBConstants.C_NATIONWIDE);		        
		    }
		}
		
		long dateLong = new Date().getTime();
		String dateString = String.valueOf(dateLong);
		addRangeIntoFilterQuery(query, DBConstants.F_END_DATE, dateString, null);

		if (todayOnly) {
			Date todayDate = DateUtil.getDateOfToday();
			// long dateInc = 3600 * 1000 * 24 * 0;
			String todayDateString = String.valueOf(todayDate.getTime());
			addRangeIntoFilterQuery(query, DBConstants.F_START_DATE, todayDateString, null);
		}
		
		if (categoryList != null && categoryList.size() > 0) {
			//"myField:(id1 OR id2 OR id3)"	
		    addOrIntoFilterQuery(query, DBConstants.F_CATEGORY, categoryList);
		}
		
		if (maxCount > 0)
			query.setRows(maxCount);
		if (startOffset >= 0)
			query.setStart(startOffset);

		if (price != null && price.doubleValue() > 0.0f) {
		    String priceString = String.valueOf(price.doubleValue());
		    addRangeIntoFilterQuery(query, DBConstants.F_PRICE, "-100.0", priceString);
		}
		
		query.set("fl", "score, id, price");
		
		log.info("<searchProductBySolr> query=" + query.toString());

		CommonsHttpSolrServer server = SolrClient.getSolrServer();
		QueryResponse rsp;
		try {
			rsp = server.query(query);
			
			if (rsp == null)
				return null;

			SolrDocumentList resultList = rsp.getResults();
			if (resultList == null)
				return null;

			Iterator<SolrDocument> iter = resultList.iterator();
			List<ObjectId> objectIdList = new ArrayList<ObjectId>();
			Map <ObjectId,Float> productScoreMap = new HashMap<ObjectId,Float>();
			while (iter.hasNext()) {
				SolrDocument resultDoc = iter.next();
				
				String productId = (String) resultDoc
						.getFieldValue(DBConstants.F_INDEX_ID);
				Float productScore = (Float) resultDoc
						.getFieldValue("score");
//				String productTitle =  (String) resultDoc.getFieldValue(DBConstants.F_TITLE);			
//				log.info("<search> id="+productId+",score="+productScore+",title="+productTitle);

				ObjectId objectId = new ObjectId(productId);
				objectIdList.add(objectId);
				
				productScoreMap.put(objectId, productScore);
				
				log.debug("<searchProductBySolr> result doc="+ resultDoc.toString());
			}
			log.info("<searchProductBySolr> search done, result size = " + resultList.size());

			if (objectIdList == null || objectIdList.size() == 0)
				return null;
			// convert to product
			DBCursor dbCursor = mongoClient.findByIds(DBConstants.T_PRODUCT,
					DBConstants.F_ID, objectIdList);
			if (dbCursor == null)
				return null;
			List<Product> productList = getProduct(dbCursor);
			List<Product> orderedProductList = new ArrayList<Product>();
			int i;
			int size = productList.size();
			for (ObjectId objectId : objectIdList) {
				i = 0;
				Product product = productList.get(i);
				Float score = productScoreMap.get(objectId);
				
				while (!product.getObjectId().equals(objectId)
						&& (size > (++i))) {
					product = productList.get(i);
				}
				if (i < size){
				    product.setScore(score);
				    orderedProductList.add(product);
				}
			}
			
			dbCursor.close();
			return orderedProductList;

		} catch (SolrServerException e) {
			log.error("<searchProductBySolr> catch exception="+e.toString()+","+e.getMessage(), e);
			return null;
		}
	}
	
	public static List<Product> searchProductBySolr(SolrClient solrClient,
            MongoDBClient mongoClient, String city, List<Integer> categoryList,
            boolean todayOnly, String keyword, int startOffset, int maxCount) {
	    
	    List<Product> list = searchProductBySolr(solrClient, mongoClient, city, categoryList, todayOnly, keyword, null, startOffset, maxCount);
	    return list;
	}

	public static void incActionCounter(MongoDBClient mongoClient,
			String productId, String actionName, int actionValue) {

	    ObjectId id = new ObjectId(productId);
		mongoClient.inc(DBConstants.T_PRODUCT, DBConstants.F_ID, id,
				actionName, actionValue);
	}

    public static DBCursor getProductCursor(MongoDBClient mongoClient, String city, List<Integer> categoryList,
            boolean todayOnly, boolean gpsQuery, double latitude, double longitude, double maxDistance, int sortBy,
            int startOffset, int maxCount) {

        DBObject query = new BasicDBObject();
        DBObject orderBy = new BasicDBObject();

        // set query
        addCityIntoQuery(query, city);
        addCategoryIntoQuery(query, categoryList);
        addExpirationIntoQuery(query);
        if (gpsQuery) {
            double degreeDistance = maxDistance * 1000 / 6371;
            addGpsIntoQuery(query, longitude, latitude, degreeDistance);
        }

        if (todayOnly) {
            addTodayIntoQuery(query);
        }

        // set order by
        // if (!gpsQuery){
        switch (sortBy) {
        case DBConstants.SORT_BY_PRICE:
            addFieldIntoOrder(orderBy, DBConstants.F_PRICE, true);
            break;

        case DBConstants.SORT_BY_REBATE: {
            addNonZeroPriceIntoQuery(query);
            addFieldIntoOrder(orderBy, DBConstants.F_REBATE, true);
        }
            break;

        case DBConstants.SORT_BY_BOUGHT: {
            addNonZeroPriceIntoQuery(query);
            addFieldIntoOrder(orderBy, DBConstants.F_BOUGHT, false);
        }
            break;
        }
        addFieldIntoOrder(orderBy, DBConstants.F_START_DATE, false);
        // }

        log.info("<getProducts> query = " + query.toString() + " , orderBy = " + orderBy + " startOffset = "
                + startOffset + ", maxCount = " + maxCount);

        DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query, orderBy, startOffset, maxCount);
        return cursor;
        // TODO 
//        cursor.count();
//        return getProduct(cursor);
    }
    
    public static List<Product> getProducts(MongoDBClient mongoClient, String city, List<Integer> categoryList,
            boolean todayOnly, boolean gpsQuery, double latitude, double longitude, double maxDistance, int sortBy,
            int startOffset, int maxCount) {

        DBObject query = new BasicDBObject();
        DBObject orderBy = new BasicDBObject();

        // set query
        addCityIntoQuery(query, city);
        addCategoryIntoQuery(query, categoryList);
        addExpirationIntoQuery(query);
        if (gpsQuery) {
            double degreeDistance = maxDistance * 1000 / 6371;
            addGpsIntoQuery(query, longitude, latitude, degreeDistance);
        }

        if (todayOnly) {
            addTodayIntoQuery(query);
        }

        // set order by
        // if (!gpsQuery){
        switch (sortBy) {
        case DBConstants.SORT_BY_PRICE:
            addFieldIntoOrder(orderBy, DBConstants.F_PRICE, true);
            break;

        case DBConstants.SORT_BY_REBATE: {
            addNonZeroPriceIntoQuery(query);
            addFieldIntoOrder(orderBy, DBConstants.F_REBATE, true);
        }
            break;

        case DBConstants.SORT_BY_BOUGHT: {
            addNonZeroPriceIntoQuery(query);
            addFieldIntoOrder(orderBy, DBConstants.F_BOUGHT, false);
        }
            break;
        }
        addFieldIntoOrder(orderBy, DBConstants.F_START_DATE, false);
        // }

        log.info("<getProducts> query = " + query.toString() + " , orderBy = " + orderBy + " startOffset = "
                + startOffset + ", maxCount = " + maxCount);

        DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query, orderBy, startOffset, maxCount);
        return getProduct(cursor);
    }

    public static List<Product> getAllProductsWithPrice(MongoDBClient mongoClient, String city, boolean sortAscending,
            int startOffset, int maxCount) {

        DBObject query = new BasicDBObject();
        DBObject orderBy = new BasicDBObject();

        // set query
        addCityIntoQuery(query, city);
        addExpirationIntoQuery(query);

        // set order by
        addPriceIntoOrder(orderBy, true);

        DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query, orderBy, startOffset, maxCount);
        return getProduct(cursor);
    }

    public static List<Product> getAllProductsWithBought(MongoDBClient mongoClient, String city, boolean sortAscending,
            String startOffset, String maxCount) {
        List<Product> list = getAllProductsWithField(mongoClient, DBConstants.F_BOUGHT, city, sortAscending,
                startOffset, maxCount);
        return list;
    }

    public static List<Product> getAllProductsWithRebate(MongoDBClient mongoClient, String city, boolean sortAscending,
            String startOffset, String maxCount) {
        List<Product> list = getAllProductsWithField(mongoClient, DBConstants.F_REBATE, city, sortAscending,
                startOffset, maxCount);
        return list;
    }

    public static List<Product> getProduct(DBCursor cursor) {
        if (cursor == null) {
            return null;
        }

        if (cursor.size() == 0) {
            cursor.close();
            return null;
        }

        List<Product> productList = new ArrayList<Product>();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            if (obj != null) {
                Product product = new Product(obj);
                productList.add(product);
            }
        }

        cursor.close();
        return productList;
    }

    public static List<Product> getAllProductsWithLocation(MongoDBClient mongoClient, String latitude,
            String longitude, String startOffset, String maxCount) {

        int count = getMaxcount(maxCount);
        int offset = getOffset(startOffset);

        double latitudeD = getLatitude(latitude);
        double longitudeD = getLongitude(longitude);

        DBCursor idCursor = mongoClient.findNearby(DBConstants.T_IDX_PRODUCT_GPS, DBConstants.F_GPS, latitudeD,
                longitudeD, offset, count);

        if (idCursor == null || idCursor.size() < 1) {
            return null;
        }

        List<Object> productIdList = new ArrayList<Object>();
        while (idCursor.hasNext()) {
            DBObject productObject = idCursor.next();
            Object productId = productObject.get(DBConstants.F_PRODUCTID);
            if (productId != null) {
                productIdList.add(productId);
            }
        }

        idCursor.close();

        if (productIdList == null || productIdList.size() < 1) {
            return null;
        }

        DBCursor productCursor = mongoClient.findByFieldInValues(DBConstants.T_PRODUCT, DBConstants.F_ID,
                productIdList, offset, count);

        List<Product> productList = getProduct(productCursor);
        productCursor.close();

        if (productList == null || productList.size() < 1) {
            return null;
        }
        return sortByProductId1(productIdList, productList);
    }

    private static List<Product> sortByProductId(List<Object> productIdList, List<Product> productList) {
        Map<Object, Product> map = new HashMap<Object, Product>();
        for (Product product : productList) {
            map.put(product.getObjectId(), product);
        }
        List<Product> products = new ArrayList<Product>();
        for (Object id : productIdList) {
            if (map.containsKey(id)) {
                products.add(map.get(id));
                map.remove(id);
            }
        }
        return products;
    }

    public static List<Product> getAllProductsByCategory(MongoDBClient mongoClient, String city, String category,
            String startOffset, String maxCount) {

        if (category == null)
            return null;

        List<String> list = new LinkedList<String>();
        list.add(category);
        return getAllProductsWithCategory(mongoClient, city, list, startOffset, maxCount);
    }

    public static List<Product> getAllProductsWithCategory(MongoDBClient mongoClient, String city,
            List<String> categoryList, String startOffset, String maxCount) {

        List<Object> cityList = null;
        int count = getMaxcount(maxCount);
        int offset = getOffset(startOffset);

        if (city != null && city.trim().length() > 0) {
            cityList = new ArrayList<Object>();
            cityList.add(city);
            if (!city.equals(DBConstants.C_NATIONWIDE)) {
                cityList.add(DBConstants.C_NATIONWIDE);
            }
        }

        List<Object> categories = null;
        if (categoryList != null && categoryList.size() > 0) {
            categories = new ArrayList<Object>();
            for (String category : categoryList) {
                Integer categoryInteger = Integer.valueOf(category);
                categories.add(categoryInteger);
            }
        }
        Map<String, List<Object>> map = new HashMap<String, List<Object>>();
        if (cityList != null && cityList.size() > 0) {
            map.put(DBConstants.F_CITY, cityList);
        }
        if (categories != null && categories.size() > 0) {
            map.put(DBConstants.F_CATEGORY, categories);
        }

        DBCursor cursor = mongoClient.findByFieldsInValues(DBConstants.T_PRODUCT, map, offset, count);
        List<Product> list = getProduct(cursor);
        return list;
    }

    public static List<String> getAllCategories() {
        List<String> categoryList = new ArrayList<String>();
        categoryList.add(String.valueOf(DBConstants.C_CATEGORY_EAT));
        categoryList.add(String.valueOf(DBConstants.C_CATEGORY_FACE));
        categoryList.add(String.valueOf(DBConstants.C_CATEGORY_FUN));
        categoryList.add(String.valueOf(DBConstants.C_CATEGORY_SHOPPING));
        categoryList.add(String.valueOf(DBConstants.C_CATEGORY_KEEPFIT));
        categoryList.add(String.valueOf(DBConstants.C_CATEGORY_LIFE));
        categoryList.add(String.valueOf(DBConstants.C_CATEGORY_UNKNOWN));
        return categoryList;
    }


    public static SolrDocumentList searchProductBySolr(SolrClient solrClient, MongoDBClient mongoClient, String city,
            List<Integer> categoryList, boolean todayOnly, String keyword, Double price, Double lat, Double lng,
            Double radius, int startOffset, int maxCount) {

        SolrQuery query = new SolrQuery();
        if (keyword == null || keyword.isEmpty())
            return null;
        query.setQuery(keyword);

        if (city != null && !city.isEmpty()) {
            if (city.equalsIgnoreCase(DBConstants.C_NATIONWIDE)) {
                addOrIntoFilterQuery(query, DBConstants.F_CITY, city);
            } else {
                addOrIntoFilterQuery(query, DBConstants.F_CITY, city, DBConstants.C_NATIONWIDE);
            }
        }

        long dateLong = new Date().getTime();
        String dateString = String.valueOf(dateLong);
        addRangeIntoFilterQuery(query, DBConstants.F_END_DATE, dateString, null);

        if (todayOnly) {
            Date todayDate = DateUtil.getDateOfToday();
            // long dateInc = 3600 * 1000 * 24 * 0;
            String todayDateString = String.valueOf(todayDate.getTime());
            addRangeIntoFilterQuery(query, DBConstants.F_START_DATE, todayDateString, null);
        }

        if (categoryList != null && categoryList.size() > 0) {
            // "myField:(id1 OR id2 OR id3)"
            addOrIntoFilterQuery(query, DBConstants.F_CATEGORY, categoryList);
        }

        if (maxCount > 0)
            query.setRows(maxCount);
        if (startOffset >= 0)
            query.setStart(startOffset);

        if (price != null) {
            String priceString = String.valueOf(price.doubleValue());
            addRangeIntoFilterQuery(query, DBConstants.F_PRICE, "-100.0", priceString);
        }
        
        // search location
        if (lat != null && lng != null && radius != null) {
           query.addFilterQuery("{!geofilt}"); 
           query.set("sfield", "gps_1_p"); 
           query.set("pt", lat + "," + lng); 
           query.set("d", radius+"");       
        }

        query.set("fl", "score, id, price");

        log.info("<searchProductBySolr> query=" + query.toString());

        CommonsHttpSolrServer server = SolrClient.getSolrServer();
        QueryResponse rsp;
        try {
            rsp = server.query(query);
            if (rsp == null)
                return null;
            
            SolrDocumentList resultList = rsp.getResults();
            return resultList;
        } catch (SolrServerException e) {
            e.printStackTrace();
            log.error("<searchProductBySolr> catch exception=" + e.toString() + "," + e.getMessage());
            return null;
        }
    }
    
    public static long getResultCnt(SolrDocumentList resultList) {
            return resultList.getNumFound();
    }
    
    public static List<Product> getResultList(SolrDocumentList resultList, MongoDBClient mongoClient)        
    {
            if (resultList == null)
                return null;

            Iterator<SolrDocument> iter = resultList.iterator();
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            Map<ObjectId, Float> productScoreMap = new HashMap<ObjectId, Float>();
            while (iter.hasNext()) {
                SolrDocument resultDoc = iter.next();

                String productId = (String) resultDoc.getFieldValue(DBConstants.F_INDEX_ID);
                Float productScore = (Float) resultDoc.getFieldValue("score");

                ObjectId objectId = new ObjectId(productId);
                objectIdList.add(objectId);

                productScoreMap.put(objectId, productScore);

                log.info("<searchProductBySolr> result doc=" + resultDoc.toString());
            }
            log.info("<searchProductBySolr> search done, result size = " + resultList.size());

            if (objectIdList == null || objectIdList.size() == 0)
                return null;
            // convert to product
            DBCursor dbCursor = mongoClient.findByIds(DBConstants.T_PRODUCT, DBConstants.F_ID, objectIdList);
            if (dbCursor == null)
                return null;
            List<Product> productList = getProduct(dbCursor);
            List<Product> orderedProductList = new ArrayList<Product>();
            int i;
            int size = productList.size();
            for (ObjectId objectId : objectIdList) {
                i = 0;
                Product product = productList.get(i);
                Float score = productScoreMap.get(objectId);

                while (!product.getObjectId().equals(objectId) && (size > (++i))) {
                    product = productList.get(i);
                }
                if (i < size) {
                    product.setScore(score);
                    orderedProductList.add(product);
                }
            }

            dbCursor.close();
            return orderedProductList;
    }

    public static List<Product> searchProductBySolr(SolrClient solrClient, MongoDBClient mongoClient, String city,
            List<Integer> categoryList, boolean todayOnly, String keyword, Double lat, Double lng, Double radius, int startOffset, int maxCount) {
        
        SolrDocumentList resultList = searchProductBySolr(solrClient, mongoClient, city, categoryList, todayOnly, keyword, null,
                lat, lng, radius, startOffset, maxCount);
        List<Product> list = getResultList(resultList, mongoClient);
        
        return list;
    }
    

    public static Product findProductById(MongoDBClient mongoClient, String productId) {

        ObjectId id = new ObjectId(productId);
        DBObject dbObject = mongoClient.findOne(DBConstants.T_PRODUCT, DBConstants.F_ID, id);
        if (dbObject == null)
            return null;
        
        return new Product(dbObject);
    }

    public static void writeCommet(MongoDBClient mongoClient, String productId, String userId, String nickName,
            String content, Date date) {
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_ID, new ObjectId(productId));

        BasicDBObject item = new BasicDBObject();
        item.put(DBConstants.F_USERID, userId);
        item.put(DBConstants.F_NICKNAME, nickName);
        item.put(DBConstants.F_COMMENT_CONTENT, content);
        item.put(DBConstants.F_CREATE_DATE, date);

        BasicDBObject pushValue = new BasicDBObject();
        pushValue.put(DBConstants.F_COMMENTS, item);

        BasicDBObject update = new BasicDBObject();
        update.put("$push", pushValue);

        mongoClient.updateOrInsert(DBConstants.T_PRODUCT, query, update);
    }

    public static List<Product> getTopScoreProducts(MongoDBClient mongoClient, String city, int category,
            int startOffset, int maxCount, int startPrice, int endPrice) {
        DBObject query = new BasicDBObject();
        DBObject orderBy = new BasicDBObject();

        // set query
        addCityIntoQuery(query, city);
        addExpirationIntoQuery(query);
        if (category != -1) {
            List<Integer> categoryList = new ArrayList<Integer>();
            categoryList.add(category);
            addCategoryIntoQuery(query, categoryList);
        }
        addPriceRangeIntoQuery(query, startPrice, endPrice);
        addTopScoreIntoOrder(orderBy, false);

        log.info("<getTopScoreProducts> query = " + query.toString() + " , orderBy = " + orderBy + " startOffset = "
                + startOffset + ", maxCount = " + maxCount);

        DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query, orderBy, startOffset, maxCount);
        return getProduct(cursor);
    }
    
    public static DBCursor getTopScoreProductCursor(MongoDBClient mongoClient, String city, int category,
            int startOffset, int maxCount, int startPrice, int endPrice) {
        DBObject query = new BasicDBObject();
        DBObject orderBy = new BasicDBObject();

        // set query
        addCityIntoQuery(query, city);
        addExpirationIntoQuery(query);
        if (category != -1) {
            List<Integer> categoryList = new ArrayList<Integer>();
            categoryList.add(category);
            addCategoryIntoQuery(query, categoryList);
        }
        addPriceRangeIntoQuery(query, startPrice, endPrice);
        addTopScoreIntoOrder(orderBy, false);

        log.info("<getTopScoreProducts> query = " + query.toString() + " , orderBy = " + orderBy + " startOffset = "
                + startOffset + ", maxCount = " + maxCount);

        DBCursor cursor = mongoClient.find(DBConstants.T_PRODUCT, query, orderBy, startOffset, maxCount);
        return cursor;

    }

    private static void addPriceRangeIntoQuery(DBObject query, int startPrice, int endPrice) {
        DBObject priceCondition = new BasicDBObject();
        priceCondition.put("$gte", startPrice);
        priceCondition.put("$lt", endPrice);
        query.put(DBConstants.F_PRICE, priceCondition);
    }

    private static void addRangeIntoFilterQuery(SolrQuery solrQuery, String field, String start, String end) {
        if (field == null || solrQuery == null)
            return;

        String query = field.concat(":[");
        if (start == null) {
            query = query.concat("* TO ");
        } else {
            query = query.concat(start).concat(" TO ");
        }
        if (end == null) {
            query = query.concat("*]");
        } else {
            query = query.concat(end).concat("]");
        }

        solrQuery.addFilterQuery(query);
    }

    private static void addOrIntoFilterQuery(SolrQuery solrQuery, String field, List<?> list) {
        if (solrQuery == null || list == null || list.size() == 0)
            return;

        // "myField:(id1 OR id2 OR id3)"
        int size = list.size();
        String query = field + ":";
        String temp = "";
        for (int i = 0; i < size; i++) {
            if (i == size - 1)
                temp = temp.concat("" + list.get(i));
            else
                temp = temp.concat(list.get(i) + " OR ");
        }
        if (size == 1)
            query = query.concat(temp);
        else
            query = query.concat("(").concat(temp).concat(")");

        solrQuery.addFilterQuery(query);
    }

    private static void addOrIntoFilterQuery(SolrQuery solrQuery, String field, String... values) {
        if (solrQuery == null || values == null || values.length == 0)
            return;

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; i++)
            list.add(values[i]);

        addOrIntoFilterQuery(solrQuery, field, list);
    }
    
    public static int getCursorCount(DBCursor cursor) {
       return  cursor.count();
    }

}
