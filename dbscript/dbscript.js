
// old apiURL

// 美团 tuan800
db.task.insert({"status":0, "site_id":"meituan", "url":"http://www.meituan.com/api/deals/tuan800"})

// 拉手 tuan800
db.task.insert({"status":0, "site_id":"lashou", "url":"http://open.client.lashou.com/w17"})

// 星800 hao123
db.task.insert({"status":0, "site_id":"xing800", "url":"http://api.xing800.com/api?id=standard"})

// 窝窝团 hao123
db.task.insert({"status":0, "site_id":"wowo", "url":"http://www.55tuan.com/hao123v2.xml"})

// 高朋 hao123
db.task.insert({"status":0, "site_id":"gaopeng", "url":"http://www.tuan1212.com/gaopeng.php"})

// 好特会 hao123
db.task.insert({"status":0, "site_id":"haotehui", "url":"http://www.haotehui.com/data/xml?from=api&format=standard"})

// 新浪 hao123
db.task.insert({"status":0, "site_id":"sina", "url":"http://life.sina.com.cn/tuan/api/"})

// 爱帮团 hao123
db.task.insert({"status":0, "site_id":"aibang", "url":"http://tuan.aibang.com/api/digest"})

// QQ团购 hao123
db.task.insert({"status":0, "site_id":"qq", "url":"http://tuan.qq.com/api/deal/list/"})

// 聚美优品 hao123
db.task.insert({"status":0, "site_id":"jumeiyoupin", "url":"http://www.jumei.com/api/dealsv3.php"})

// 24券 tuan800
db.task.insert({"status":0, "site_id":"24quan", "url":"http://www.24quan.com/api/tuan800.php"})

// 京东 tuan800
db.task.insert({"status":0, "site_id":"jingdong", "url":"http://tuan.360buy.com/api/tuan800.php"})

// 搜狐 tuan800
db.task.insert({"status":0, "site_id":"sohu", "url":"http://tuan.sohu.com/api/today/shanghai/life/tuan800"})

// 58 tuan800
db.task.insert({"status":0, "site_id":"58", "url":"http://open.t.58.com/api/tuan800"})

// 糯米 tuan800
db.task.insert({"status":0, "site_id":"nuomi", "url":"http://www.nuomi.com/api/tuan800"})

// F团 hao123
db.task.insert({"status":0, "site_id":"ftuan", "url":"http://newapi.ftuan.com/api/v2.aspx"})

// 满座 tuan800
db.task.insert({"status":0, "site_id":"manzuo", "url":"http://api.manzuo.com/tuan800.xml"})

// 团好 tuan800
db.task.insert({"status":0, "site_id":"tuanhao", "url":"http://tuanok.com/api/tuan800.php"})

// 团宝 hao123
db.task.insert({"status":0, "site_id":"tuanbao", "url":"http://p5.groupon.cn/xml/city/cityproduct/hao123/"})

// 团好 tuan800
db.task.insert({"status":0, "site_id":"dida", "url":"http://www.didatuan.com/api/tuan800.php"})

// 开心 hao123
db.task.insert({"status":0, "site_id":"kaixin", "url":"http://tuan.kaixin001.com/api.php"})

// 聚齐 tuan800
db.task.insert({"status":0, "site_id":"juqi", "url":"http://biz.juqi.com:8088/api/17api.aspx"})

// 饭统 hao123
db.task.insert({"status":0, "site_id":"fantong", "url":"http://tuan.fantong.com/api/recruits/hao123"})

// 赶集 hao123
db.task.insert({"status":0, "site_id":"ganji", "url":"http://tuan.ganji.com/api/deals/hao123"})

// 去哪儿 hao123
db.task.insert({"status":0, "site_id":"qunaer", "url":"http://tuan.qunar.com/api/hao123.php"})

// 乐淘 hao123
db.task.insert({"status":0, "site_id":"letao", "url":"http://www.letao.com/letaozu/3rd_pages/Tuan/TuanGouAPI.aspx"})

// Z团 tuan800
db.task.insert({"status":0, "site_id":"ztuan", "url":"http://tuan.zol.com/api/zolTuan.php"})

// 粉团 hao123
db.task.insert({"status":0, "site_id":"fentuan", "url":"http://www.fentuan.com/api/baidu.php"})


// product的索引表 table product index;
db.product.ensureIndex({"gps":"2d"})
db.product.ensureIndex({"city":1,"cate":1,"e_date":1,"s_date":-1})
db.product.ensureIndex({"cate":1,"e_date":1,"s_date":-1})
db.product.ensureIndex({"city":1,"loc":1})

// keep the follwing three index for future usage
//db.product.ensureIndex({"city":1,"rebate":-1})
//db.product.ensureIndex({"city":1,"price":1})
//db.product.ensureIndex({"city":1,"bought":-1})

// address的索引表 table address index;
db.product.ensureIndex({"gps":1})
db.address.ensureIndex({"addr":1})

