package com.orange.groupbuy.constant;


public class DBConstants {


	public static final String D_GROUPBUY = "groupbuy";

	// tables
	public static final String T_USER = "user";
	public static final String T_FETCH_TASK = "task"; 
	public static final String T_PRODUCT = "product";
	public static final String T_IDX_PRODUCT_GPS = "address";
	public static final String T_APP = "app";
	public static final String T_KEYWORD_STAT = "keyword_stat";
	public static final String T_PUSH_MESSAGE = "push_message";
	public static final String T_RECOMMEND = "recommend"; 
    public static final String T_CATEGORY = "category"; 
    public static final String T_SHOPPING_CATEGORY = "shopping_category"; 
	

	// fields
	public static final String F_TASK_STATUS = "status";
	public static final String F_TASK_URL = "url";
	public static final String F_TASK_FILE_PATH = "path";
	public static final String F_TASK_PARSER_TYPE = "parser";
	public static final String F_TASK_SITE_ID = "site_id";
	public static final String F_TASK_RETRY_TIMES = "retry";
	
	public static final String F_PUSH_MESSAGE_TRYCOUNT = "try_cnt";
	public static final String F_PUSH_MESSAGE_STATUS = "p_status";
    public static final String F_PUSH_MESSAGE_SCHEDULE_DATE = "sc_date";
	public static final String F_PUSH_MESSAGE_START_DATE = "s_date";
	public static final String F_PUSH_MESSAGE_FINISH_DATE = "f_date";
	public static final String F_PUSH_MESSAGE_ERROR_CODE = "err_code";
	public static final String F_PUSH_MESSAGE_TYPE = "type";
    public static final String F_PUSH_MESSAGE_SUBJECT = "p_sub";
    public static final String F_PUSH_MESSAGE_BODY = "p_body";
    public static final String F_PUSH_MESSAGE_IPHONE = "p_iphone";
    public static final String F_PUSH_MESSAGE_ANDROID = "p_android";

    public static final String F_PUSH_MESSAGE_WEIBO = "p_weibo";
    public static final String F_PUSH_MESSAGE_IMAGE = "p_image";

    public static final String F_PUSH_MESSAGE_REASON = "reason";


    
    public static final String F_RECOMMEND_STATUS = "r_status";
    public static final String F_RECOMMEND_COUNT = "r_count";
    public static final String F_RECOMMEND_DATE = "r_date";
    public static final String F_PUSH_COUNT = "p_count";
    public static final String F_PUSH_DATE = "p_date";


	// constants
	public static final int C_TASK_STATUS_NOT_RUNNING = 0;
	public static final int C_TASK_STATUS_RUNNING = 1;
	public static final int C_TASK_STATUS_DOWNLOAD_OK = 2;
	public static final int C_TASK_STATUS_CLOSE = 3;
	public static final int C_TASK_STATUS_FAILURE = 4;
	public static final int C_TASK_STATUS_FAIL_MAX_RETRY = 5;
	
	public static final int C_PUSH_MESSAGE_STATUS_NOT_RUNNING = 0;
    public static final int C_PUSH_MESSAGE_STATUS_RUNNING = 1;
    public static final int C_PUSH_MESSAGE_STATUS_CLOSE = 2;
    public static final int C_PUSH_MESSAGE_STATUS_FAILURE = 3;
    
    public static final int C_PUSH_MESSAGE_TRY_COUNT_LIMIT = 100;
    public static final int C_PUSH_DAILY_LIMIT = 5;

    // failure reason
    public static final int C_PUSH_MESSAGE_FAIL_REACH_USER_LIMIT = 1001;
    
    public static final int C_RECOMMEND_STATUS_NOT_RUNNING = 0;
    public static final int C_RECOMMEND_STATUS_RUNNING = 1;
    public static final int C_RECOMMEND_STATUS_COLSE = 2;
    public static final int C_RECOMMEND_STATUS_FAILURE = 3;
    
    
    
    
    public static final int C_ITEM_NOT_SENT = 0;
    public static final int C_ITEM_SENT = 1;
    
	// major product or not
	public static final int C_NOT_MAJOR = 0;
	public static final int C_IS_MAJOR = 1;
	
