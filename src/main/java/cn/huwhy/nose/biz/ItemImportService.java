package cn.huwhy.nose.biz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.comblife.athena.common.util.CollectionUtil;
import com.comblife.athena.common.util.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import cn.huwhy.interfaces.Json;
import cn.huwhy.nose.model.Item;

@Component
public class ItemImportService {

    private static Pattern TB_SKU_P      = Pattern.compile("skuMap\\s*:\\s*(\\{.*\\})[\\s\\S]*,propertyMemoMap\\s*:\\s*(\\{.*\\})");
    private static Pattern TB_DESC_URL_P = Pattern.compile("\\s*descUrl\\s*:\\s*location.protocol\\s*===\\s*'http:' \\s*\\?\\s*'([^']*)'\\s*");
    private static Pattern TB_IMAGES_P   = Pattern.compile("auctionImages[^:]*:\\s*(\\[[^\\]]+\\])");

    private static Pattern TM_SKU_P        = Pattern.compile("skuList.*:(\\[[^\\]]*\\])[^,]*,.*skuMap[^:]*:[^\\{]*(\\{([^\\{\\}]*\\{[^\\{\\}]*\\}[^\\{\\}]*)*\\})");
    private static Pattern TM_DESC_URL_P   = Pattern.compile("httpsDescUrl\":\"([^\"]*)\"");
    private static Pattern TM_IMAGES_P     = Pattern.compile("default[^:]*:\\s*(\\[[^\\]]+\\])[\\s\\S]*rateConfig");
    private static Pattern TMHK_DESC_URL_P = Pattern.compile("httpsDescUrl[^:]*:[^\"]*\"([^\"]+)\"");

    public Json<Item> importAliItem(String url) throws IOException {
        if (url.matches("http[s]?://\\w+\\.tmall\\.com[\\s\\S]+")) {
            return importTMallItem(url);
        } else if (url.matches("http[s]?://\\w+\\.tmall\\.hk[\\s\\S]+")) {
            return importTMHK(url);
        } else {
            return importTaobaoItem(url);
        }
    }

    private Json<Item> importTaobaoItem(String taobaoItemUrl) throws IOException {
        Document doc = Jsoup.connect(taobaoItemUrl).userAgent("Mozilla").get();

        Item item = new Item();
        Elements titleEs = doc.select(".tb-main-title");
        if (titleEs.size() > 0) {
            item.setTitle(StringUtil.substring(titleEs.first().text(), 0, 32));
        }
        if (StringUtil.isEmpty(item.getTitle())) {
            return Json.ERROR().setMessage("商品标题没取到");
        }
        Elements attrEs = doc.select(".attributes-list li");
        StringBuilder attrSb = new StringBuilder();
        for (int i = 0; i < attrEs.size(); i++) {
            Element e = attrEs.get(i);
            attrSb.append(e.text()).append("\r\n");
        }
        item.setProps(attrSb.toString());

        Elements subTitleEs = doc.select(".tb-subtitle");
        if (subTitleEs.size() > 0) {
            item.setSubTitle(subTitleEs.first().text());
        }

        Elements jsEs = doc.select("script");
        for (int i = 0; i < jsEs.size(); i++) {
            Element e = jsEs.get(i);
            if (Strings.isNullOrEmpty(item.getImages()) && e.html().contains("auctionImages")) {
                List<String> images = new ArrayList<>();
                Matcher m = TB_IMAGES_P.matcher(e.html());
                if (m.find()) {
                    List<String> list = JSON.parseArray(m.group(1), String.class);
                    for (String img : list) {
                        images.add((img.startsWith("http") ? img : "https:" + img));
                    }
                }
                if (CollectionUtil.isEmpty(images)) {
                    return Json.ERROR().setMessage("商品图片没取到");
                }
                item.setMainImg(images.get(0));
                item.setImages(Joiner.on(',').join(images));
            }
            //            if (CollectionUtil.isEmpty(item.getSkuList()) && e.html().contains("Hub.config")) {
            //                Matcher m = TB_SKU_P.matcher(e.html());
            //                Map<String, Map<String, String>> skuMap;
            //                Map<String, String> skuNameMap;
            //                if (m.find()) {
            //                    List<Sku> skuList = new ArrayList<>();
            //                    skuMap = JSON.parseObject(m.group(1), Map.class);
            //                    skuNameMap = JSON.parseObject(m.group(2), Map.class);
            //                    Sku sku = new Sku();
            //                    Map<String, String> skuInfo = Iterables.getFirst(skuMap.values(), null);
            //                    if (skuInfo == null)
            //                        break;
            //                    String skuName = Iterables.getFirst(skuNameMap.values(), "");
            //                    sku.setSpec(skuName);
            //                    BigDecimal price = new BigDecimal(skuInfo.get("price"));
            //                    sku.setMarketPrice(price);
            //                    sku.setPrice(price);
            //                    sku.setStock(Long.valueOf(skuInfo.get("stock")));
            //                    skuList.add(sku);
            //                    item.setSkuList(skuList);
            //                }
            //            }
            if (Strings.isNullOrEmpty(item.getContent()) && e.html().contains("g_config")) {
                Matcher m = TB_DESC_URL_P.matcher(e.html());
                if (m.find()) {
                    item.setContent(getItemContents(m));
                }
            }
        }
        //        if (CollectionUtil.isEmpty(item.getSkuList())) {
        //            Sku sku = new Sku();
        //            sku.setStock(1L);
        //            sku.setMarketPrice(BigDecimal.ZERO);
        //            sku.setPrice(BigDecimal.ZERO);
        //            sku.setSpec("默认");
        //            sku.setDeleted(false);
        //            item.setSkuList(Arrays.asList(sku));
        //        }
        return Json.SUCCESS().setData(item);
    }

