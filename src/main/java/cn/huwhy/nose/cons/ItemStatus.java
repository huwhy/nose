package cn.huwhy.nose.cons;

import cn.huwhy.interfaces.EnumValue;

public enum ItemStatus implements EnumValue<Integer> {
    ON_SALE("在售", 10),
    OFF_SALE("下架", 20),
    LOCK("冻结", 30);

    ItemStatus(String name, Integer value) {
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
