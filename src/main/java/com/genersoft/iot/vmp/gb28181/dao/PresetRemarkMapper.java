package com.genersoft.iot.vmp.gb28181.dao;

import com.genersoft.iot.vmp.gb28181.bean.Preset;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PresetRemarkMapper {
    @Select("SELECT preset_id AS presetId, preset_name AS presetName FROM wvp_preset_remark WHERE channel_id=#{channelId}")
    List<Preset> list(int channelId);

    @Update("UPDATE wvp_preset_remark SET preset_name=#{name} WHERE channel_id=#{channelId} AND preset_id=#{presetId}")
    int update(@Param("channelId") int channelId, @Param("presetId") int presetId, @Param("name") String name);

    @Insert("INSERT INTO wvp_preset_remark (channel_id,preset_id,preset_name) VALUES (#{channelId},#{presetId},#{name})")
    int insert(@Param("channelId") int channelId, @Param("presetId") int presetId, @Param("name") String name);

    @Delete("DELETE FROM wvp_preset_remark WHERE channel_id=#{channelId} AND preset_id=#{presetId}")
    int delete(@Param("channelId") int channelId, @Param("presetId") int presetId);
}
