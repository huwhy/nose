package cn.huwhy.nose.model;

import java.io.Serializable;
import java.util.Date;

import cn.huwhy.nose.cons.ShopStatus;

public class Shop implements Serializable {
    private Long       id;
    private Integer    customerId;
    private String     name;
    private ShopStatus status;
    private Date       created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopStatus getStatus() {
        return status;
    }

    public void setStatus(ShopStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
