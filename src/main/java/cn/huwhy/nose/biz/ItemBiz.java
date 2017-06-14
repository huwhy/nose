package cn.huwhy.nose.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.huwhy.nose.dao.ItemContentDao;
import cn.huwhy.nose.dao.ItemDao;
import cn.huwhy.nose.dao.SkuDao;
import cn.huwhy.nose.model.Item;
import cn.huwhy.nose.model.ItemContent;

@Service
public class ItemBiz {

    @Autowired
    private ItemDao        itemDao;
    @Autowired
    private ItemContentDao itemContentDao;
    @Autowired
    private SkuDao skuDao;

    @Transactional
    public void save(Item item) {
        if (item.getId() == null) {
            item.setId(itemDao.nextId());
        }
        itemDao.save(item);
        itemContentDao.save(new ItemContent(item.getId(), item.getContent()));
        skuDao.saves(item.getSkuList());
    }
}
