package com.orange.groupbuy.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.cassandra.cli.CliParser.newColumnFamily_return;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.mongodb.MongoDBClient;
import com.orange.common.solr.SolrClient;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;
import com.orange.groupbuy.dao.Product;
import com.orange.groupbuy.dao.RecommendItem;
import com.orange.groupbuy.dao.User;

public class RecommendItemManager {

    public static final Logger log = Logger.getLogger(RecommendItemManager.class.getName());

    public static final int MAX_RECOMMEND_COUNT = 25;

    public static RecommendItem findAndUpsertRecommendItem(MongoDBClient mongoClient, String userId, String itemId) {

        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_ITEM_ID, itemId);

        BasicDBObject updateValue = new BasicDBObject();
        updateValue.put(DBConstants.F_FOREIGN_USER_ID, userId);
        updateValue.put(DBConstants.F_ITEM_ID, itemId);

        BasicDBObject update = new BasicDBObject();
        update.put("$set", updateValue);

        DBObject obj = mongoClient.findAndModifyInsert(DBConstants.T_RECOMMEND, query, update);
        if (obj == null) {
            return null;
        } else {
            return new RecommendItem(obj);
        }
    }

    public static RecommendItem findRecommendItem(MongoDBClient mongoClient, String userId, String itemId) {

        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_ITEM_ID, itemId);

        DBObject obj = mongoClient.findOne(DBConstants.T_RECOMMEND, query);
        if (obj == null) {
            Log.info("No recommend item found for userId=" + userId + ", itemId=" + itemId);
            return null;
        } else {
            return new RecommendItem(obj);
        }
    }

    private static String getItemArrayKey() {
        return DBConstants.F_RECOMMEND_LIST.concat(".").concat(DBConstants.F_END_DATE);
    }

    private static String getItemArrayResultKey() {
        return DBConstants.F_RECOMMEND_LIST.concat(".$");
    }

    public static boolean deleteRecommendProductList(MongoDBClient mongoClient, String userId, String itemId) {

        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_ITEM_ID, itemId);

        BasicDBObject update = new BasicDBObject();
        BasicDBObject unsetValue = new BasicDBObject();
        BasicDBObject updateValue = new BasicDBObject();
        unsetValue.put(DBConstants.F_RECOMMEND_LIST, 1); // remove the key found
        update.put("$unset", unsetValue);
        updateValue.put(DBConstants.F_RECOMMEND_COUNT, 0);
        update.put("$set", updateValue);
        mongoClient.updateAll(DBConstants.T_RECOMMEND, query, update);
        log.info("query = " + query + " update=" + update);

        // pull null
        BasicDBObject pullquery = new BasicDBObject();
        pullquery.put(DBConstants.F_USERID, userId);

        BasicDBObject pullupdate = new BasicDBObject();
        BasicDBObject pullValue = new BasicDBObject();
        pullValue.put(DBConstants.F_RECOMMEND_LIST, null);
        pullupdate.put("$pull", pullValue);
        mongoClient.updateAll(DBConstants.T_RECOMMEND, pullquery, pullupdate);

        log.info("query = " + pullquery + " update=" + pullupdate);
        return true;
    }

    public static boolean deleteRecommendItem(MongoDBClient mongoClient, String userId, String itemId) {
        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_FOREIGN_USER_ID, userId);
        query.put(DBConstants.F_ITEM_ID, itemId);

        return mongoClient.removeOne(DBConstants.T_RECOMMEND, query);
    }

    public static void matchShoppingItem(MongoDBClient mongoClient, String userId, String[] itemIdArray) {

        User user = UserManager.findUserByUserId(mongoClient, userId);
        try {

            for (int i = 0; i < itemIdArray.length; i++) {

                BasicDBObject item = UserManager.findUserShoppingItem(mongoClient, user, itemIdArray[i]);

                String city = item.getString(DBConstants.F_CITY);
                String cate = item.getString(DBConstants.F_CATEGORY_NAME);
                String subcate = item.getString(DBConstants.F_SUB_CATEGORY_NAME);
                String keyword = item.getString(DBConstants.F_KEYWORD);
                Double maxPrice = (Double) item.get(DBConstants.F_MAX_PRICE);
                Date expireDate = (Date) item.get(DBConstants.F_EXPIRE_DATE);

                if (isExpire(expireDate)) {
                    log.info("user = " + user.getUserId() + ", itemId = " + itemIdArray[i] + ",  expireDate = "
                            + expireDate);
                    continue;
                }

                String keywords = generateKeyword(cate, subcate, keyword);

                log.info("starting matching for userid =" + userId + " itemId" + itemIdArray[i]);

                List<Product> productList = ProductManager.searchProductBySolr(SolrClient.getInstance(), mongoClient,
                        city, null, false, keywords, maxPrice, 0, MAX_RECOMMEND_COUNT);

                if (productList == null || productList.size() <= 0) {
                    log.info("No product match to recommend for user=" + userId + ", itemId = " + itemIdArray[i]);
                    continue;
                }
                // create recommend item
                RecommendItem recommendItem = findAndUpsertRecommendItem(mongoClient, user.getUserId(), itemIdArray[i]);

                boolean hasChange = false;
                int addCount = 0;
                for (Product product : productList) {

                    if (RecommendItemManager.addOrUpdateProduct(recommendItem, product)) {
                        log.info("add or update product (" + product.getId() + "), score = " + product.getScore()
                                + " into recommend item = " + itemIdArray[i]);

                        hasChange = true;
                        addCount++;
                    }
                }

                log.info(productList.size() + " product found, " + addCount + " are added/updated for user=" + userId
                        + ", itemId = " + itemIdArray[i]);

                if (hasChange) {
                    mongoClient.save(DBConstants.T_RECOMMEND, recommendItem.getDbObject());
                }
            }
        } catch (Exception e) {
            log.error("<RecommendItemManager> catch Exception while running. exception=" + e.getMessage(), e);
        }
    }

    public static void deleteExpiredProduct(MongoDBClient mongoClient, String itemId) {

        BasicDBObject query = new BasicDBObject();
        query.put(DBConstants.F_ITEM_ID, itemId);
        
        BasicDBObject update = new BasicDBObject();
        BasicDBObject pullValue = new BasicDBObject();
        BasicDBObject pull = new BasicDBObject();
        
        pullValue.put(DBConstants.F_END_DATE, new BasicDBObject("$lt", new Date()));
        pull.put(DBConstants.F_RECOMMEND_LIST, pullValue);
        update.put("$pull", pull);
        
        BasicDBObject inc = new BasicDBObject();
        inc.put(DBConstants.F_RECOMMEND_COUNT, -1);
        update.put("$inc", inc);
        
        log.info("query=" + query + "update= " + update);
        mongoClient.updateAll(DBConstants.T_RECOMMEND, query, update);
     
    }

    public static boolean isProductExpired(Product product) {
        Date e_date = product.getEndDate();
        Date now = new Date();

        if (e_date == null) {
            return false;
        }
        if (e_date.before(now)) {
            return true;
        }
        return false;
    }

    private static final String SUB_CATEGORY_BOOST = "2";
    private static final String KEYWORD_BOOST = "4";
    
    public static String generateKeyword(String cate, String subcate, String kw) {

        String keywords = "";
        if (!StringUtil.isEmpty(cate)) {
            keywords = keywords.concat(" ").concat(cate);
        }
        if (!StringUtil.isEmpty(subcate)) {
            String[] list = subcate.split(" ");
            if (list != null && list.length > 0) {
                for (int i = 0; i < list.length; i++) {
                    keywords = keywords.concat(" ").concat(list[i]).concat("^").concat(SUB_CATEGORY_BOOST);
                }
            }
        }
        if (!StringUtil.isEmpty(kw)) {
            String[] list = kw.split(" ");
            if (list != null && list.length > 0) {
                for (int i = 0; i < list.length; i++) {
                    keywords = keywords.concat(" ").concat(list[i]).concat("^").concat(KEYWORD_BOOST);
                }
            }
        }

        return keywords.trim();
    }

    public static boolean isExpire(Date expireDate) {
        if (expireDate == null)
            return false;

        Date now = new Date();
        if (now.after(expireDate)) {
            return true;
        }
        return false;
    }

    public static List<Product> getRecommendProducts(MongoDBClient mongoClient, RecommendItem recommendItem) {
        List<Product> list = new ArrayList<Product>();
        BasicDBList productList = recommendItem.getProductList();
        if (productList == null) {
            return null;
        }
        list = getProduct(mongoClient, productList);
        return list;
    }

    public static List<Product> sortRecommendProducts(List<Product> list) {
        java.util.Collections.sort(list, new Comparator<Object>() {

            @Override
            public int compare(Object obj1, Object obj2) {
                Product pro1 = (Product) obj1;
                Product pro2 = (Product) obj2;

                if (pro1.getScore() > pro2.getScore()) {
                    return -1;
                } else if (pro1.getScore() < pro2.getScore()) {
                    return 1;
                } else {
                    return 0;
                }

            }
        });
        return list;
    }

    private static List<Product> getProduct(MongoDBClient mongoClient, BasicDBList list) {
        if (list == null || list.size() == 0) {
            return null;
        }

        List<Product> productList = new ArrayList<Product>();
        Iterator<Object> iter = list.iterator();

        while (iter.hasNext()) {
            BasicDBObject obj = (BasicDBObject) iter.next();
            String productId = obj.getString(DBConstants.F_PRODUCTID);
            Product product = ProductManager.findProductById(mongoClient, productId);

            if (!(product.getEndDate().before(DateUtil.getGMT8Date()))) {
                productList.add(product);
            }
        }

        return productList;
    }

    public static boolean addOrUpdateProduct(RecommendItem recommendItem, Product product) {

        if (product.getScore() < DBConstants.MIN_SCORE_TO_RECOMMEND) {
            return false;
        }
        BasicDBList existProductList = recommendItem.getProductList();
        if (existProductList == null) {
            existProductList = new BasicDBList();
            recommendItem.put(DBConstants.F_RECOMMEND_LIST, existProductList);
            recommendItem.put(DBConstants.F_RECOMMEND_COUNT, 0);
        }

        boolean found = false;
        String productId = product.getId();
        Iterator<Object> iter = existProductList.iterator();
        while (iter.hasNext()) {
            BasicDBObject recommendProduct = (BasicDBObject) iter.next();
            String recommendProductId = recommendProduct.getString(DBConstants.F_PRODUCTID);
            if (recommendProductId.equalsIgnoreCase(productId)) {
                recommendProduct.put(DBConstants.F_SCORE, product.getScore());
                found = true;
                break;
            }

        }

        if (!found) {
            addRecommendProduct(existProductList, product);
            recommendItem.setRecommendCount(recommendItem.getRecommendCount() + 1);
        }

        return true;
    }

    private static void addRecommendProduct(BasicDBList existProductList, Product product) {
        BasicDBObject item = new BasicDBObject();
        item.put(DBConstants.F_PRODUCTID, product.getStringObjectId());
        item.put(DBConstants.F_SCORE, product.getScore());
        item.put(DBConstants.F_START_DATE, product.getStartDate());
        item.put(DBConstants.F_END_DATE, product.getEndDate());
        item.put(DBConstants.F_ITEM_SENT_STATUS, DBConstants.C_ITEM_NOT_SENT);

        existProductList.add(item);
    }

    public static void cleanExpireProduct(MongoDBClient mongoClient, RecommendItem recommendItem) {
        
        BasicDBList productList = recommendItem.getProductList();
        if (productList == null || productList.size() == 0)
            return;
        
        List<BasicDBObject> deleteList = new ArrayList<BasicDBObject>();
        Iterator<?> iter = productList.iterator();
        while (iter.hasNext()){
            BasicDBObject product = (BasicDBObject)iter.next();
            Date endDate = (Date)product.get(DBConstants.F_END_DATE);
            if (endDate != null && endDate.before(new Date())){
                // remove this product
                log.info("clean expired product " + product.toString() + 
                        " for item " + recommendItem.getItemId());
                deleteList.add(product);
            }
        }
        
        if (deleteList.size() == 0)
            return;
        
        productList.removeAll(deleteList);
        recommendItem.decRecommendCount(deleteList.size());
        
        mongoClient.save(DBConstants.T_RECOMMEND, recommendItem.getDbObject());
    }

}
