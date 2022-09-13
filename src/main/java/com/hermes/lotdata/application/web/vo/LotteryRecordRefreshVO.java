package com.hermes.lotdata.application.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by liuqingqian on 2022/9/13.
 */
@Data
@ApiModel("彩种开奖记录更新信息")
public class LotteryRecordRefreshVO {

    @ApiModelProperty("彩种编码")
    private String code;

    @ApiModelProperty("日期")
    private String date;//yyyy-MM-dd

    @ApiModelProperty("文件回写状态")
    private Boolean writeLotFile;

    @ApiModelProperty("文件记录条数")
    private Integer lotFileRecordCount;

    @ApiModelProperty("本次DB新增记录条数")
    private Integer batchInsertCount;

    @ApiModelProperty("DB插入数据状态")
    private Boolean batchInsert;

    @ApiModelProperty("数据同步时间")
    private String syncTime;//yyyy-MM-dd HH:mm:ss

}
