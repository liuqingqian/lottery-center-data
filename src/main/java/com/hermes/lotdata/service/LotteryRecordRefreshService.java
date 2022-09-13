package com.hermes.lotdata.service;

import com.beicai.common.restful.Response;
import com.hermes.lotdata.application.web.criteria.RecordRefreshCriteria;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;

/**
 * Created by liuqingqian on 2022/9/13.
 */
public interface LotteryRecordRefreshService {

    Response<LotteryRecordRefreshVO> recordRefresh(RecordRefreshCriteria criteria);
}
