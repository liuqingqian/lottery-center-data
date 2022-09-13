package com.hermes.lotdata.domain;

import com.hermes.lotdata.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotdata.domain.rpc.response.LotteryOptResponse;
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
        String qiHao = "202209071316";

        LotteryLastResponse response = lotteryRecordNetDomain.lotteryLast(lotCode, qiHao);

        Assert.assertEquals(response.getMsg(), true, response.isSuccess());
        System.out.println("response= " + response);
    }

}
