package cn.huwhy.nose.task;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.huwhy.interfaces.Json;
import cn.huwhy.interfaces.Paging;
import cn.huwhy.nose.ItemTerm;
import cn.huwhy.nose.biz.ItemBiz;
import cn.huwhy.nose.biz.ItemImportService;
import cn.huwhy.nose.biz.SyncBiz;
import cn.huwhy.nose.cons.ItemStatus;
import cn.huwhy.nose.model.Item;
import cn.huwhy.nose.model.SyncItem;

@Component
public class SyncItemTask {

    @Autowired
    private SyncBiz syncBiz;
    @Autowired
    private ItemBiz itemBiz;
    @Autowired
    private ItemImportService itemImportService;

    @Scheduled(cron = "0 0/2 22 * * *")
    public void sync() {
        ItemTerm term = new ItemTerm();
        term.setPage(1L);
        term.setSize(15L);
        Paging<SyncItem> paging = syncBiz.findSyncItems(term);
        do {
            for (SyncItem si : paging.getData()) {
                try {
                    Json<Item> json = itemImportService.importAliItem(si.getUrl());
                    if (json.isOk()) {
                        Item item = json.getData();
                        item.setTbId(si.getId());
                        item.setStatus(ItemStatus.ONLINE);
                        si.setOk(true);
                        syncBiz.save(si);
                        itemBiz.save(item);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                si.setOk(true);
            }
            term.setPage(term.getPage() + 1);
        } while (term.getPage() <= paging.getTotalPage());
    }
}
