package cn.huwhy.nose.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.huwhy.nose.dao.ShopDao;
import cn.huwhy.nose.model.Shop;

@Service
public class ShopBiz {

    @Autowired
    private ShopDao shopDao;

    public Shop get(Long id) {
        return shopDao.get(id);
    }

    public void save(Shop shop) {
        shopDao.save(shop);
    }

}
