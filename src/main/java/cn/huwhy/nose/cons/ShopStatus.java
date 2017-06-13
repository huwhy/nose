package cn.huwhy.nose.cons;

import com.comblife.interfaces.EnumValue;

public enum ShopStatus implements EnumValue<String> {
    ONLINE,
    OFFLINE;

    @Override
    public String getValue() {
        return name();
    }
}
