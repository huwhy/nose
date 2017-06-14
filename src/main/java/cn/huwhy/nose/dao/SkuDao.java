package cn.huwhy.nose.dao;

import org.apache.ibatis.annotations.Param;

import cn.huwhy.ibatis.BaseDao;
import cn.huwhy.nose.model.Sku;

public interface SkuDao extends BaseDao<Sku, Long> {
    void delSkuList(@Param("itemId") Long itemId);
}
