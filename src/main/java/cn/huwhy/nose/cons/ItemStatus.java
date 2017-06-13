package cn.huwhy.nose.cons;

import cn.huwhy.interfaces.EnumValue;

public enum ItemStatus implements EnumValue<String> {
    ONLINE,
    OFFLINE,
    LOCK;

    @Override
    public String getValue() {
        return name();
    }
}