    private Json<Item> importTMallItem(String tMallUrl) throws IOException {
        Document doc = Jsoup.connect(tMallUrl).userAgent("Mozilla").get();

        Item item = new Item();
        Elements titleEs = doc.select(".tb-detail-hd h1");
        if (titleEs.size() > 0) {
            item.setTitle(StringUtil.substring(titleEs.first().text(), 0, 32));
        }

        Elements subTitleEs = doc.select(".tb-detail-hd .newp");
        if (subTitleEs.size() > 0) {
            item.setSubTitle(subTitleEs.first().text());
        }

        Elements attrEs = doc.select("#J_AttrUL li");
        StringBuilder attrSb = new StringBuilder();
        for (int i = 0; i < attrEs.size(); i++) {
            Element e = attrEs.get(i);
            attrSb.append(e.text()).append("\r\n");
        }
        item.setProps(attrSb.toString());

        Elements jsEs = doc.select("script");
        for (int i = 0; i < jsEs.size(); i++) {
            Element e = jsEs.get(i);
            if (Strings.isNullOrEmpty(item.getImages())) {
                Matcher m = TM_IMAGES_P.matcher(e.html());
                if (m.find()) {
                    List<String> images = new ArrayList<>();
                    List<String> list = JSON.parseArray(m.group(1), String.class);
                    for (String img : list) {
                        images.add((img.startsWith("http") ? img : "https:" + img));
                    }
                    item.setMainImg(images.get(0));
                    item.setImages(Joiner.on(',').join(images));
                }
            }
            //            if (CollectionUtil.isEmpty(item.getSkuList()) && e.html().contains("skuList")) {
            //                Matcher m = TM_SKU_P.matcher(e.html());
            //                JSONArray skuMap;
            //                Map<String, JSONObject> skuNameMap;
            //                if (m.find()) {
            //                    List<Sku> skuList = new ArrayList<>();
            //                    skuMap = JSON.parseArray(m.group(1));
            //                    skuNameMap = JSON.parseObject(m.group(2), Map.class);
            //                    Sku sku = new Sku();
            //                    JSONObject skuInfo = Iterables.getFirst(skuNameMap.values(), null);
            //                    if (skuInfo == null)
            //                        break;
            //                    String skuName = ((Map<String, String>) skuMap.get(0)).get("names");
            //                    sku.setSpec(skuName);
            //                    BigDecimal price = skuInfo.getBigDecimal("price");
            //                    sku.setMarketPrice(price);
            //                    sku.setPrice(price);
            //                    sku.setStock(skuInfo.getLong("stock"));
            //                    skuList.add(sku);
            //                    item.setSkuList(skuList);
            //                }
            //            }
            if (Strings.isNullOrEmpty(item.getContent()) && e.html().contains("httpsDescUrl")) {
                Matcher m = TM_DESC_URL_P.matcher(e.html());
                if (m.find()) {
                    item.setContent(getItemContents(m));
                }
            }
        }
        if (Strings.isNullOrEmpty(item.getImages())) {
            List<String> images = new ArrayList<>();
            Elements picEs = doc.select(".tb-gallery .tb-thumb li img");
            for (int i = 0; i < (picEs.size() > 5 ? 5 : picEs.size()); i++) {
                Element e = picEs.get(i);
                String src = e.attr("src");
                src = src.startsWith("http") ? "" : "https:" + src;
                src = StringUtil.substring(src, 0, src.indexOf(".jpg") + 4);
                images.add(src);
            }
            item.setMainImg(images.get(0));
            item.setImages(Joiner.on(',').join(images));
        }
        //        if (CollectionUtil.isEmpty(item.getSkuList())) {
        //            Sku sku = new Sku();
        //            sku.setStock(1L);
        //            sku.setMarketPrice(BigDecimal.ZERO);
        //            sku.setPrice(BigDecimal.ZERO);
        //            sku.setSpec("默认");
        //            sku.setDeleted(false);
        //            item.setSkuList(Arrays.asList(sku));
        //        }
        return Json.SUCCESS().setData(item);
    }

