package cn.huwhy.nose.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.huwhy.nose.cons.ShopStatus;
import cn.huwhy.nose.model.Shop;
import cn.huwhy.nose.model.SyncItem;

@Component
public class ShopImportService {
    static Pattern p        = Pattern.compile("(item.taobao.com|detail.tmall.com)/item.htm\\?[^id]*id=(\\d+)");
    static Pattern TM_P     = Pattern.compile("\"J_ShopAsynSearchURL\"[\\s\\S]+value=\"(.*)\"");
    static Pattern DOMAIN_P = Pattern.compile("http[s]?://[^\\.]+\\.(tmall.com|taobao.com)");
    static Pattern SHOP_P   = Pattern.compile("\"shopId\":[^\"]*\"(\\d+)\"");
    static Pattern NAME_P   = Pattern.compile("\"user_nick\":[^\"]*\"([^\"]*)\"");

    @Autowired
    private ShopBiz shopBiz;
    @Autowired
    private SyncBiz syncBiz;

    public void importShop(String shopUrl) throws IOException {
        Document doc = Jsoup.connect(shopUrl).userAgent("Mozilla").get();
        Shop shop = new Shop();
        Matcher matcher = NAME_P.matcher(doc.html());
        if (matcher.find()) {
            shop.setName(matcher.group(1));
        }
        matcher = SHOP_P.matcher(doc.html());
        if (matcher.find()) {
            shop.setId(Long.valueOf(matcher.group(1)));
        }
        shop.setStatus(ShopStatus.ONLINE);
        shopBiz.save(shop);

        Map<String, String> idUrls;
        if (shopUrl.matches("http[s]?://[^\\.]+\\.taobao.com")) {
            idUrls = tb(shopUrl);
        } else {
            idUrls = tm(shopUrl);
        }
        List<SyncItem> syncItems = new ArrayList<>(idUrls.size());
        for (Map.Entry<String, String> entry : idUrls.entrySet()) {
            SyncItem si = new SyncItem();
            si.setId(Long.valueOf(entry.getKey()));
            String value = entry.getValue();
            if (!value.startsWith("http")) {
                value = "https://" + value;
            }
            si.setUrl(value);
            si.setOk(false);
            syncItems.add(si);
        }
        syncBiz.save(syncItems);

    }

    private Map<String, String> tm(String shopUrl) throws IOException {
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

    private Map<String, String> tb(String shopUrl) throws IOException {
        Map<String, String> idUrls = new HashMap<>();
        Matcher dm = DOMAIN_P.matcher(shopUrl);
        if (dm.find()) {
            String baseUrl = dm.group();
            int page = 1, cnt;
            String uri = baseUrl + "/search.htm?search=y&pageNo=" + page;
            do {
                cnt = 0;
                Document doc = Jsoup.connect(uri).userAgent("Mozilla").get();
                Matcher m = p.matcher(doc.html());
                System.out.println(doc.html());
                int index = 0;
                while (m.find(index)) {
                    idUrls.put(m.group(2), m.group());
                    index = m.end();
                    cnt++;
                }
                page++;
            } while (cnt > 0);
        }
        return idUrls;
    }

}
