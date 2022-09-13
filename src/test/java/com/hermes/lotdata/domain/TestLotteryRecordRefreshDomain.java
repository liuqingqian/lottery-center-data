package com.hermes.lotdata.domain;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLotteryRecordRefreshDomain {

    @Autowired
    private LotteryRecordRefreshDomain lotteryRecordRefreshDomain;

    @Test
    public void testRefreshTask() {
        String code = "FFK3";
        LocalDate yesterdayDate = LocalDate.now().minusDays(1L);
        String yesterday = yesterdayDate.format(DateTimeUtil.yyyy_MM_dd);
        LotteryRecordRefreshVO lotteryRecordRefreshVO = lotteryRecordRefreshDomain.refreshTask(code, yesterday);
        System.out.println("lotteryRecordRefreshVO = " + lotteryRecordRefreshVO);
    }

}
