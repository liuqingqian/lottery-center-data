package com.hermes.lotdata.domain;

import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        String date = "2022-09-13";
        LotteryRecordRefreshVO lotteryRecordRefreshVO = lotteryRecordRefreshDomain.refreshTask(code, date);
        System.out.println("lotteryRecordRefreshVO = " + lotteryRecordRefreshVO);
    }

}
