package cn.huwhy.nose.cons;

import cn.huwhy.interfaces.EnumValue;

public enum ShopStatus implements EnumValue<Integer> {
    NORMAL("正常", 1),
    LOCKED("冻结", 2);

    ShopStatus(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    private String  name;
    private Integer value;

    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}