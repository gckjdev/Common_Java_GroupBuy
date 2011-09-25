package com.orange.groupbuy.constant;

import org.antlr.grammar.v3.ANTLRv3Parser.finallyClause_return;

public class ServiceConstant  {

	// server name
//	public static final String FILE_SERVER_NAME = "http://192.168.1.150:80/upload/";
//	public static final String FILE_LOCAL_PATH = "F:/dipan/upload/";
	public static final String FILE_LOCAL_PATH = "/Library/WebServer/Documents/upload/";
	public static final String FILE_SERVER_NAME = "http://192.168.1.188:80/upload/";
	public static final String SNS_LOG_FILE = "F:/sns.log";
//	public static final String FILE_SERVER_NAME = "http://192.168.1.151:80/upload/";
//	public static final String FILE_LOCAL_PATH = "C:/xampp/htdocs/upload/";
	public static final String VERIFY_URL = "http://127.0.0.1:8000/service";
	
	// method name
	public static final String METHOD = "m";
	public static final String METHOD_TEST = "test";
	public static final String METHOD_ONLINESTATUS = "srpt";
	public static final String METHOD_REGISTRATION = "reg";
	public static final String METHOD_CREATEPOST = "cp";
	public static final String METHOD_CREATEPLACE = "cpl";
	public static final String METHOD_GETUSERPLACES = "gup";
	public static final String METHOD_GETPLACEPOST = "gpp";
	public static final String METHOD_GETNEARBYPLACE = "gnp";
	public static final String METHOD_USERFOLLOWPLACE = "ufp";
	public static final String METHOD_USERUNFOLLOWPLACE = "unfp";
	public static final String METHOD_GETUSERFOLLOWPOSTS = "guf";
	public static final String METHOD_GETUSERFOLLOWPLACE = "gufp";
	public static final String METHOD_GETNEARBYPOSTS = "gne";
	public static final String METHOD_GETMYPOSTS = "gmyp";
	public static final String METHOD_DEVICELOGIN = "dl";
	public static final String METHOD_GETPOSTRELATEDPOST = "gpr";
	public static final String METHOD_BINDUSER = "bu";
	public static final String METHOD_SENDMESSAGE = "sm";
	public static final String METHOD_GETMYMESSAGE = "gmm";
	public static final String METHOD_DELETEMESSAGE = "dmm";
	public static final String METHOD_GETMEPOST = "gmep";
	public static final String METHOD_UPDATEUSER = "uu";
	public static final String METHOD_UPDATEPLACE = "up";
	public static final String METHOD_GETAPPS = "ga";
	public static final String METHOD_GETAPPUPDATE = "gau";
	public static final String METHOD_GETPLACE = "gtp";
	public static final String METHOD_GETPUBLICTIMELINE = "gpt";
	public static final String METHOD_ACTIONONPOST = "aop";	
	public static final String METHOD_SEARCHPRODUCT = "fpk";
	public static final String METHOD_UPDATEKEYWORD = "uk";
	public static final String METHOD_ACTIONONPRODUCT = "ap";
	public static final String METHOD_REGISTERUSER = "ru";
	public static final String METHOD_LOGIN = "lg";
	public static final String METHOD_ADDSHOPPINGITEM = "asi";
	public static final String METHOD_UPDATESHOPPINGITEM = "usi";
	public static final String METHOD_DELETESHOPPINGITEM = "dsi";
	public static final String METHOD_GETSHOPPINGITEM = "gsi";	
    public static final String METHOD_WRITEPRODUCTCOMMENT = "wpc";
    public static final String METHOD_GETPRODUCTCOMMENTS = "gpc";
    public static final String METHOD_GETALLCATEGORY = "gac";
    public static final String METHOD_GETSHOPPINGCATEGORY = "gsc";
    public static final String METHOD_GETUSERSHOPPINGITEMLIST = "gusil";
    public static final String METHOD_COUNTSHOPPINGITEMPRODUCTS = "csip";
    public static final String METHOD_SEGMENTTEXT = "st";
    public static final String METHOD_COMPAREPRODUCT = "comp";

    public static final String METHOD_REGISTEREMAIL = "re";
	
	// internal usage
	public static final String METHOD_DELETESOLRINDEX = "deletesolr";
	
	// for groupbuy
	public static final String METHOD_REGISTERDEVICE = "rd";
	public static final String METHOD_GROUPBUY_DEVICELOGIN = "gdl";
	public static final String METHOD_UPDATE_SUBSCRIPTION = "us";
	public static final String METHOD_BIND_USER_SERVICE = "bu";
	
	// request parameters

