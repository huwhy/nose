package cn.huwhy.nose.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.comblife.athena.common.util.CollectionUtil;

import cn.huwhy.nose.dao.ItemContentDao;
import cn.huwhy.nose.dao.ItemDao;
import cn.huwhy.nose.dao.SkuDao;
import cn.huwhy.nose.model.Item;
import cn.huwhy.nose.model.ItemContent;
import cn.huwhy.nose.model.Sku;

@Service
public class ItemBiz {

    @Autowired
    private ItemDao        itemDao;
    @Autowired
    private ItemContentDao itemContentDao;
    @Autowired
    private SkuDao         skuDao;

    @Transactional
    public void save(Item item) {
        if (item.getTbId() != null) {
            Item itemDb = itemDao.getByTbId(item.getTbId());
            if (itemDb != null) {
                item.setId(itemDb.getId());
            }
        }
        if (item.getId() == null) {
            item.setId(itemDao.nextId());
        }
        itemDao.save(item);
        itemContentDao.save(new ItemContent(item.getId(), item.getContent()));
        if (CollectionUtil.isNotEmpty(item.getSkuList())) {
            List<Sku> skuList = item.getSkuList();
            for (Sku sku : skuList) {
                sku.setItemId(item.getId());
                sku.setShopId(item.getShopId());
                sku.setId(skuDao.nextId());
                sku.setDeleted(false);
            }
            skuDao.delSkuList(item.getId());
            skuDao.saves(skuList);
        }
    }
}
