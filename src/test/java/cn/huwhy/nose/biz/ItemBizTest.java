package cn.huwhy.nose.biz;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.huwhy.nose.BaseTest;
import cn.huwhy.nose.cons.ItemStatus;
import cn.huwhy.nose.model.Item;

public class ItemBizTest extends BaseTest {

    @Autowired
    private ItemBiz itemBiz;

    @Test
    public void save() throws Exception {
        Item po = new Item();
//        po.setTbId(0L);
        po.setTitle("测试商品");
        po.setContent("asfdadf");
        po.setTitle("测试");
        po.setProps("hello2");
        po.setMainImg("http://www.main2.com");
        po.setShopId(112L);
        po.setSaleNum(0);
        po.setCreated(new Date());
        po.setStatus(ItemStatus.ONLINE);
        po.setContent("text");
        itemBiz.save(po);
    }

}