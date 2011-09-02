package com.orange.groupbuy.dao;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.orange.common.utils.DateUtil;
import com.orange.groupbuy.constant.DBConstants;

public class RecommendItem extends CommonData {
    public RecommendItem(DBObject dbObject) {
        super(dbObject);
    }

    public BasicDBList getProductList() {
        return (BasicDBList) dbObject.get(DBConstants.F_RECOMMENDLIST);
    }

    public void setRecommendDate(Date date) {
        this.getDbObject().put(DBConstants.F_RECOMMEND_DATE, date);
    }

    public Date getRecommendDate() {
        return getDate(DBConstants.F_RECOMMEND_DATE);
    }
    
    public boolean hasRecommendToday() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+0800");
        Calendar now = Calendar.getInstance(timeZone);
        now.setTime(new Date());

        Date lastRecommendDate = getRecommendDate();
        Calendar lastRecommendCalendar = Calendar.getInstance(timeZone);
        if (lastRecommendDate != null) {
            lastRecommendCalendar.setTime(lastRecommendDate);
            if (now.get(Calendar.DAY_OF_MONTH) <= lastRecommendCalendar.get(Calendar.DAY_OF_MONTH)) {
                return true;
            }
        }
        
        return false;
    }

    public String sortAndSelectProduct(User user) {
        BasicDBList list = getProductList();

        if (list == null || list.size() <= 0) {
            return null;
        }

        java.util.Collections.sort(list, new Comparator<Object>() {

            @Override
            public int compare(Object obj1, Object obj2) {
                BasicDBObject pro1 = (BasicDBObject) obj1;
                BasicDBObject pro2 = (BasicDBObject) obj2;

                if (pro1.getDouble(DBConstants.F_SCORE) > pro2.getDouble(DBConstants.F_SCORE)) {
                    return -1;
                }
                else if (pro1.getDouble(DBConstants.F_SCORE) < pro2.getDouble(DBConstants.F_SCORE)) {
                    return 1;
                }
                else {
                    return 0;
                }

            }
        }
        );

        for (Object obj : list) {
            BasicDBObject product = (BasicDBObject) obj;
            int status = product.getInt(DBConstants.F_ITEM_SENT_STATUS);
            if (status == DBConstants.C_ITEM_NOT_SENT) {
                product.put(DBConstants.F_ITEM_SENT_STATUS, DBConstants.C_ITEM_SENT);
                setRecommendDate(new Date());
                return product.getString(DBConstants.F_PRODUCTID);
            }
        }
        return null;

    }

}
