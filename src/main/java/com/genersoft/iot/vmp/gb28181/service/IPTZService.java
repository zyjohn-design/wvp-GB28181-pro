package com.genersoft.iot.vmp.gb28181.service;


import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.Preset;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;

import java.util.List;

public interface IPTZService {

    void ptz(Device device, String channelId, int cmdCode, int horizonSpeed, int verticalSpeed, int zoomSpeed);

    void frontEndCommand(Device device, String channelId, int cmdCode, int parameter1, int parameter2, int combindCode2);

    void frontEndCommand(CommonGBChannel channel, Integer cmdCode, Integer parameter1, Integer parameter2, Integer combindCode2);

    /** GB/T 28181 A.3.4: 0x81/0x82/0x83, data1=0, data2=presetId; completes on SIP response. */
    void preset(Device device, String channelId, int cmdCode, int presetId, ErrorCallback<String> callback);

    void preset(CommonGBChannel channel, int cmdCode, int presetId, ErrorCallback<String> callback);

    void queryPresetList(CommonGBChannel channel, ErrorCallback<List<Preset>> callback);

    void dragZoomIn(CommonGBChannel channel, int length, int width, int midPointX, int midPointY, int lengthX, int lengthY);

    void dragZoomOut(CommonGBChannel channel, int length, int width, int midPointX, int midPointY, int lengthX, int lengthY);

    void homePosition(CommonGBChannel channel, Boolean enabled, Integer resetTime, Integer presetIndex, ErrorCallback<String> callback);
}
