package com.orange.groupbuy.dao;

public class App {
	String version;
	String appUrl;
	String appId;

	public App(String version, String appUrl, String appId) {
		super();
		this.version = version;
		this.appUrl = appUrl;
		this.appId = appId;
	}

	public String getAppId() {
		return appId;
	}

	public String getVersion() {
		return version;
	}

	public String getAppUrl() {
		return appUrl;
	}

}
