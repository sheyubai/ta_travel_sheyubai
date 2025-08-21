//package cn.wolfcode.wolf2w.business;
//
//import cn.wolfcode.wolf2w.business.util.HttpUtils;
//import org.apache.http.HttpResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class TestSms {
//    public static void main(String[] args) {
//        String host = "https://smsv2.market.alicloudapi.com";
//        String path = "/sms/sendv2";
//        String method = "POST";
//        String appcode = "0609ce7c32cb4a12bc8f177c13a05380";
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);
//        //根据API的要求，定义相对应的Content-Type
//        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        Map<String, String> querys = new HashMap<String, String>();
//        Map<String, String> bodys = new HashMap<String, String>();
//        bodys.put("mobile", "13019346269");
//        bodys.put("content", "【智能云】您的验证码是423561。如非本人操作，请忽略本短信");
//
//
//        try {
//            /**
//             * 重要提示如下:
//             * HttpUtils请从
//             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
//             * 下载
//             *
//             * 相应的依赖请参照
//             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
//             */
//            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
//            //获取response的body
//            //System.out.println(EntityUtils.toString(response.getEntity()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//}
