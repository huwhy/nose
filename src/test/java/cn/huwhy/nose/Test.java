package cn.huwhy.nose;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.base.Strings;

public class Test {
    static Pattern p        = Pattern.compile("(item.taobao.com|detail.tmall.com)/item.htm\\?[^id]*id=(\\d+)");
    static Pattern TM_P     = Pattern.compile("\"J_ShopAsynSearchURL\"[\\s\\S]+value=\"(.*)\"");
    static Pattern DOMAIN_P = Pattern.compile("http[s]?://[^\\.]+\\.(tmall.com|taobao.com)");
    static Pattern SHOP_P   = Pattern.compile("\"shopId\":[^\"]*\"(\\d+)\"");
    static Pattern NAME_P   = Pattern.compile("\"user_nick\":[^\"]*\"([^\"]*)\"");

    public static void main(String[] args) throws IOException {
        Map<String, String> urls = tm("https://fly-style.taobao.com/?spm=2013.1.1000126.3.48W6rh");
        System.out.println(urls);
    }

    static Map<String, String> tm(String shopUrl) throws IOException {
        Map<String, String> idUrls = new HashMap<>();
        Matcher dm = DOMAIN_P.matcher(shopUrl);
        Document doc = Jsoup.connect(shopUrl).userAgent("Mozilla").get();
        Matcher matcher = NAME_P.matcher(doc.html());
        if (matcher.find()) {
            System.out.println(URLDecoder.decode(matcher.group(1), "utf-8"));
        }
        String shopName = doc.select("a.shop-name").text();
        if (!Strings.isNullOrEmpty(shopName)) {
            System.out.println(shopName);
        }
//        if (dm.find()) {
//            String baseUrl = dm.group();
//            int page = 1;
//            do {
//                String uri = baseUrl + "/search.htm?search=y&pageNo=" + page;
//                Document doc = Jsoup.connect(uri).userAgent("Mozilla").get();
//                Matcher m = TM_P.matcher(doc.html());
//                if (m.find()) {
//                    String uri2 = m.group(1);
//                    String x = baseUrl + uri2.replaceAll("&amp;", "&");
//                    doc = Jsoup.connect(x).userAgent("Mozilla").get();
//                    if (doc.body().html().contains("item-not-found")) {
//                        break;
//                    }
//                    Matcher mm = p.matcher(doc.html());
//                    int index = 0;
//                    while (mm.find(index)) {
//                        idUrls.put(mm.group(2), mm.group());
//                        index = mm.end();
//                    }
//                }
//                page++;
//            } while (true);
//        }
        return idUrls;
    }

}
