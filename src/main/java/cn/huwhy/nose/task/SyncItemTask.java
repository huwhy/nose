package cn.huwhy.nose.task;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import cn.huwhy.interfaces.Json;
import cn.huwhy.interfaces.Paging;
import cn.huwhy.nose.biz.ItemBiz;
import cn.huwhy.nose.biz.ItemImportService;
import cn.huwhy.nose.biz.SyncBiz;
import cn.huwhy.nose.cons.ItemStatus;
import cn.huwhy.nose.model.Item;
import cn.huwhy.nose.model.Sku;
import cn.huwhy.nose.model.SyncItem;
import cn.huwhy.nose.term.ItemTerm;

@Component
public class SyncItemTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SyncBiz             syncBiz;
    @Autowired
    private ItemBiz             itemBiz;
    @Autowired
    private ItemImportService   itemImportService;
    @Autowired
    private TransactionTemplate txTemplate;

    private static Set<Long> ids = new HashSet<>();

    @Scheduled(cron = "0 0/2 * * * *")
    public void sync() {
        ItemTerm term = new ItemTerm();
        term.setPage(1L);
        term.setSize(15L);
        Paging<SyncItem> paging;
        do {
            paging = syncBiz.findSyncItems(term);
            for (SyncItem si : paging.getData()) {
                if (ids.add(si.getId())) {
                    logger.info("sync item start tb-id={}", si.getId());
                    try {
                        Json<Item> json = itemImportService.importAliItem(si.getUrl());
                        if (json.isOk()) {
                            Integer low = Integer.MAX_VALUE;
                            Integer high = 0;
                            Item item = json.getData();
                            for (Sku sku : item.getSkuList()) {
                                if (low > sku.getPrice()) {
                                    low = sku.getPrice();
                                }
                                if (high < sku.getPrice()) {
                                    high = sku.getPrice();
                                }
                            }
                            item.setHighMarketPrice(high);
                            item.setLowMarketPrice(low);
                            item.setHighPrice(high);
                            item.setLowPrice(low);
                            item.setTotalStock(0L);
                            item.setModified(new Date());
                            syncSave(si, item);
                        }
                        logger.info("sync item end tb-id={}, result-{}, msg-{}",
                                si.getId(), json.isOk(), json.getMessage());
                    } catch (Throwable e) {
                        logger.error("sync item error tb-id=" + si.getId(), e);
                    } finally {
                        ids.remove(si.getId());
                    }
                }
            }
            term.setPage(term.getPage() + 1);
        } while (term.getPage() <= paging.getTotalPage());
    }

    private void syncSave(SyncItem si, Item item) {
        txTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                item.setTbId(si.getId());
                item.setStatus(ItemStatus.ONLINE);
                si.setOk(true);
                syncBiz.save(si);
                itemBiz.save(item);
            }
        });
    }
}
