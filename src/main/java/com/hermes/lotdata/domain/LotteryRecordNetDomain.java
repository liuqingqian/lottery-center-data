package com.hermes.lotdata.domain;

import com.hermes.lotdata.domain.rpc.LotteryClient;
import com.hermes.lotdata.domain.rpc.criteria.LotteryLastCriteria;
import com.hermes.lotdata.domain.rpc.criteria.LotteryOptCriteria;
import com.hermes.lotdata.domain.rpc.response.LotteryLastResponse;
import com.hermes.lotdata.domain.rpc.response.LotteryOptResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@Component
public class LotteryRecordNetDomain {

    @Autowired
    private LotteryClient lotteryClient;

    public LotteryOptResponse lotteryOpt(String lotCode) {

        if (StringUtils.isBlank(lotCode)) {
            return null;
        }
        LotteryOptCriteria criteria = new LotteryOptCriteria();
        criteria.setLotCode(lotCode);
        return lotteryClient.lotteryOpt(criteria);
    }

    public LotteryLastResponse lotteryLast(String lotCode, String qiHao) {
        if (StringUtils.isBlank(lotCode) || StringUtils.isBlank(qiHao)) {
            return null;
        }
        LotteryLastCriteria criteria = new LotteryLastCriteria();
        criteria.setLotCode(lotCode);
        criteria.setQiHao(qiHao);

        return lotteryClient.lotteryLast(criteria);
    }
}
