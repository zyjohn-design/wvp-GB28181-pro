package com.genersoft.iot.vmp.vmanager.cloudRecord.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "云端录像及其播放、下载地址")
public class CloudRecordUrl {

    @Schema(description = "源文件路径；list-url 接口当前不填充此字段")
    private String filePath;
    @Schema(description = "录像播放地址")
    private String playUrl;
    @Schema(description = "录像下载地址")
    private String downloadUrl;
    @Schema(description = "源文件名；list-url 接口当前不填充此字段")
    private String fileName;
    @Schema(description = "录像记录数据库 ID；play/path 的 recordId 使用此值")
    private int id;

}
