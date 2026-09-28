package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.vmanager.bean.ErrorCode;

final class PresetIdValidator {

    private PresetIdValidator() {
    }

    static int parse(String value) {
        if (value == null || !value.matches("[1-9][0-9]*")) {
            throw invalidPresetId();
        }
        try {
            int presetId = Integer.parseInt(value);
            if (presetId >= 1 && presetId <= 255) {
                return presetId;
            }
        } catch (NumberFormatException ignored) {
            // A number outside the Integer range is also an invalid preset ID.
        }
        throw invalidPresetId();
    }

    private static ControllerException invalidPresetId() {
        return new ControllerException(ErrorCode.ERROR100.getCode(), "预置位编号必须为1-255之间的整数");
    }
}
