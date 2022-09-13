package com.hermes.lotdata.domain;

import com.beicai.common.DateTimeUtil;
import com.hermes.lotdata.entity.LotteryRecordEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestLotteryRecordDomain {

    @Autowired
    private LotteryRecordDomain lotteryRecordDomain;

    @Test
    public void testQueryListByPeriodNumber() {
        String code = "FFK3";
        List<String> periodNumbers = Arrays.asList("202209131399", "202209131400", "202209131401");
        List<LotteryRecordEntity> lotteryRecordEntities = lotteryRecordDomain.queryListByPeriodNumber(code, periodNumbers);
        Assert.assertNotNull("查询开奖记录列表为空", lotteryRecordEntities);
        Assert.assertTrue("查询开奖记录列表无数据", lotteryRecordEntities.size() > 0);
        System.out.println("lotteryRecordEntities = " + lotteryRecordEntities);
    }

    @Test
    public void testPeriodNumber() {
        String periodNumber = "202209131400";//2022-09-13 23:20:00
        Integer number = 1401;

        long millis = number * 60 * 1000;

        DateTimeFormatter origin = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        DateTimeFormatter target = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String format = DateTimeUtil.format(periodNumber, origin, target);

        System.out.println("format = " + format);
    }
}
