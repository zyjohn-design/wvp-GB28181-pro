package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.gb28181.bean.Group;
import com.genersoft.iot.vmp.gb28181.dao.GroupMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * 校验业务分组/虚拟组织的历史乱码名称可以在重新同步目录时被自动修复
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GroupServiceImplNameRepairTest {

    @Mock
    private GroupMapper groupManager;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Group group(int id, String deviceId, String name) {
        Group group = new Group();
        group.setId(id);
        group.setDeviceId(deviceId);
        group.setName(name);
        group.setBusinessGroup("42010400002000000001");
        return group;
    }

    @Test
    void repairsNameWithReplacementCharacter() {
        Group inDb = group(1, "42010400002000000002", "03-\uFFFD~口大队");
        Group incoming = group(0, "42010400002000000002", "03-硚口大队");
        when(groupManager.queryInGroupListByDeviceId(anyList())).thenReturn(List.of(inDb));

        groupService.batchAdd(List.of(incoming));

        ArgumentCaptor<Group> captor = ArgumentCaptor.forClass(Group.class);
        verify(groupManager).updateName(captor.capture());
        assertEquals("03-硚口大队", captor.getValue().getName());
        assertEquals(1, captor.getValue().getId());
        // 已存在的分组不应该被重复插入
        verify(groupManager, never()).batchAdd(anyList());
    }

    @Test
    void keepsManuallyModifiedNameWhenNoGarbledCharacter() {
        Group inDb = group(1, "42010400002000000002", "硚口大队(自定义)");
        Group incoming = group(0, "42010400002000000002", "03-硚口大队");
        when(groupManager.queryInGroupListByDeviceId(anyList())).thenReturn(List.of(inDb));

        groupService.batchAdd(List.of(incoming));

        verify(groupManager, never()).updateName(org.mockito.ArgumentMatchers.any());
    }
}
