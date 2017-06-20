package cn.huwhy.nose.task;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import cn.huwhy.interfaces.Paging;
import cn.huwhy.nose.biz.ShopImportService;
import cn.huwhy.nose.biz.SyncBiz;
import cn.huwhy.nose.model.SyncShop;
import cn.huwhy.nose.term.ShopTerm;

@Component
public class SyncShopTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SyncBiz             syncBiz;
    @Autowired
    private ShopImportService   shopImportService;
    @Autowired
    private TransactionTemplate txTemplate;

    private static Set<Long> ids = new HashSet<>();

    @Scheduled(cron = "0 0/2 * * * *")
    public void sync() {
        ShopTerm term = new ShopTerm();
        term.setPage(1L);
        term.setSize(15L);
        term.setLastSyncTime(LocalDate.now().minusDays(1).toDate());
        Paging<SyncShop> paging = syncBiz.findShops(term);
        do {
            for (SyncShop si : paging.getData()) {
                if (ids.add(si.getId())) {
                    logger.info("sync shop start tb-id={}", si.getId());
                    try {
                        txTemplate.execute(new TransactionCallbackWithoutResult() {
                            @Override
                            protected void doInTransactionWithoutResult(TransactionStatus status) {
                                try {
                                    shopImportService.importShop(si.getUrl());
                                    si.setLastSyncTime(new Date());
                                    syncBiz.save(si);
                                } catch (IOException e) {
                                    logger.error("import shop error:", e);
                                }
                            }
                        });

                        logger.info("sync shop end tb-id={}, result-{}, msg-{}", si.getId());
                    } catch (Throwable e) {
                        logger.error("sync shop error tb-id=" + si.getId(), e);
                    } finally {
                        ids.remove(si.getId());
                    }
                }
            }
            term.setPage(term.getPage() + 1);
        } while (term.getPage() <= paging.getTotalPage());
    }
}
