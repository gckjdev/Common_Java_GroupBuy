// old apiURL
db.task.insert({"status":0, "site_id":"meituan", "url":"http://www.meituan.com/api/deals/tuan800"})
db.task.insert({"status":0, "site_id":"lashou", "url":"http://open.client.lashou.com/v1/hao123"})
db.task.insert({"status":0, "site_id":"xing800", "url":"http://api.xing800.com/api?id=standard"})
db.task.insert({"status":0, "site_id":"wowo", "url":"http://www.55tuan.com/hao123v2.xml"})
db.task.insert({"status":0, "site_id":"gaopeng", "url":"http://www.tuan1212.com/gaopeng.php"})
db.task.insert({"status":0, "site_id":"haotehui", "url":"http://www.haotehui.com/data/xml?from=api&format=standard"})
db.task.insert({"status":0, "site_id":"sina", "url":"http://life.sina.com.cn/tuan/api/"})
db.task.insert({"status":0, "site_id":"aibang", "url":"http://tuan.aibang.com/api/digest"})
db.task.insert({"status":0, "site_id":"qq", "url":"http://tuan.qq.com/api/deal/list/"})
// update new apiURL
db.task.insert({"status":0, "site_id":"jumeiyoupin", "url":"http://www.jumei.com/api/dealsv3.php"})
db.task.insert({"status":0, "site_id":"24quan", "url":"http://www.24quan.com/api/tuan800.php"})
db.task.insert({"status":0, "site_id":"jingdong", "url":"http://tuan.360buy.com/api/tuan800.php"})
db.task.insert({"status":0, "site_id":"sohu", "url":"http://tuan.sohu.com/api/today/shanghai/life/tuan800"}) 
db.task.insert({"status":0, "site_id":"58", "url":"http://open.t.58.com/api/tuan800"})
db.task.insert({"status":0, "site_id":"nuomi", "url":"http://www.nuomi.com/api/tuan800"})
db.task.insert({"status":0, "site_id":"ftuan", "url":"http://newapi.ftuan.com/api/v2.aspx"})
db.task.insert({"status":0, "site_id":"manzuo", "url":"http://api.manzuo.com/tuan800.xml"})
db.task.insert({"status":0, "site_id":"tuanhao", "url":"http://tuanok.com/api/tuan800.php"})
db.task.insert({"status":0, "site_id":"tuanbao", "url":"http://p5.groupon.cn/xml/city/cityproduct/hao123/"})
db.task.insert({"status":0, "site_id":"dida", "url":"http://www.didatuan.com/api/tuan800.php"})
db.task.insert({"status":0, "site_id":"jumeiyoupin", "url":"http://www.jumei.com/api/dealsv3.php"})
db.task.insert({"status":0, "site_id":"kaixin", "url":"http://tuan.kaixin001.com/api.php"})
db.task.insert({"status":0, "site_id":"juqi", "url":"http://biz.juqi.com:8088/api/17api.aspx"})
db.task.insert({"status":0, "site_id":"fantong", "url":"http://tuan.fantong.com/api/recruits/hao123"})
db.task.insert({"status":0, "site_id":"ganji", "url":"http://tuan.ganji.com/api/deals/hao123"})
db.task.insert({"status":0, "site_id":"qunaer", "url":"http://tuan.qunar.com/api/hao123.php"})
db.task.insert({"status":0, "site_id":"letao", "url":"http://www.letao.com/letaozu/3rd_pages/Tuan/TuanGouAPI.aspx"})
db.task.insert({"status":0, "site_id":"ztuan", "url":"http://tuan.zol.com/api/zolTuan.php"})
db.task.insert({"status":0, "site_id":"fentuan", "url":"http://www.fentuan.com/api/baidu.php"})




//table product index;

db.product.ensureIndex({"gps":"2d"})
db.product.ensureIndex({"city":1,"category":1,"end_date":1,"start_date":-1})
db.product.ensureIndex({"category":1,"end_date":1,"start_date":-1})
//db.product.ensureIndex({"city":1,"rebate":-1})
//db.product.ensureIndex({"city":1,"price":1})
//db.product.ensureIndex({"city":1,"bought":-1})
db.product.ensureIndex({"city":1,"loc":1})


//table address index;
db.product.ensureIndex({"gps":1})
db.address.ensureIndex({"address":1},{"unique":true})