	public static final int C_CATEGORY_UNKNOWN = 0;
	public static final int C_CATEGORY_EAT = 1;
	public static final int C_CATEGORY_FUN = 2;
	public static final int C_CATEGORY_FACE = 3;
	public static final int C_CATEGORY_SHOPPING = 4;
	public static final int C_CATEGORY_KEEPFIT = 5;
	public static final int C_CATEGORY_LIFE = 6;
    public static final int C_CATEGORY_FILM = 7;
    public static final int C_CATEGORY_COUPON = 8;
    public static final int C_CATEGORY_TRAVEL = 9;
    public static final int C_CATEGORY_HOTEL = 10;
    public static final int C_CATEGORY_PHOTO = 11;

	
	public static final String C_CATEGORY_NAME_UNKNOWN = "综合";
	public static final String C_CATEGORY_NAME_EAT = "美食";
	public static final String C_CATEGORY_NAME_FUN = "休闲";
	public static final String C_CATEGORY_NAME_FACE = "丽人";
	public static final String C_CATEGORY_NAME_SHOPPING = "网购";
	public static final String C_CATEGORY_NAME_KEEPFIT = "运动";
	public static final String C_CATEGORY_NAME_LIFE = "生活";
    public static final String C_CATEGORY_NAME_FILM = "电影票";
    public static final String C_CATEGORY_NAME_COUPON = "代金券";
    public static final String C_CATEGORY_NAME_TRAVEL = "旅游";
    public static final String C_CATEGORY_NAME_HOTEL = "酒店";
    public static final String C_CATEGORY_NAME_PHOTO = "写真";

	
	public static final int SORT_BY_START_DATE = 0;
	public static final int SORT_BY_PRICE = 1;
	public static final int SORT_BY_REBATE = 2;
	public static final int SORT_BY_BOUGHT = 3;
	
	// site ID
	public static final String C_SITE_MEITUAN = "meituan";
	public static final String C_SITE_DIANPIAN = "dianping";
	public static final String C_SITE_WOWO = "wowo";
	public static final String C_SITE_58 = "58";
	public static final String C_SITE_24QUAN = "24quan";
	public static final String C_SITE_FTUAN = "ftuan";
	public static final String C_SITE_MANZUO = "manzuo";
	public static final String C_SITE_GAOPENG = "gaopeng";
	public static final String C_SITE_DIDA = "dida";
	public static final String C_SITE_NUOMI = "nuomi";
	public static final String C_SITE_GANJI = "ganji";
	public static final String C_SITE_KAIXIN = "kaixin";
	public static final String C_SITE_XING800 = "xing800";
	public static final String C_SITE_FANTONG = "fantong";
	public static final String C_SITE_LASHOU = "lashou";
	public static final String C_SITE_QUNAER = "qunaer";
	public static final String C_SITE_JUQI = "juqi";
	public static final String C_SITE_TUANBAO = "tuanbao";
	public static final String C_SITE_JUMEIYOUPIN = "jumeiyoupin";
	public static final String C_SITE_TUANHAO = "tuanhao";
	public static final String C_SITE_HAOTEHUI = "haotehui";
	public static final String C_SITE_SOUHU = "sohu";
	public static final String C_SITE_AIBANG = "aibang";
	public static final String C_SITE_SINA = "sina";
	public static final String C_SITE_QQ = "qq";
	public static final String C_SITE_JINGDONG = "jingdong";
	public static final String C_SITE_LETAO = "letao";
	public static final String C_SITE_ZTUAN = "ztuan";
	public static final String C_SITE_FENTUAN = "fentuan";
    public static final String C_SITE_ZUITAO = "zuitao";
    public static final String C_SITE_TGBABA = "tianji88";
    public static final String C_SITE_CHECKOO = "qianku";
    public static final String C_SITE_XIUTUAN = "xiutuan";
    public static final String C_SITE_5151TUAN = "5151tuan";
    public static final String C_SITE_5151POPO = "5151paopao";
    public static final String C_SITE_MIQI = "miqi";
    public static final String C_SITE_COO8 = "coo8";
    public static final String C_SITE_36TUAN = "36tuan";
    public static final String C_SITE_HAOYIDING = "haoyiding";
    public static final String C_SITE_HAOHUASUAN = "haohuasuan";
    public static final String C_SITE_SHUANGTUAN = "shuangtuan";


//	user("device_id","device_model","device_os","device_token","language",
//	"country_code","create_date","source_id","subscribe")
	
