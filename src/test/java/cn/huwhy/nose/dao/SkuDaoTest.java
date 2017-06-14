package cn.huwhy.nose.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.huwhy.nose.BaseTest;
import cn.huwhy.nose.model.Sku;

public class SkuDaoTest extends BaseTest {

    @Autowired
    private SkuDao skuDao;

    @Test
    public void save() throws Exception {
        List<Sku> skuList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Sku sku = new Sku();
            sku.setId(skuDao.nextId());
            sku.setItemId(1L);
            sku.setMarketPrice(10);
            sku.setPrice(10);
            sku.setShopId(1L);
            sku.setStock(1);
            sku.setSpec("a" + i);
            skuList.add(sku);
        }
        skuDao.saves(skuList);
    }

}