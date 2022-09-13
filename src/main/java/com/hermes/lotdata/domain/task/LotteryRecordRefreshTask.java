package com.hermes.lotdata.domain.task;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import com.hermes.lotdata.domain.LotteryRecordRefreshDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@Component
public class LotteryRecordRefreshTask {

    @Autowired
    private LotteryRecordRefreshDomain lotteryRecordRefreshDomain;

    @Scheduled(cron = "10 */1 * * * ?")
    public void refreshTask() {
        System.out.println("[RefreshTask]触发时间 = " + DateTimeUtil.nowStr());
        String lotCode = "FFK3";
        String startDate = DateTimeUtil.nowStr(DateTimeUtil.yyyy_MM_dd);
        System.out.println("startDate = " + startDate);
        LotteryRecordRefreshVO recordRefreshVO = lotteryRecordRefreshDomain.refreshTask(lotCode, startDate);

        System.out.println("recordRefreshVO = " + recordRefreshVO);
    }
}
