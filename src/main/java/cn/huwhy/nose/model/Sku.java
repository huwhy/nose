package cn.huwhy.nose.model;

import java.io.Serializable;

public class Sku implements Serializable {
    /**
     * sku id
     */
    private Long id;

    /**
     * 所属Item id
     */
    private Long itemId;

    /**
     * 卖家Id
     */
    private Long shopId;

    /**
     * 规格
     */
    private String spec;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 市场价(分)
     */
    private Integer marketPrice;
    /**
     * 售价(分)
     */
    private Integer price;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 同否删除
     */
    private Boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Integer marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
