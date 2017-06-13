package cn.huwhy.nose.cons;

import cn.huwhy.interfaces.EnumValue;

public enum ShopStatus implements EnumValue<String> {
    ONLINE,
    OFFLINE;

    @Override
    public String getValue() {
        return name();
    }
}
