package com.hermes.lotdata.domain.task;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import com.hermes.lotdata.domain.LotteryRecordRefreshDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Created by liuqingqian on 2022/9/12.
 */
@Component
public class LotteryRecordDailyRefreshTask {

    @Autowired
    private LotteryRecordRefreshDomain lotteryRecordRefreshDomain;

    @Scheduled(cron = "10 0 0 * * ?")//每天0点0分10秒执行
    public void refreshTask() {
        System.out.println("[DailyRefreshTask]触发时间 = " + DateTimeUtil.nowStr());
        String lotCode = "FFK3";
        //同步矫正前一天的开奖记录
        LocalDate yesterdayDate = LocalDate.now().minusDays(1L);
        String yesterday = yesterdayDate.format(DateTimeUtil.yyyy_MM_dd);
        System.out.println("yesterday = " + yesterday);
        LotteryRecordRefreshVO recordRefreshVO = lotteryRecordRefreshDomain.refreshTask(lotCode, yesterday);

        System.out.println("recordRefreshVO = " + recordRefreshVO);
    }
}