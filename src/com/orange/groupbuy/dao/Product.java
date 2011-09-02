package com.orange.groupbuy.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import sun.util.resources.CalendarData;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.ListUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;

public class Product extends CommonData {
    
    private float score;

	public Product(DBObject obj) {
		super(obj);
	}

	public Product() {
		super();
	}

	public boolean setMandantoryFields(String city, String loc, String image,
			String title, Date startDate, Date endDate, double price,
			double value, int bought, String siteId, String siteName,
			String siteURL) {

		if (StringUtil.isEmpty(city) || StringUtil.isEmpty(loc)
				|| StringUtil.isEmpty(image) || StringUtil.isEmpty(title)
				|| startDate == null || endDate == null
				|| StringUtil.isEmpty(siteId) || StringUtil.isEmpty(siteName)
				|| siteURL == null)
			return false;

		put(DBConstants.F_CITY, city);
		put(DBConstants.F_LOC, loc);
		put(DBConstants.F_IMAGE, image);
		put(DBConstants.F_TITLE, title);
		put(DBConstants.F_START_DATE, startDate);
		put(DBConstants.F_END_DATE, endDate);
		put(DBConstants.F_PRICE, price);
		put(DBConstants.F_VALUE, value);
		put(DBConstants.F_BOUGHT, bought);
		put(DBConstants.F_SITE_ID, siteId);
		put(DBConstants.F_SITE_NAME, siteName);
		put(DBConstants.F_SITE_URL, siteURL);
		
		double topScore = calcTopScore_2(bought, startDate);
		put(DBConstants.F_TOP_SCORE, topScore);
		System.out.println("<Product> topscore="+topScore+",title="+title);
		
		return true;
	}

	public double calcTopScore(int bought, Date startDate) {
        // TODO Auto-generated method stub
	    final double GRAVITY = 1.5;
	    Date nowDate = new Date();
	    int hours = DateUtil.calcHour(startDate, nowDate);
	    if (hours != -1) {
	        double score = (double) bought / Math.pow((hours + 2), GRAVITY);    
	        return score;
	    } else {
	        return 0; 
	    }
    }
	
	public double calcTopScore_2(int bought, Date startDate) {
	    final double T = 24 * 60 * 60 * 1000;
	    final int N = 2;
        long time = startDate.getTime();
        if (bought >= 0) {
            double score = logFuntion(N, bought) +  ((double)time / (T));
            return score;
        } else {
            return 0;
        }
    }
	
	private double logFuntion(int n, int bought) {
	    if (n<0 || bought<0)
	        return 0.0;
	    double base = Math.log((double) bought) / Math.log((double) n); 
	        
        return base;  
    }

    public void calcAndSetTopScore(int bought, Date startDate) {
	    double topScore = calcTopScore_2(bought, startDate);
	    put(DBConstants.F_TOP_SCORE, topScore);
	}
    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1463231236679654576L;
	
	public List<String> getTag() {
		return this.getStringList(DBConstants.F_TAG);
	} 

	public String getLoc() {
		return this.getString(DBConstants.F_LOC);
	}

	public void setLoc(String loc) {
		this.put(DBConstants.F_LOC, loc);
	}

	public String getWapLoc() {
		return this.getString(DBConstants.F_WAP_LOC);
	}

	public void setWapLoc(String loc) {
		this.put(DBConstants.F_WAP_LOC, loc);
	}

	public String getSiteId() {
		return this.getString(DBConstants.F_SITE_ID);
	}

	public void setSiteId(String siteId) {
		this.put(DBConstants.F_SITE_ID, siteId);
	}

	public String getSiteName() {
		return this.getString(DBConstants.F_SITE_NAME);
	}

	public void setSiteName(String website) {
		this.put(DBConstants.F_SITE_NAME, website);
	}

	public String getSiteUrl() {
		return this.getString(DBConstants.F_SITE_URL);
	}

