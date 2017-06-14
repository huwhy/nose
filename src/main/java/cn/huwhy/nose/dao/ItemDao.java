package cn.huwhy.nose.dao;

import org.apache.ibatis.annotations.Param;

import cn.huwhy.ibatis.BaseDao;
import cn.huwhy.nose.model.Item;

public interface ItemDao extends BaseDao<Item, Long> {
    Item getByTbId(@Param("tbId") Long tbId);
}
