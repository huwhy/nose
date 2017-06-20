package cn.huwhy.nose;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test {
    static Pattern p        = Pattern.compile("(item.taobao.com|detail.tmall.com)/item.htm\\?[^id]*id=(\\d+)");
    static Pattern TM_P     = Pattern.compile("\"J_ShopAsynSearchURL\"[\\s\\S]+value=\"(.*)\"");
    static Pattern DOMAIN_P = Pattern.compile("http[s]?://[^\\.]+\\.(tmall.com|taobao.com)");
    static Pattern SHOP_P = Pattern.compile("\"shopId\":[^\"]*\"(\\d+)\"");
    static Pattern NAME_P = Pattern.compile("\"user_nick\":[^\"]*\"([^\"]*)\"");

    public static void main(String[] args) throws IOException {
        String shopUrl = "https://zhuomeifs.tmall.com/";
        Document doc = Jsoup.connect(shopUrl).userAgent("Mozilla").get();
        Matcher matcher = NAME_P.matcher(doc.html());
        if(matcher.find()) {
            String s = matcher.group();
            System.out.println(s);
            System.out.println(URLDecoder.decode(matcher.group(1), "utf-8"));
        }
        matcher = SHOP_P.matcher(doc.html());
        if(matcher.find()) {
            String s = matcher.group();
            System.out.println(s);
            System.out.println(matcher.group(1));
        }
//        Map<String, String> idUrl;
//        if (shopUrl.matches("http[s]?://[^\\.]+\\.taobao.com")) {
//            idUrl = tb(shopUrl);
//        } else {
//            idUrl = tm(shopUrl);
//        }
//        System.out.println(idUrl);
    }

    static Map<String, String> tm(String shopUrl) throws IOException {
        Map<String, String> idUrls = new HashMap<>();
        Matcher dm = DOMAIN_P.matcher(shopUrl);
        if (dm.find()) {
            String baseUrl = dm.group();
            String uri = baseUrl + "/search.htm?search=y&pageNo=1";
            Document doc = Jsoup.connect(uri).userAgent("Mozilla").get();
            Matcher m = TM_P.matcher(doc.html());
            if (m.find()) {
                String uri2 = m.group(1);
                String x = baseUrl + uri2.replaceAll("&amp;", "&");
                doc = Jsoup.connect(x).userAgent("Mozilla").get();
                Matcher mm = p.matcher(doc.html());
                int index = 0;
                while (mm.find(index)) {
                    idUrls.put(mm.group(2), mm.group());
                    index = mm.end();
                }
            }
        }
        return idUrls;
    }

    static Map<String, String> tb(String shopUrl) throws IOException {
        Map<String, String> idUrls = new HashMap<>();
        Matcher dm = DOMAIN_P.matcher(shopUrl);
        if (dm.find()) {
            String baseUrl = dm.group();
            String uri = baseUrl + "/search.htm?search=y&pageNo=1";
            Document doc = Jsoup.connect(uri).userAgent("Mozilla").get();
            Matcher m = p.matcher(doc.html());
            System.out.println(doc.html());
            int index = 0;
            while (m.find(index)) {
                idUrls.put(m.group(2), m.group());
                index = m.end();
            }
        }
        return idUrls;
    }

}
