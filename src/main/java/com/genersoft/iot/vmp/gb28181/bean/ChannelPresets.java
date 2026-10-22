package com.genersoft.iot.vmp.gb28181.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "有预置位的设备通道")
public class ChannelPresets {
    @Schema(description = "通道国标编号")
    private final String channelId;
    @Schema(description = "通道名称")
    private final String channelName;
    @Schema(description = "预置位列表，包含平台名称备注")
    private final List<Preset> presets;
}
