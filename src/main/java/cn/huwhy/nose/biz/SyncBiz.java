package cn.huwhy.nose.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.huwhy.interfaces.Paging;
import cn.huwhy.nose.ItemTerm;
import cn.huwhy.nose.ShopTerm;
import cn.huwhy.nose.dao.SyncItemDao;
import cn.huwhy.nose.dao.SyncShopDao;
import cn.huwhy.nose.model.SyncItem;
import cn.huwhy.nose.model.SyncShop;

@Service
public class SyncBiz {
    @Autowired
    private SyncItemDao syncItemDao;
    @Autowired
    private SyncShopDao syncShopDao;

    public Paging<SyncItem> findSyncItems(ItemTerm term) {
        List<SyncItem> list = syncItemDao.findPaging(term);
        return new Paging<>(term, list);
    }

    public void save(SyncItem si) {
        syncItemDao.save(si);
    }

    public Paging<SyncShop> findShops(ShopTerm term) {
        List<SyncShop> list = syncShopDao.findPaging(term);
        return new Paging<>(term, list);
    }
}