	// DB User Fields
	public static final String F_USERID = "_id";
	public static final String F_LOGINID = "login_id";
	public static final String F_APPID = "app_id";
	public static final String F_DEVICEID = "device_id";
	public static final String F_DEVICEMODEL = "device_model";
	public static final String F_DEVICEOS = "device_os";
	public static final String F_DEVICETOKEN = "device_token";
	public static final String F_LANGUAGE = "language";
	public static final String F_COUNTRYCODE = "country_code";
	public static final String F_CREATE_SOURCE_ID = "source_id";
	

	public static final String F_CREATE_DATE = "c_date";
    public static final String F_MODIFY_DATE = "m_date";

	public static final String F_FOREIGN_USER_ID = "user_id";
	
	public static final String F_EMAIL = "email";
	public static final String F_MOBILE = "mobile";
	public static final String F_PASSWORD = "password";
	public static final String F_VERIFICATION = "verification";
	public static final String F_VERIFYCODE = "verify_code";
	public static final String F_STATUS = "status";

	public static final String F_NICKNAME = "nick_name";
	public static final String F_AVATAR = "avatar";
	public static final String F_SINAID = "sina_id";
	public static final String F_QQID = "qq_id";
	public static final String F_RENRENID = "renren_id";
	public static final String F_FACEBOOKID = "facebook_id";
	public static final String F_TWITTERID = "twitter_id";

	public static final String F_SINA_ACCESS_TOKEN = "sina_at";
	public static final String F_SINA_ACCESS_TOKEN_SECRET = "sina_ats";
	public static final String F_QQ_ACCESS_TOKEN = "qq_at";
	public static final String F_QQ_ACCESS_TOKEN_SECRET = "qq_ats";

	public static final String F_PROVINCE = "province";
	public static final String F_LOCATION = "location";
	public static final String F_GENDER = "gender";
	public static final String F_BIRTHDAY = "birthday";
	public static final String F_SINA_NICKNAME = "sina_nick";
	public static final String F_SINA_DOMAIN = "sina_domain";
	public static final String F_QQ_NICKNAME = "qq_nick";
	public static final String F_QQ_DOMAIN = "qq_domain";

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

	// App Table
	public static final String F_APPURL = "app_url";
	public static final String F_VERSION = "version";
	public static final String F_ICON = "icon";
	public static final String F_KEYWORD_NAME = "name";
	public static final String F_KEYWORD_QUERY = "query";
	

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
	public static final String STATUS_TO_VERIFY = "3";

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
	
	public static final String F_LOC = "loc";
	public static final String F_WAP_LOC = "w_loc";
	
	public static final String F_SITE_NAME = "s_name";
	public static final String F_SITE_URL = "s_url";
	public static final String F_SITE_ID = "s_id";	
	
	public static final String F_TITLE = "title";
	public static final String F_DESCRIPTION = "desc";
	public static final String F_DETAIL = "tip";	
	
	public static final String F_IMAGE = "image";
	public static final String F_START_DATE = "s_date";
	public static final String F_END_DATE = "e_date";
	public static final String F_VALUE = "value";
	public static final String F_PRICE = "price";
	public static final String F_REBATE = "rebate";
	public static final String F_BOUGHT = "bought";

	public static final String F_CATEGORY = "cate";
	public static final String F_MAJOR = "major";
	
	public static final String F_CITY = "city";
	public static final String F_VENDOR = "vendor";
	public static final String F_SHOP = "shop";
	public static final String F_ADDRESS = "addr";
	public static final String F_GPS = "gps";
	public static final String F_RANGE = "range";
	public static final String F_DP_SHOPID = "dpid";
	public static final String F_TEL = "tel";
	public static final String F_POST = "post";
	public static final String F_SOLD_OUT = "sold";
	public static final String F_MIN_QUOTA = "min";
	public static final String F_MAX_QUOTA = "max";
	public static final String F_TAG = "tag";
	public static final String F_PRIORITY = "pri";
	public static final String F_MERCHANT_END_DATE = "me_date";
	public static final String F_SCORE = "score";
	public static final String F_RECOMMEND_LIST = "re_list";
	public static final String F_ITEM_SENT_STATUS = "status";
	public static final String F_TOP_SCORE = "topscore";

