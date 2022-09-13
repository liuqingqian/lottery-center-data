package com.hermes.lotdata.domain;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotdata.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotdata.domain.rpc.response.LotteryOptResponse;
import com.hermes.lotdata.entity.LotteryRecordEntity;
import org.junit.Assert;
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
public class TestLotteryRecordNetDomain {


    @Autowired
    private LotteryRecordNetDomain lotteryRecordNetDomain;

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Test
    public void testLotteryOpt() {
        String lotCode = "FFK3";

        LotteryOptResponse response = lotteryRecordNetDomain.lotteryOpt(lotCode);

        Assert.assertEquals(response.getMsg(), true, response.isSuccess());
        System.out.println("response= " + response);

    }

    @Test
    public void testLotteryLast() {
        String lotCode = "FFK3";
        String qiHao = "202209131401";

        LotteryLastResponse response = lotteryRecordNetDomain.lotteryLast(lotCode, qiHao);

        Assert.assertEquals(response.getMsg(), true, response.isSuccess());
        System.out.println("response= " + response);
        LotteryLastResponse.Last last = response.getLast();

        String openTime = DateTimeUtil.queryByTimestamp(last.getOpenTime()).format(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);
        String startTime = DateTimeUtil.queryByTimestamp(last.getStartTime()).format(DateTimeUtil.yyyy_MM_dd_HH_mm_ss);

        System.out.println("openTime = " + openTime);
        System.out.println("startTime = " + startTime);

        System.out.println("last = " + last);

        LotteryRecordEntity lotteryRecordEntity = lotteryRecordDomain.toLotteryRecordEntity(last);
        System.out.println("lotteryRecordEntity = " + lotteryRecordEntity);

    }

}
