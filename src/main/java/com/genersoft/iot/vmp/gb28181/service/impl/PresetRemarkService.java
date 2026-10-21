package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.DeviceChannel;
import com.genersoft.iot.vmp.gb28181.bean.Preset;
import com.genersoft.iot.vmp.gb28181.dao.PresetRemarkMapper;
import com.genersoft.iot.vmp.gb28181.service.IDeviceChannelService;
import com.genersoft.iot.vmp.gb28181.utils.PresetIdUtils;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/** Platform labels are separate from device preset names and from GB28181 control messages. */
@Service
@Slf4j
public class PresetRemarkService {
    private final PresetRemarkMapper mapper;
    private final IDeviceChannelService channels;

    public PresetRemarkService(PresetRemarkMapper mapper, IDeviceChannelService channels) {
        this.mapper = mapper;
        this.channels = channels;
    }

    public int channelId(Device device, String channelId) {
        DeviceChannel source = channels.getOneForSource(device.getDeviceId(), channelId);
        if (source == null) source = channels.getOneBySourceId(device.getId(), channelId);
        if (source == null) throw new ControllerException(404, "通道不存在");
        return source.getId();
    }

    public static String name(int presetId, String name) {
        if (presetId < 1 || presetId > 255) throw new ControllerException(400, "预置位编号必须为1-255之间的整数");
        String value = name == null ? "" : name.strip();
        if (value.length() > 128) throw new ControllerException(400, "预置位名称备注不能超过128个字符");
        return value.isEmpty() ? "预置点" + presetId : value;
    }

    public void save(int channelId, int presetId, String name) {
        String value = name(presetId, name);
        if (mapper.update(channelId, presetId, value) == 0) {
            try {
                mapper.insert(channelId, presetId, value);
            } catch (DuplicateKeyException exception) {
                mapper.update(channelId, presetId, value);
            }
        }
    }

    public List<Preset> apply(int channelId, List<Preset> presets) {
        if (presets == null || presets.isEmpty()) return presets;
        Map<Integer, String> remarks = new HashMap<>();
        for (Preset remark : mapper.list(channelId)) {
            remarks.put(PresetIdUtils.parse(remark.getPresetId()), remark.getPresetName());
        }
        List<Preset> result = new ArrayList<>();
        for (Preset preset : presets) {
            Preset copy = new Preset();
            copy.setPresetId(preset.getPresetId());
            String value = remarks.get(PresetIdUtils.parse(preset.getPresetId()));
            if (value == null) value = preset.getPresetName();
            copy.setPresetName(value == null || value.isBlank() ? "预置点" + PresetIdUtils.normalize(preset.getPresetId()) : value);
            result.add(copy);
        }
        return result;
    }

    public WVPResult<String> controlResult(int channelId, int command, int presetId, String name, int code, String msg, String data) {
        if (code == 0) {
            try {
                if (command == 0x81) save(channelId, presetId, name);
                if (command == 0x83) mapper.delete(channelId, presetId);
            } catch (RuntimeException exception) {
                log.error("[预置位备注保存失败] 通道: {}, 编号: {}", channelId, presetId, exception);
                return WVPResult.fail(100, "设备已接收指令，但平台名称备注保存失败，请刷新后修改名称");
            }
        }
        return new WVPResult<>(code, msg, data);
    }
}
