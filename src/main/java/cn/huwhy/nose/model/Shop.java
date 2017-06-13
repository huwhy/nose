package cn.huwhy.nose.model;

import java.io.Serializable;
import java.util.Date;

import cn.huwhy.nose.cons.ShopStatus;

public class Shop implements Serializable {
    private Long       id;
    private Integer    uid;
    private String     name;
    private ShopStatus status;
    private Date       created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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
