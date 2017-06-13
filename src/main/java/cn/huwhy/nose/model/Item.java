package cn.huwhy.nose.model;

import java.io.Serializable;
import java.util.Date;

import cn.huwhy.nose.cons.ItemStatus;
import cn.huwhy.nose.cons.ShopStatus;

/**
 * 商品
 */
public class Item implements Serializable {
    /**
     * id
     */
    private Long       id;
    /**
     * 淘id
     */
    private Long       tbId;
    /**
     * 一级类目
     */
    private Integer    firstCatId;
    /**
     * 二级类目
     */
    private Integer    secondCatId;
    /**
     * 三级类目
     */
    private Integer    thirdCatId;
    /**
     * SHOP Id
     */
    private Long       shopId;
    /**
     * 商铺状态
     */
    private ShopStatus shopStatus;
    /**
     * 标题
     */
    private String     title;
    /**
     * 副标题
     */
    private String     sub_title;
    /**
     * 属性
     */
    private String     props;
    /**
     * 商品主图
     */
    private String     mainImg;
    /**
     * 商品图片
     */
    private String     images;
    /**
     * 状态
     */
    private ItemStatus status;
    /**
     * 商品销量
     */
    private int        saleNum;
    /**
     * 蜂巢最低价
     */
    private Integer    lowPrice;
    /**
     * 蜂巢最高价
     */
    private Integer    highPrice;
    /**
     * 市场最低价
     */
    private Integer    lowMarketPrice;
    /**
     * 市场最高价
     */
    private Integer    highMarketPrice;
    /**
     * 总库存
     */
    private Long       totalStock;
    /**
     * 商品更新时间
     */
    private Date       modified;
    /**
     * 商品创建时间
     */
    private Date       created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTbId() {
        return tbId;
    }

    public void setTbId(Long tbId) {
        this.tbId = tbId;
    }

    public Integer getFirstCatId() {
        return firstCatId;
    }

    public void setFirstCatId(Integer firstCatId) {
        this.firstCatId = firstCatId;
    }

    public Integer getSecondCatId() {
        return secondCatId;
    }

    public void setSecondCatId(Integer secondCatId) {
        this.secondCatId = secondCatId;
    }

    public Integer getThirdCatId() {
        return thirdCatId;
    }

    public void setThirdCatId(Integer thirdCatId) {
        this.thirdCatId = thirdCatId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public ShopStatus getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(ShopStatus shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public int getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(int saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Integer lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Integer getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Integer highPrice) {
        this.highPrice = highPrice;
    }

    public Integer getLowMarketPrice() {
        return lowMarketPrice;
    }

    public void setLowMarketPrice(Integer lowMarketPrice) {
        this.lowMarketPrice = lowMarketPrice;
    }

    public Integer getHighMarketPrice() {
        return highMarketPrice;
    }

    public void setHighMarketPrice(Integer highMarketPrice) {
        this.highMarketPrice = highMarketPrice;
    }

    public Long getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Long totalStock) {
        this.totalStock = totalStock;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