	public static final String F_COMMENTS = "comments";
	public static final String F_KEYWORD = "keys";
	public static final String F_PRODUCTID = "p_id";
	
	public static final String C_NATIONWIDE = "全国";

	public static final String F_ID = "_id";
	public static final String F_INDEX_ID = "id";

	public static final String F_STAT = "stat";	
	
	public static final String F_COUNTER_ADDRESS_API = "address_api";
	public static final String F_COUNTER_ADDRESS_HTML = "address_html";
	public static final String F_COUNTER_ADDRESS_FAIL = "address_fail";
	public static final String F_COUNTER_ADDRESS_TOTAL = "address_toal";
	public static final String F_COUNTER_ADDRESS_SKIP = "address_skip";

	public static final String F_COUNTER_INSERT = "insert";
	public static final String F_COUNTER_UPDATE = "update";
	public static final String F_COUNTER_EXIST = "exist";
	public static final String F_COUNTER_TOTAL = "total";
	public static final String F_COUNTER_FAIL = "fail";

	public static final String F_DATE = "date";
	public static final String F_SEARCH_HISTORY = "s_his";
	public static final String F_COUNT = "cnt";

	public static final String F_CATEGORY_NAME = "cate_n";
    public static final String F_CATEGORY_ID = "cate_id";
	public static final String F_SUB_CATEGORY_NAME = "subcate_n";
	public static final String F_SUB_CATEGORY_ID = "subcate_id";
	public static final String F_SUB_CATEGORY_KEYS = "keys";
	public static final String F_MAX_PRICE = "max_p";
	public static final String F_MIN_REBATE = "min_r";
	public static final String F_EXPIRE_DATE = "v_date";

	public static final String F_SHOPPING_LIST = "s_list";
	public static final String F_ITEM_ID = "item_id";

    public static final float MIN_SCORE_TO_RECOMMEND = 0.04f;

    public static final String F_UP = "up";
    public static final String F_DOWN = "down";

    public static final int C_PUSH_TYPE_IPHONE = 1;
    public static final int C_PUSH_TYPE_ANDROID = 2;
    public static final int C_PUSH_TYPE_EMAIL = 3;
    public static final int C_PUSH_TYPE_WEIBO = 4;

    public static final String F_COMMENT_CONTENT = "content";

    public static final String F_SUB_CATEGORY = "subcate";

    public static final String F_PUSH_APP_KEY = "push_key";
    public static final String F_PUSH_APP_SECRET = "push_secret";
    public static final String F_PUSH_APP_MASTER_SECRET = "push_master_secret";

    public static final String F_PUSH_APP_DEV_CERTIFICATE = "push_dev_cert";
    public static final String F_PUSH_APP_DEV_CERTIFICATE_PASSWORD = "push_dev_cert_pwd";

    public static final String F_PUSH_APP_PRODUCT_CERTIFICATE = "push_pro_cert";
    public static final String F_PUSH_APP_PRODUCT_CERTIFICATE_PASSWORD = "push_pro_cert_pwd";

    public static final String F_EXTRA_ID = "extra_id";





    // category manager
    // public static final String F_CATEGORY_NAME = "cate_n";
    // public static final String F_KEYWORD = "keys";
    // 
    

    // shopping list category manager
    // public static final String F_CATEGORY_NAME = "cate_n";
    // public static final String F_SUB_CATEGORY_NAME = "subcate_n";
    // public static final String F_SUB_CATEGORY_ID = "subcate_id";
    // public static final String F_KEYWORD = "keys";
    // { { "cate_n":"美食1", "subcate" : [ { "subcate_id": 1, "subcate_n":"湘菜", "keys":["湘菜",湖南�?] ] },
    //   { "cate_n":"美食2", "subcate" : [ { "subcate_id": 2, "subcate_n":"西餐", "keys":["浪漫西餐", "法式大餐"] ] },
    // }       
    
    // test
}
