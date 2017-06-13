package cn.huwhy.nose.model;

import java.io.Serializable;

/**
 * @author huwhy
 * @data 16/10/8
 * @Desc 商品内容
 */
public class ItemContent implements Serializable {
    private Long id;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
