CREATE TABLE `item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `tb_id` bigint(20) NOT NULL COMMENT '淘商品ID',
  `first_cat_id` int(11) DEFAULT '0' COMMENT '一级类目',
  `second_cat_id` int(11) DEFAULT '0' COMMENT '二级类目',
  `third_cat_id` int(11) DEFAULT '0' COMMENT '三级类目',
  `shop_id` bigint(20) DEFAULT NULL COMMENT '店铺ID',
  `shop_status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT 'ONLINE' COMMENT '卖家状态',
  `title` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品标题',
  `sub_title` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品属性描述',
  `props` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `main_img` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品主图',
  `images` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '产品图片',
  `status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态 ONLINE-在线， OFFLINE-已下架，30-冻结',
  `sale_num` int(11) DEFAULT '0' COMMENT '销量',
  `low_price` int(11) DEFAULT '0' COMMENT '最低价(分) 冗余字段',
  `high_price` int(11) DEFAULT '0' COMMENT '最高价(分) 冗余字段',
  `low_market_price` int(11) DEFAULT '0' COMMENT '最低市场价(分) 冗余字段',
  `high_market_price` int(11) DEFAULT '0' COMMENT '最高市场价(分) 冗余字段',
  `total_stock` bigint(20) DEFAULT '0' COMMENT '总库存 冗余字段',
  `modified` datetime DEFAULT NULL COMMENT '更新时间',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

CREATE TABLE `item_content` (
  `id` bigint(20) NOT NULL,
  `content` mediumtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `shop` (
  `id` bigint(20) NOT NULL COMMENT '店铺ID',
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `name` varchar(90) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '店铺名',
  `status` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT 'ONLINE' COMMENT '状态 ONLINE  OFFLINE',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺表';

DROP TABLE  IF EXISTS `sync_item`;
CREATE TABLE `sync_item` (
  `id` bigint(20) NOT NULL COMMENT '商品ID',
  `ok` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否同步OK',
  `url` varchar(255) null,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品同步表';

DROP TABLE  IF EXISTS `sync_shop`;
CREATE TABLE `sync_shop` (
  `id` bigint(20) NOT NULL COMMENT '店铺ID',
  `url` varchar(255) null,
  `locked` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否停止同步',
  `last_sync_time` datetime DEFAULT NULL COMMENT '上次同步时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='店铺同步表';

DROP TABLE  IF EXISTS `sku`;
CREATE TABLE `sku` (
  `id` bigint(20) NOT NULL COMMENT 'SKUID',
  `item_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '产品ID',
  `shop_id` bigint(20) DEFAULT NULL COMMENT 'SHOPID',
  `spec` varchar(30) NOT NULL COMMENT '规格描述',
  `market_price` int(11) DEFAULT NULL COMMENT '市场价(分)',
  `price` int(11) DEFAULT NULL COMMENT '售价(分)',
  `stock` int(11) DEFAULT '0' COMMENT '库存',
  `deleted` bit(1) DEFAULT b'0' COMMENT '是否被删除',
  `barcode` varchar(32) DEFAULT NULL COMMENT 'SKU编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品SKU表';

DROP table IF EXISTS `sequence`;
CREATE TABLE `sequence` (
  `name` varchar(50) NOT NULL,
  `current_value` bigint(20) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY(`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC COMMENT='序列表，命名s_[table_name]';

drop function if exists nextval;
delimiter //
CREATE FUNCTION `nextval` (`seq_name` varchar(100))
  RETURNS bigint(20) NOT DETERMINISTIC
  BEGIN
    DECLARE cur_val bigint(20);
    SELECT
      current_value + increment INTO cur_val
    FROM
      sequence
    WHERE
      `name` = seq_name for update;

    IF cur_val IS NOT NULL THEN
      UPDATE sequence SET current_value = cur_val WHERE `name` = seq_name;
    ELSE
      insert into sequence(`name`, current_value, increment) values (seq_name, 1, 1);
      select 1 into cur_val;
    END IF;

    RETURN cur_val;
  END
//
delimiter ;