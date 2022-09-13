package com.hermes.lotdata.application.web.criteria;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@Data
@ApiModel("开奖记录刷新请求参数")
public class RecordRefreshCriteria {

    @NotNull(message = "彩种代码不能为空")
    @ApiModelProperty("彩种代码")
    private String code;

    @ApiModelProperty("日期")
    private String date;//yyyy-MM-dd
}