	public void setSiteUrl(String siteurl) {
		this.put(DBConstants.F_SITE_URL, siteurl);
	}

	public String getCity() {
		return this.getString(DBConstants.F_CITY);
	}

	public void setCity(String city) {
		this.put(DBConstants.F_CITY, city);
	}

	public String getTitle() {
		return this.getString(DBConstants.F_TITLE);
	}

	public void setTitle(String title) {
		this.put(DBConstants.F_TITLE, title);
	}

	public String getImage() {
		return this.getString(DBConstants.F_IMAGE);
	}

	public void setImage(String image) {
		this.put(DBConstants.F_IMAGE, image);
	}

	public Date getStartDate() {
		return (Date) this.getDate(DBConstants.F_START_DATE);
	}

	public void setStartDate(Date startDate) {
		this.put(DBConstants.F_START_DATE, startDate);
	}

	public Date getEndDate() {
		return (Date) this.getDate(DBConstants.F_END_DATE);
	}

	public void setEndDate(Date endDate) {
		this.put(DBConstants.F_END_DATE, endDate);
	}

	public double getValue() {
		return this.getDouble(DBConstants.F_VALUE);
	}

	public void setValue(double value) {
		this.put(DBConstants.F_VALUE, value);
	}

	public double getPrice() {
		return this.getDouble(DBConstants.F_PRICE);
	}

	public void setPrice(double price) {
		this.put(DBConstants.F_PRICE, price);
	}

	public double getRebate() {
		return this.getDouble(DBConstants.F_REBATE);
	}

	public void setRebate(double rebate) {
		this.put(DBConstants.F_REBATE, rebate);
	}

	public int getBought() {
		return this.getInt(DBConstants.F_BOUGHT);
	}

	public void setBought(int bought) {
		this.put(DBConstants.F_BOUGHT, bought);
	}

	public String getDetail() {
		return this.getString(DBConstants.F_DETAIL);
	}

	public void setDetail(String detail) {
		this.put(DBConstants.F_DETAIL, detail);
	}

	public String getDescription() {
		return this.getString(DBConstants.F_DESCRIPTION);
	}

	public void setDescription(String description) {
		this.put(DBConstants.F_DESCRIPTION, description);
	}

	public int getCategory() {
		return this.getInt(DBConstants.F_CATEGORY);
	}

	public void setCategory(String... categoryList) {
		List<String> list = ListUtil.stringsToList(categoryList);
		if (list == null)
			return;

		this.put(DBConstants.F_CATEGORY, list);
	}

	public int getMajor() {
		return this.getInt(DBConstants.F_MAJOR);
	}

	public void setMajor(int major) {
		this.put(DBConstants.F_MAJOR, major);
	}

	public String getVendor() {
		return this.getString(DBConstants.F_VENDOR);
	}

	public void setVendor(String description) {
		this.put(DBConstants.F_VENDOR, description);
	}

	public static final String F_SHOP = "shop";

	public List<String> getShop() {
		return (List<String>) this.getStringList(DBConstants.F_SHOP);
	}

	public void setShopList(List<String> list) {
		if (list == null)
			return;

		this.put(DBConstants.F_SHOP, list);
	}

	public List<String> getAddress() {
		return (List<String>) this.getStringList(DBConstants.F_ADDRESS);
	}

	public void setAddress(List<String> addrList) {
		if (addrList == null)
			return;

		this.put(DBConstants.F_ADDRESS, addrList);
	}

	@SuppressWarnings("unchecked")
	public List<List<Double>> getGPS() {
		return (List<List<Double>>) this.getObject(DBConstants.F_GPS);
	}

	public void setGPS(List<List<Double>> gpsList) {		
		if (gpsList == null)
			return;

		this.put(DBConstants.F_GPS, gpsList);
	}

	
	public List<String> getRange() {
		return (List<String>) this.getStringList(DBConstants.F_RANGE);
	}

