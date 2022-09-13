package com.hermes.lotdata.application.web;

import com.beicai.common.ValidationUtil;
import com.beicai.common.restful.ErrorCode;
import com.beicai.common.restful.Response;
import com.hermes.lotdata.application.web.criteria.RecordRefreshCriteria;
import com.hermes.lotdata.application.web.vo.LotteryRecordRefreshVO;
import com.hermes.lotdata.service.LotteryRecordRefreshService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@RestController
@RequestMapping("/data/refresh")
@Api(value = "彩种开奖记录同步接口", tags = "彩种开奖记录同步接口")
public class LotteryRecordRefreshController {

    @Autowired
    private LotteryRecordRefreshService lotteryRecordRefreshService;

    @GetMapping("/record/html")
    @ApiOperation(value = "彩种开奖记录网络文件同步接口")
    public Response<LotteryRecordRefreshVO> recordRefresh(@SpringQueryMap RecordRefreshCriteria criteria) {
        String validate = ValidationUtil.validate(criteria);
        if (StringUtils.isNotBlank(validate)) {
            return Response.error(ErrorCode.PARAM_INVALID, validate);
        }
        return lotteryRecordRefreshService.recordRefresh(criteria);
    }
}
