package cn.huwhy.nose.cons;

import com.comblife.interfaces.EnumValue;

public enum ItemStatus implements EnumValue<String> {
    ONLINE,
    OFFLINE,
    LOCK;

    @Override
    public String getValue() {
        return name();
    }
}
