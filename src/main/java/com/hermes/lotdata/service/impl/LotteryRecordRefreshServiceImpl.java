package com.hermes.lotdata.service.impl;

import com.beicai.common.DateTimeUtil;
import com.beicai.common.restful.Response;
import com.hermes.lotdata.application.web.criteria.RecordRefreshCriteria;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import com.hermes.lotdata.domain.LotteryRecordRefreshDomain;
import com.hermes.lotdata.service.LotteryRecordRefreshService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@Service
@Slf4j
public class LotteryRecordRefreshServiceImpl implements LotteryRecordRefreshService {

    @Autowired
    private LotteryRecordRefreshDomain lotteryRecordRefreshDomain;

    @Override
    public Response<LotteryRecordRefreshVO> recordRefresh(RecordRefreshCriteria criteria) {
        String code = criteria.getCode();
        String date = "";//yyyy-MM-dd
        if (StringUtils.isBlank(criteria.getDate())) {
            date = DateTimeUtil.nowStr(DateTimeUtil.yyyy_MM_dd);
        } else {
            date = criteria.getDate();
        }
        LotteryRecordRefreshVO lotteryRecordRefreshVO = lotteryRecordRefreshDomain.refreshTask(code, date);
        return Response.ok(lotteryRecordRefreshVO);
    }
}
