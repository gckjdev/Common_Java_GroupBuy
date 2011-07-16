package com.orange.groupbuy.constant;

public class DBConstants {
	public static final String SERVER = "localhost:9160";
	//public static final String SERVER = "192.168.1.101:9160";
	public static final String CLUSTERNAME = "Test Cluster";

	public static final String KEYSPACE = "PlaceKS";

	// normal column family
	public static final String USER = "place_user";
	public static final String PLACE = "place_place";
	public static final String POST = "place_post";
	public static final String MESSAGE = "place_message";
	public static final String UPDATE = "place_update";
	public static final String APP = "place_app";
	public static final String LOCALIZEDICT = "place_localize_dict";
	// column family for index
	public static final String INDEX_USER = "idx_user";
	public static final String INDEX_USER_OWN_PLACE = "idx_user_own_places";
	public static final String INDEX_USER_FOLLOW_PLACE = "idx_user_follow_places";
	public static final String INDEX_USER_POST = "idx_user_posts";
	public static final String INDEX_PLACE_FOLLOWED_USERS = "idx_place_followed_users";
	public static final String INDEX_USER_VIEW_POSTS = "idx_user_timeline";
	public static final String INDEX_POST_RELATED_POST = "idx_post_related_posts";
	public static final String INDEX_ME_POST = "idx_user_me_posts";
	public static final String INDEX_MY_MESSAGE = "idx_my_messages";
	public static final String INDEX_GEOHASH6_PLACEID = "idx_geohash6_to_place";
	public static final String INDEX_POST_LOCATION = "idx_post_location";
	public static final String INDEX_APPID_POST = "idx_app_post";
	
	// column family for counter
	public static final String COUNTER_POST = "counter_post";
	public static final String KEY_LOGINID = "loginId";
	public static final String KEY_DEVICEID = "deviceId";

	// index place post
	public static final String INDEX_PLACE_POST = "idx_place_posts";

	// DB User Fields
	public static final String F_USERID = "userId";
	public static final String F_LOGINID = "loginId";
	public static final String F_APPID = "appId";
	public static final String F_DEVICEID = "deviceId";
	public static final String F_DEVICEMODEL = "deviceModel";
	public static final String F_DEVICEOS = "deviceOS";
	public static final String F_DEVICETOKEN = "deviceToken";
	public static final String F_LANGUAGE = "language";
	public static final String F_COUNTRYCODE = "countryCode";

	public static final String F_CREATE_DATE = "createDate";
	public static final String F_CREATE_SOURCE_ID = "createSourceId";
	public static final String F_MODIFY_DATE = "modifyDate";
	public static final String F_MODIFY_SOURCE_ID = "modifySourceId";

	public static final String F_EMAIL = "user_email";
	public static final String F_MOBILE = "mobile";
	public static final String F_PASSWORD = "password";
	public static final String F_STATUS = "status";

	public static final String F_NICKNAME = "nickName";
	public static final String F_AVATAR = "avatar";
	public static final String F_SINAID = "sinaID";
	public static final String F_QQID = "qqID";
	public static final String F_RENRENID = "renrenID";
	public static final String F_FACEBOOKID = "facebookID";
	public static final String F_TWITTERID = "twitterID";

	public static final String F_SINA_ACCESS_TOKEN = "sinaAT";
	public static final String F_SINA_ACCESS_TOKEN_SECRET = "sinaATS";
	public static final String F_QQ_ACCESS_TOKEN = "qqAT";
	public static final String F_QQ_ACCESS_TOKEN_SECRET = "qqATS";

	public static final String F_PROVINCE = "province";
	public static final String F_CITY = "city";
	public static final String F_LOCATION = "location";
	public static final String F_GENDER = "gender";
	public static final String F_BIRTHDAY = "birthday";
	public static final String F_SINA_NICKNAME = "sinaNickName";
	public static final String F_SINA_DOMAIN = "sinaDomain";
	public static final String F_QQ_NICKNAME = "qqNickName";
	public static final String F_QQ_DOMAIN = "qqDomain";

	// DB Place Fields
	public static final String F_PLACEID = "placeId";
	public static final String F_LONGITUDE = "long";
	public static final String F_LATITUDE = "lat";
	public static final String F_NAME = "name";
	public static final String F_RADIUS = "radius";
	public static final String F_POST_TYPE = "postType";
	public static final String F_DESC = "desc";
	public static final String F_PLACE_TYPE = "type";
	public static final String F_AUTH_FLAG = "auth";

	// DB Post Fields
	public static final String F_POSTID = "postId";
	public static final String F_USER_LONGITUDE = "userLong";
	public static final String F_USER_LATITUDE = "userLat";
	public static final String F_TEXT_CONTENT = "text";
	public static final String F_CONTENT_TYPE = "type";
	public static final String F_IMAGE_URL = "image";
	public static final String F_TOTAL_VIEW = "totalView";
	public static final String F_TOTAL_FORWARD = "totalForward";
	public static final String F_TOTAL_QUOTE = "totalQuote";
	public static final String F_TOTAL_REPLY = "totalReply";
	public static final String F_SRC_POSTID = "srcPostId";
	public static final String F_REPLY_POSTID = "replyPostId";

	//DB App fields
//	public static final String F_APPID = "appid";
//	public static final String F_NAME = "name";
//	public static final String F_DESC = "desc";

	public static final String F_APPURL = "appUrl";
	public static final String F_VERSION = "version";
	public static final String F_ICON = "icon";

	public static final String F_SINA_APPKEY = "sinaAppKey";
	public static final String F_SINA_APPSECRET = "sinaAppSecret";
	public static final String F_QQ_APPKEY = "qqAppKey";
	public static final String F_QQ_APPSECRET = "qqAppSecret";
	public static final String F_RENREN_APPKEY = "renrenAppKey";
	public static final String F_RENREN_APPSECRET = "renrenAppSecret";
	// computed fields, not stored in DB
	public static final String C_TOTAL_RELATED = "totalRelatedPost";

	public static final String F_MESSAGEID = "messageId";
	public static final String F_MESSAGE_CONTENT = "text";
	public static final String F_FROM_USERID = "fromUserId";
	public static final String F_TO_USERID = "toUserId";
	public static final String F_SRC_MESSAGEID = "srcMessageId";

	public static final String F_MESSAGE_TYPE = "messageType";

	// Value
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_SUSPEND = "2";

	// Constants
	public static final int LOGINID_OWN = 1;
	public static final int LOGINID_SINA = 2;
	public static final int LOGINID_QQ = 3;
	public static final int LOGINID_RENREN = 4;
	public static final int LOGINID_FACEBOOK = 5;
	public static final int LOGINID_TWITTER = 6;

	public static final int CONTENT_TYPE_TEXT = 1;
	public static final int CONTENT_TYPE_TEXT_PHOTO = 2;

	public static final String AUTH_FLAG_NONE = "0";

	public static final String PLACE_TYPE_UNKNOWN = "0";

	// TODO: CommonManager.java
	public static int UNLIMITED_COUNT = 9999999;

	// normal statistic Constants
	public static final String USER_SIMILARITY = "place_user_similarity";
	public static final String USER_POST_STATISTIC = "place_user_post_stat";

	public static final String F_USER_POST_STATISTIC_TOTOAL = "totalPost";

	// DB UPDATE Row Key
	public static final String R_UPDATE_DATA = "update_data";
	public static final String R_RECOMMEND_APPS = "recommend_apps";

	// appID type
	public static final String R_APPID_ALL = "ALL";

}