    public Json<Item> importTMHK(String url) throws IOException {
        Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
        Item item = new Item();
        Elements titleEs = doc.select(".tb-detail-hd h1");
        if (titleEs.size() > 0) {
            item.setTitle(titleEs.first().text());
        }

        Elements subTitleEs = doc.select(".tb-detail-hd .newp");
        if (subTitleEs.size() > 0) {
            item.setSubTitle(subTitleEs.first().text());
        } else {
            subTitleEs = doc.select(".tb-detail-hd");
            if (subTitleEs.size() > 0)
                item.setSubTitle(subTitleEs.first().children().eq(1).text());
        }

        Elements attrEs = doc.select("#J_AttrUL li");
        StringBuilder attrSb = new StringBuilder();
        for (int i = 0; i < attrEs.size(); i++) {
            Element e = attrEs.get(i);
            attrSb.append(e.text()).append("\r\n");
        }
        item.setProps(attrSb.toString());

        List<String> images = new ArrayList<>();
        Elements picEs = doc.select(".tb-gallery .tb-thumb li img");
        for (int i = 0; i < (picEs.size() > 5 ? 5 : picEs.size()); i++) {
            Element e = picEs.get(i);
            String src = e.attr("src");
            src = src.startsWith("http") ? "" : "https:" + src;
            src = StringUtil.substring(src, 0, src.indexOf(".jpg") + 4);
            images.add(src + "?");
        }
        item.setMainImg(images.get(0));
        item.setImages(Joiner.on(',').join(images));

        Elements jsEs = doc.select("script");
        for (int i = 0; i < jsEs.size(); i++) {
            Element e = jsEs.get(i);
            //            if (e.html().contains("skuList")) {
            //                Matcher m = TM_SKU_P.matcher(e.html());
            //                JSONArray skuMap;
            //                Map<String, JSONObject> skuNameMap;
            //                if (m.find()) {
            //                    List<Sku> skuList = new ArrayList<>();
            //                    skuMap = JSON.parseArray(m.group(1));
            //                    skuNameMap = JSON.parseObject(m.group(2), Map.class);
            //                    Sku sku = new Sku();
            //                    JSONObject skuInfo = Iterables.getFirst(skuNameMap.values(), null);
            //                    if (skuInfo == null)
            //                        break;
            //                    String skuName = ((Map<String, String>) skuMap.get(0)).get("names");
            //                    sku.setSpec(skuName);
            //                    BigDecimal price = skuInfo.getBigDecimal("price");
            //                    sku.setMarketPrice(price);
            //                    sku.setPrice(price);
            //                    sku.setStock(skuInfo.getLong("stock"));
            //                    skuList.add(sku);
            //                    item.setSkuList(skuList);
            //                }
            //            }
            if (e.html().contains("httpsDescUrl")) {
                Matcher m = TMHK_DESC_URL_P.matcher(e.html());
                if (m.find()) {
                    item.setContent(getItemContents(m));
                }
            }
        }
        //        if (CollectionUtil.isEmpty(item.getSkuList())) {
        //            Sku sku = new Sku();
        //            sku.setSpec("默认不发货");
        //            BigDecimal marketPrice = BigDecimal.ZERO;
        //            sku.setMarketPrice(marketPrice);
        //            sku.setPrice(marketPrice);
        //            sku.setStock(0L);
        //            item.setSkuList(Arrays.asList(sku));
        //        }
        return Json.SUCCESS().setData(item);
    }

    private String getItemContents(Matcher m) throws IOException {
        String descUrl = "http:" + m.group(1);
        Document descDoc = Jsoup.connect(descUrl).ignoreContentType(true).get();
        return descDoc.getElementsByTag("body").first().children().html();
//        while (els.hasNext()) {
//            Element el = els.next();
//            if ("img".equals(el.tagName())) {
//                String src = el.attr("src");
//                if (src.endsWith(".gif"))
//                    continue;
//                ItemContent content = new ItemContent();
//                content.setType("IMG");
//                content.setContent(src);
//                contents.add(content);
//            } else if (el.children().size() > 0) {
//                Elements imgEs = el.select("img");
//                if (imgEs.size() > 0) {
//                    for (int j = 0; j < imgEs.size(); j++) {
//                        String src = imgEs.get(j).attr("src");
//                        if (src.endsWith(".gif"))
//                            continue;
//                        ItemContent content = new ItemContent();
//                        content.setType("IMG");
//                        content.setContent(src);
//                        contents.add(content);
//                    }
//                }
//                Elements spanEs = el.select("span");
//                if (spanEs.size() > 0) {
//                    for (int j = 0; j < spanEs.size(); j++) {
//                        ItemContent content = new ItemContent();
//                        content.setType("A");
//                        content.setContent(spanEs.get(j).text());
//                        contents.add(content);
//                    }
//                }
//                Elements pEs = el.select("p");
//                if (pEs.size() > 0) {
//                    for (int j = 0; j < pEs.size(); j++) {
//                        ItemContent content = new ItemContent();
//                        content.setType("A");
//                        content.setContent(pEs.get(j).text());
//                        contents.add(content);
//                    }
//                }
//            } else {
//                ItemContent content = new ItemContent();
//                content.setType("A");
//                content.setContent(el.text());
//                contents.add(content);
//            }
//        }
//        return contents;
    }

}