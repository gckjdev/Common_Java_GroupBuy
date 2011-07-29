package com.orange.groupbuy.dao;

import java.util.Date;
import java.util.List;


import com.mongodb.DBObject;
import com.orange.common.utils.DateUtil;
import com.orange.common.utils.ListUtil;
import com.orange.common.utils.StringUtil;
import com.orange.groupbuy.constant.DBConstants;

public class Product extends CommonData {

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

		return true;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1463231236679654576L;

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

	public List<String> getCategory() {
		return (List<String>) this.getStringList(DBConstants.F_CATEGORY);
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
			rebate = 10.0f; // not applicable
		} else if (value == -1.0f) {
			rebate = 10.0f; // not applicable
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
}
