package com.genersoft.iot.vmp.service.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "录制计划项")
public class RecordPlanItem {

    @Schema(description = "计划项数据库ID")
    private int id;

    @Schema(description = "开始分钟数，从当天00:00起，范围0-1439，包含")
    private Integer start;

    @Schema(description = "结束分钟数，从当天00:00起，范围1-1440，不包含")
    private Integer stop;

    @Schema(description = "计划周几执行，1为周一，7为周日")
    private Integer weekDay;

    @Schema(description = "所属计划ID")
    private Integer planId;

}