	public static final String PARA_USERID = "uid";
	public static final String PARA_LOGINID = "lid";
	public static final String PARA_LOGINIDTYPE = "lty";
	public static final String PARA_USERTYPE = "uty";
	public static final String PARA_PASSWORD = "pwd";
	public static final String PARA_VERIFICATION = "vri";
	public static final String PARA_NEW_PASSWORD = "npwd";
	public static final String PARA_VERIFYCODE = "code";
	
	public static final String PARA_SINAID = "sid";
	public static final String PARA_QQID = "qid";
	public static final String PARA_FACEBOOKID = "fid";
	public static final String PARA_RENRENID = "rid";
	public static final String PARA_TWITTERID = "tid";
	public static final String PARA_QUERY = "q";
	public static final String PAPA_STARTDATE = "sd";
	public static final String PAPA_ENDDATE = "ed";

	public static final String PARA_MOBILE = "mb";
	public static final String PARA_EMAIL = "em";
	public static final String PARA_NEW_EMAIL = "nem";
	
	public static final String PARA_DEVICEID = "did";
	public static final String PARA_DEVICETYPE = "dty";
	public static final String PARA_DEVICEMODEL = "dm";
	public static final String PARA_DEVICEOS = "dos";
	public static final String PARA_DEVICETOKEN = "dto";
	public static final String PARA_NICKNAME = "nn";

	public static final String PARA_NEED_RETURN_USER = "r";
	public static final String PARA_ACCESS_TOKEN = "at";
	public static final String PARA_ACCESS_TOKEN_SECRET = "ats";
	public static final String PARA_AVATAR = "av";

	public static final String PARA_COUNTRYCODE = "cc";
	public static final String PARA_LANGUAGE = "lang";
	public static final String PARA_APPID = "app";

	public static final String PARA_RADIUS = "ra";
	public static final String PARA_POSTTYPE = "pt";
	public static final String PARA_NAME = "na";
	public static final String PARA_DESC = "de";
	public static final String PARA_AFTER_TIMESTAMP = "at";
	public static final String PARA_BEFORE_TIMESTAMP = "bt";
	public static final String PARA_MAX_COUNT = "mc";

	public static final String PARA_TOTAL_VIEW = "tv";
	public static final String PARA_TOTAL_FORWARD = "tf";
	public static final String PARA_TOTAL_QUOTE = "tq";
	public static final String PARA_TOTAL_REPLY = "tr";
	public static final String PARA_TOTAL_RELATED = "trl";	
	public static final String PARA_CREATE_DATE = "cd";
	public static final String PARA_SEQ = "sq";

	public static final String PARA_POSTID = "pi";
	public static final String PARA_IMAGE_URL = "iu";
	public static final String PARA_CONTENT_TYPE = "ct";
	public static final String PARA_TEXT_CONTENT = "t";
	public static final String PARA_USER_LATITUDE = "ula";
	public static final String PARA_USER_LONGITUDE = "ulo";
	public static final String PARA_SYNC_SNS = "ss";
	public static final String PARA_SRC_POSTID = "spi";
	public static final String PARA_EXCLUDE_POSTID = "epi";

	public static final String PARA_PLACEID = "pid";

	public static final String PARA_REPLY_POSTID = "rpi";

	public static final String PARA_SINA_ACCESS_TOKEN = "sat";
	public static final String PARA_SINA_ACCESS_TOKEN_SECRET = "sats";
	public static final String PARA_QQ_ACCESS_TOKEN = "qat";
	public static final String PARA_QQ_ACCESS_TOKEN_SECRET = "qats";

	public static final String PARA_PROVINCE = "pro";
	public static final String PARA_CITY = "ci";
	public static final String PARA_LOCATION = "lo";
	public static final String PARA_GENDER = "ge";
	public static final String PARA_BIRTHDAY = "bi";
	public static final String PARA_SINA_NICKNAME = "sn";
	public static final String PARA_SINA_DOMAIN = "sd";
	public static final String PARA_QQ_NICKNAME = "qn";
	public static final String PARA_QQ_DOMAIN = "qd";
	public static final String PARA_DOMAIN = "do";

	public static final String PARA_CREATE_USERID = "cuid";

	public static final String PARA_STATUS = "s";

	public static final String PARA_TIMESTAMP = "ts";
	public static final String PARA_MAC = "mac";

	public static final String PARA_DATA = "dat";

	public static final String PARA_LONGITUDE = "lo";
	public static final String PARA_LATITUDE = "lat";
	public static final String PAPA_RADIUS = "r";
	public static final String PARA_MESSAGETEXT = "t";

	public static final String PARA_VERSION = "v";

	public static final String PARA_TO_USERID = "tuid";
	public static final String PARA_MESSAGE_ID = "mid";
	
	public static final String PARA_POST_ACTION_TYPE = "pat";
	
	public static final String PARA_COMPAREWORD = "cw";
	
	// response parameters

