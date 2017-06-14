package cn.huwhy.nose;

import java.util.Date;

import cn.huwhy.interfaces.Term;

public class ShopTerm extends Term {
    private Date lastSyncTime;

    public Date getLastSyncTime() {
        return lastSyncTime;
    }

    public void setLastSyncTime(Date lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }
}
