package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.conf.PresetRemarkSchema;
import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.bean.Preset;
import com.genersoft.iot.vmp.gb28181.dao.PresetRemarkMapper;
import com.genersoft.iot.vmp.gb28181.service.IDeviceChannelService;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PresetRemarkServiceTest {
    private Preset preset(String id, String name) {
        Preset preset = new Preset();
        preset.setPresetId(id);
        preset.setPresetName(name);
        return preset;
    }

    @Test
    void persistsAcrossSessionsAndSchemaInitializationAndIsolatesChannels() throws Exception {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1");
        new PresetRemarkSchema(dataSource).afterPropertiesSet();
        Configuration config = new Configuration(new Environment("test", new JdbcTransactionFactory(), dataSource));
        config.addMapper(PresetRemarkMapper.class);
        var factory = new SqlSessionFactoryBuilder().build(config);
        var devicePresets = List.of(preset("1", "设备名称"), preset("PresetID_2", ""), preset("3", "设备原名"));
        try (var session = factory.openSession(true)) {
            var service = new PresetRemarkService(session.getMapper(PresetRemarkMapper.class), mock(IDeviceChannelService.class));
            service.save(10, 1, " 东门入口 ");
            service.save(10, 2, "   ");
            service.save(20, 1, "西门出口");
            var named = service.apply(10, devicePresets);
            assertEquals("东门入口", named.get(0).getPresetName());
            assertEquals("预置点2", named.get(1).getPresetName());
            assertEquals("设备原名", named.get(2).getPresetName());
            assertEquals("设备名称", devicePresets.get(0).getPresetName());
            assertEquals("西门出口", service.apply(20, devicePresets).get(0).getPresetName());
            assertEquals(3, service.apply(10, devicePresets).size());
        }
        new PresetRemarkSchema(dataSource).afterPropertiesSet();
        try (var session = factory.openSession(true)) {
            var service = new PresetRemarkService(session.getMapper(PresetRemarkMapper.class), mock(IDeviceChannelService.class));
            assertEquals("东门入口", service.apply(10, devicePresets).get(0).getPresetName());
            service.save(10, 1, "路口北侧");
            assertEquals("路口北侧", service.apply(10, devicePresets).get(0).getPresetName());
            service.save(10, 1, null);
            assertEquals("预置点1", service.apply(10, devicePresets).get(0).getPresetName());
        }
    }

    @Test
    void changesRemarksOnlyOnAcceptedSetOrDeleteAndReportsPersistenceFailure() {
        PresetRemarkMapper mapper = mock(PresetRemarkMapper.class);
        when(mapper.update(anyInt(), anyInt(), anyString())).thenReturn(1);
        var service = new PresetRemarkService(mapper, mock(IDeviceChannelService.class));
        service.controlResult(10, 0x81, 1, "门口", 403, "Forbidden", null);
        service.controlResult(10, 0x83, 1, null, 408, "Timeout", null);
        service.controlResult(10, 0x82, 1, null, 0, "accepted", null);
        verifyNoInteractions(mapper);
        assertEquals(0, service.controlResult(10, 0x81, 1, "门口", 0, "accepted", null).getCode());
        verify(mapper).update(10, 1, "门口");
        service.controlResult(10, 0x83, 1, null, 0, "accepted", null);
        verify(mapper).delete(10, 1);
        when(mapper.update(10, 2, "预置点2")).thenThrow(new IllegalStateException("database unavailable"));
        var failed = service.controlResult(10, 0x81, 2, "", 0, "accepted", null);
        assertEquals(100, failed.getCode());
        assertTrue(failed.getMsg().contains("设备已接收指令"));
    }

    @Test
    void trimsBoundsAndGeneratesNamesWithoutUsingDeviceNames() {
        assertEquals("预置点255", PresetRemarkService.name(255, "\u3000\t "));
        assertEquals("入口", PresetRemarkService.name(1, " 入口 "));
        assertEquals("名".repeat(128), PresetRemarkService.name(1, "名".repeat(128)));
        assertThrows(ControllerException.class, () -> PresetRemarkService.name(1, "名".repeat(129)));
        assertThrows(ControllerException.class, () -> PresetRemarkService.name(0, "test"));
    }
}
