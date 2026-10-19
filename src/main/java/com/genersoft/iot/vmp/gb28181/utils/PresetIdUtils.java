package com.genersoft.iot.vmp.gb28181.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Shared handling for numeric IDs and the PresetID_N form returned by some platforms. */
public final class PresetIdUtils {

    private static final Pattern PRESET_ID = Pattern.compile("(?:PresetID_)?([1-9][0-9]{0,2})");

    private PresetIdUtils() {
    }

    public static Integer parse(String value) {
        if (value == null) {
            return null;
        }
        Matcher matcher = PRESET_ID.matcher(value);
        if (!matcher.matches()) {
            return null;
        }
        int id = Integer.parseInt(matcher.group(1));
        return id <= 255 ? id : null;
    }

    /** Keep unrecognized IDs visible rather than silently discarding or guessing them. */
    public static String normalize(String value) {
        Integer id = parse(value);
        return id == null ? value : id.toString();
    }
}
