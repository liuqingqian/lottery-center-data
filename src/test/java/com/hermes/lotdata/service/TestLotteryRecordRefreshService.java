package com.hermes.lotdata.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.beicai.common.restful.ErrorCode;
import com.beicai.common.restful.Response;
import com.hermes.lotdata.application.web.criteria.RecordRefreshCriteria;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
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
public class TestLotteryRecordRefreshService {
    @Autowired
    private LotteryRecordRefreshService lotteryRecordRefreshService;

    @Test
    public void testRefreshTask() {
        String code = "FFK3";
        String date = "2022-09-13";
        RecordRefreshCriteria criteria = new RecordRefreshCriteria();
        criteria.setCode(code);
        criteria.setDate(date);

        Response<LotteryRecordRefreshVO> response = lotteryRecordRefreshService.recordRefresh(criteria);
        Assert.assertEquals(response.getErrorMsg(), ErrorCode.OK.getCode(), response.getErrorCode());
        System.out.println("response.getResults() = " + response.getResults());
    }
}
