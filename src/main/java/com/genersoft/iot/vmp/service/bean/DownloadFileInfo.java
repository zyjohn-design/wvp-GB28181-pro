package com.genersoft.iot.vmp.service.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "录像文件访问地址")
public class DownloadFileInfo {

    @Schema(description = "HTTP 文件地址")
    private String httpPath;
    @Schema(description = "HTTPS 文件地址")
    private String httpsPath;
    @Schema(description = "带域名的 HTTP 文件地址")
    private String httpDomainPath;
    @Schema(description = "带域名的 HTTPS 文件地址")
    private String httpsDomainPath;

}
