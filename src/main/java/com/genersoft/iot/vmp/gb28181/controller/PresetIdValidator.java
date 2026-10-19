package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.utils.PresetIdUtils;
import com.genersoft.iot.vmp.vmanager.bean.ErrorCode;

final class PresetIdValidator {

    private PresetIdValidator() {
    }

    static int parse(String value) {
        Integer presetId = PresetIdUtils.parse(value);
        if (presetId != null) {
            return presetId;
        }
        throw invalidPresetId();
    }

    private static ControllerException invalidPresetId() {
        return new ControllerException(ErrorCode.ERROR100.getCode(), "预置位编号必须为1-255之间的整数");
    }
}
