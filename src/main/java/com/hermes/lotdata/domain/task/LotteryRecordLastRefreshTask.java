package com.hermes.lotdata.domain.task;

import com.beicai.common.DateTimeUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import com.hermes.lotdata.domain.LotteryRecordDomain;
import com.hermes.lotdata.domain.LotteryRecordNetDomain;
import com.hermes.lotdata.domain.LotteryRecordRefreshDomain;
import com.hermes.lotdata.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotdata.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotdata.entity.LotteryRecordEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuqingqian on 2022/9/14.
 */
@Slf4j
@Component
public class LotteryRecordLastRefreshTask {

    @Autowired
    private LotteryRecordNetDomain lotteryRecordNetDomain;

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Autowired
    private LotteryRecordRefreshDomain lotteryRecordRefreshDomain;

    //每小时全文件同步一次当天的开奖记录
    private static final int REFRESH_RECORD_GET_GUAVA_EXPIRE = 60;//分钟

    @Scheduled(cron = "5 * * * * ?")
    public void refreshTask() {
        String code = "FFK3";
        log.info("[LastRefreshTask]触发时间 = " + DateTimeUtil.nowStr());
        ifNeedFileRefreshRecord(code);
        LotteryOptResponse lotteryOptResponse = lotteryRecordNetDomain.lotteryOpt(code);
        if (Objects.nonNull(lotteryOptResponse) && lotteryOptResponse.isSuccess()) {
            LotteryOptResponse.Last last = lotteryOptResponse.getLast();
            String qiHao = last.getQiHao();
            LotteryRecordEntity lastRecordEntity = lotteryRecordDomain.query(code, qiHao);
            if (Objects.isNull(lastRecordEntity)) {
                LotteryLastResponse lotteryLastResponse = lotteryRecordNetDomain.lotteryLast(code, qiHao);
                if (Objects.nonNull(lotteryLastResponse) && lotteryLastResponse.isSuccess()) {
                    LotteryLastResponse.Last lastDetail = lotteryLastResponse.getLast();
                    LotteryRecordEntity lotteryRecordEntity = lotteryRecordDomain.toLotteryRecordEntity(lastDetail);
                    int insert = lotteryRecordDomain.insert(lotteryRecordEntity);
                    log.info("lotteryRecordEntity = " + lotteryRecordEntity + ",insert = " + insert);
                }
            }
        }
    }

    private void ifNeedFileRefreshRecord(String code) {
        String date = DateTimeUtil.nowStr(DateTimeUtil.yyyy_MM_dd);
        String cacheKey = code + "_" + date;
        Boolean ifPresent = refreshRecordGuavaCache.getIfPresent(cacheKey);
        if (Objects.isNull(ifPresent)) {
            LotteryRecordRefreshVO lotteryRecordRefreshVO = lotteryRecordRefreshDomain.refreshTask(code, date);
            log.info("[NeedFileRefresh] = " + lotteryRecordRefreshVO);
            refreshRecordGuavaCache.put(cacheKey, true);
        }
    }

    //Cache<Code-Date, Boolean>
    private Cache<String, Boolean> refreshRecordGuavaCache = CacheBuilder.newBuilder()
            .expireAfterWrite(REFRESH_RECORD_GET_GUAVA_EXPIRE, TimeUnit.MINUTES)
            .maximumSize(3).build();
}