	public static final String RET_MESSAGE = "msg";
	public static final String RET_CODE = "ret";
	public static final String RET_DATA = "dat";
	
	//app service response parameters

	public static final String PARA_APPURL = "au";
	public static final String PARA_ICON = "ai";
	public static final String PARA_SINA_APPKEY = "sak";
	public static final String PARA_SINA_APPSECRET = "sas";
	public static final String PARA_QQ_APPKEY = "qak";
	public static final String PARA_QQ_APPSECRET = "qas";
	public static final String PARA_RENREN_APPKEY = "rak";
	public static final String PARA_RENREN_APPSECRET = "ras";
	public static final String PARA_MESSAGE_TYPE = "mt";

	public static final int DEFAULT_MAX_COUNT = 30;
	
	public static final String PARA_GPS = "gps";
	public static final String PARA_KEYWORDS = "kw";
	public static final String PARA_CATEGORIES = "ctg";
	
	public static final String PRAR_START_OFFSET = "so";
	public static final String PARA_MAX_DISTANCE = "md";
	public static final String PARA_TODAY_ONLY = "to";
	public static final String PARA_SORT_BY = "sb";
	public static final String PARA_KEYWORD = "kw";
	public static final String PARA_START_PRICE = "sp";
	public static final String PARA_END_PRICE = "ep";
	public static final String PARA_SEGMENT = "sg";
	
	public static final String METHOD_FINDPRODUCTSWITHPRICE = "fpp";
	public static final String METHOD_FINDPRODUCTSWITHREBATE = "fpd";
	public static final String METHOD_FINDPRODUCTSWITHBOUGHT = "fpb";
	public static final String METHOD_FINDPRODUCTSWITHLOCATION = "fpl";
	public static final String METHOD_FINDPRODUCTSWITHCATEGORY = "fpc";
	public static final String METHOD_FINDPRODUCTSGROUPBYCATEGORY = "fgc";	
	public static final String METHOD_FINDPRODUCTS = "fp";
	public static final String METHOD_FINDPRODUCTBYSCORE = "fps";
	public static final String METHOD_FINDPRODUCTBYSHOPPINGITEM = "fpsi";

	
	//response parameters
	public static final String PARA_LOC = "loc";
	public static final String PARA_IMAGE= "img";
	public static final String PARA_TITLE = "tt";
	public static final String PARA_START_DATA = "sd";
	public static final String PARA_END_DATA = "ed";
	public static final String PARA_PRICE = "pr";
	public static final String PARA_VALUE = "val";
	public static final String PARA_BOUGHT = "bo";
	public static final String PARA_SITE_ID = "si";
	public static final String PARA_SITE_NAME = "sn";
	public static final String PARA_SITE_URL = "su";
	public static final String PARA_ID = "_id";
	public static final String PARA_ADDRESS = "add";
	public static final String PARA_DETAIL = "dt";
	public static final String PARA_REBATE = "rb";
	public static final String PARA_WAP_URL = "wu";
	public static final String PARA_TEL = "te";
	public static final String PARA_SHOP = "sh";
	public static final String PARA_UP = "up";
	public static final String PARA_DOWN = "down";
	public static final String PARA_TOP_SCORE = "ts";
	
	public static final String PARA_ITEMID = "ii";
	public static final String PARA_CATEGORY_NAME = "na";
	public static final String PARA_SUB_CATEGORY = "sc";
	public static final String PARA_CATEGORY_ID = "ci";
	public static final String PARA_PRODUCT = "p";
	public static final String PARA_EXPIRE_DATE = "e_date";
	public static final String PARA_MATCH_ITEM_COUNT = "mic";
	
	public static final String PARA_CATEGORY_PRODUCTS_NUM = "cpn";

	public static final String PARA_ACTION_NAME = "an";
	public static final String PARA_ACTION_VALUE = "av";
	
    public static final String PARA_COMMENT_CONTENT = "comc";
    
    public static final String PARA_SUB_CATEGORY_ID = "sid";
    public static final String PARA_SUB_CATEGORY_NAME = "scn";
    public static final String PARA_SUB_CATEGORY_KEYS = "keys";
    public static final String PARA_ITEMID_ARRAY = "iia";
    public static final String PARA_REQUIRE_MATCH = "rm";
    
    public static final String PARA_REGISTER_TYPE = "rt";

    public static final String VERIFICATION = "1";
    public static final String NEED_REQURIE_MATCH = "1";


    public static final int REGISTER_TYPE_EMAIL     = 1;
    public static final int REGISTER_TYPE_SINA      = 2;
    public static final int REGISTER_TYPE_QQ        = 3;
    public static final int REGISTER_TYPE_RENREN    = 4;
    
    public static final int PARA_PASSWORD_MIN_LENGTH  = 6;
    
    
}
