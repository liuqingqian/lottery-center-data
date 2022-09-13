package com.hermes.lotdata.service;

import com.beicai.common.restful.Response;
import com.hermes.lotdata.application.web.criteria.LotteryRecordCriteria;
import com.hermes.lotdata.domain.dto.LotCycleMatchingScoreDTO;

import java.util.List;

/**
 * Created by liuqingqian on 2022/9/12.
 */
public interface AnalysisLotteryRecordService {

    Response<List<LotCycleMatchingScoreDTO>> groupMatchingScore(LotteryRecordCriteria criteria);

}