	public void setRange(List<String> rangeList) {
		if (rangeList == null)
			return;

		this.put(DBConstants.F_RANGE, rangeList);
	}

	public List<String> getDpShopId() {
		return (List<String>) this.getStringList(DBConstants.F_DP_SHOPID);
	}

	public void setDpShopId(String... dpShopIdList) {
		List<String> list = ListUtil.stringsToList(dpShopIdList);
		if (list == null)
			return;

		this.put(DBConstants.F_DP_SHOPID, list);
	}

	public List<String> getTel() {
		return (List<String>) this.getStringList(DBConstants.F_TEL);
	}

	public void setTel(List<String> phoneList) {
		if (phoneList == null)
			return;

		this.put(DBConstants.F_TEL, phoneList);
	}

	public String getId() {
		return this.getStringObjectId();
	}

	public void calculateRebate() {

		double price = getDouble(DBConstants.F_PRICE);
		double value = getDouble(DBConstants.F_VALUE);

		double rebate = 0;
		if (value == 0.0f) {
			rebate = 0.0f; // not applicable
		} else if (value == -1.0f) {
			rebate = 0.0f; // not applicable
		} else {
			rebate = (price / value) * 10.0;
		}

		String str = String.format("%.1f", rebate);
		this.put(DBConstants.F_REBATE, Double.valueOf(str).doubleValue());
	}

	public void setCategory(int category) {
		this.put(DBConstants.F_CATEGORY, category);
	}
	public String getStartDateString() {
		Date date = getStartDate();
		return DateUtil.dateToString(date);
	}
	public String getEndDateString() {
		Date date = getEndDate();
		return DateUtil.dateToString(date);
	}

	public void addGPS(List<Double> gps) {
		if (gps == null || gps.size() == 0)
			return;
		
		if (!dbObject.containsField(DBConstants.F_GPS)){
			List<List<Double>> list = new LinkedList<List<Double>>();			
			list.add(gps);
			this.setGPS(list);
			return;
		}
		
		List<List<Double>> list = this.getGPS();
		if (list == null){
			list = new LinkedList<List<Double>>();
		}		
		list.add(gps);
		
	}

	public void setMerchantEndDate(Date merchantEndDate) {
		this.put(DBConstants.F_MERCHANT_END_DATE, merchantEndDate);
	}

	public void setPost(boolean value) {
		this.put(DBConstants.F_POST, value);
	}

	public void setSoldOut(boolean value) {
		this.put(DBConstants.F_SOLD_OUT, value);
	}


	public void setQuota(int maxQuota, int minQuota) {
		this.put(DBConstants.F_MIN_QUOTA, minQuota);
		this.put(DBConstants.F_MAX_QUOTA, maxQuota);		
	}

	public void setTag(List<String> list) {
		if (list == null)
			return;

		this.put(DBConstants.F_TAG, list);
		
	}

	public void setPriority(int priority) {
		this.put(DBConstants.F_PRIORITY, priority);
	}
	
	public void setUp(int up) {
	    this.put(DBConstants.F_UP, up);
	}
	
	public int getUp() {
	    return this.getInt(DBConstants.F_UP);
	}
	
	public void setDown(int down) {
	    this.put(DBConstants.F_DOWN, down);
	}
	
	public int getDown() {
	    return this.getInt(DBConstants.F_DOWN);
	}

    public int getActionCounterValueByName(String actionName) {        
        return this.getInt(actionName);
    }

    public float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
	
//	public void setScore(Float score) {
//	    this.put(DBConstants.F_SCORE, score);
//	}
//	
//	public float getScore() {
//	    return this.getFloat(DBConstants.F_SCORE);
//	}
    
    public BasicDBList getComments() {
        return (BasicDBList)this.dbObject.get(DBConstants.F_COMMENTS);
    }

    public double getTopScore() {
        return this.getDouble(DBConstants.F_TOP_SCORE);
    }
}
