package com.genersoft.iot.vmp.gb28181.dao;

import org.apache.ibatis.annotations.Update;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MysqlParentIdUpdateSqlTest {

    @Test
    void regionParentUpdateUsesMysqlSelfJoin() throws NoSuchMethodException {
        assertMysqlSelfJoin(RegionMapper.class.getMethod("updateParentId", List.class), "wvp_common_region");
    }

    @Test
    void groupParentUpdatesUseMysqlSelfJoin() throws NoSuchMethodException {
        assertMysqlSelfJoin(GroupMapper.class.getMethod("updateParentId", List.class), "wvp_common_group");
        assertMysqlSelfJoin(GroupMapper.class.getMethod("updateParentIdWithBusinessGroup", List.class), "wvp_common_group");
        assertMysqlSelfJoin(GroupMapper.class.getMethod("fixParentId"), "wvp_common_group");
    }

    @Test
    void groupNameRepairUsesSingleTableUpdate() throws NoSuchMethodException {
        Update[] updates = GroupMapper.class.getMethod("updateName", com.genersoft.iot.vmp.gb28181.bean.Group.class)
                .getAnnotationsByType(Update.class);
        assertTrue(updates.length == 1, "updateName 应该只有一条通用SQL");
        String sql = String.join(" ", updates[0].value()).toLowerCase();
        assertTrue(sql.contains("update wvp_common_group"), sql);
        assertTrue(sql.contains("set name=#{name}"), sql);
        assertTrue(sql.contains("where id = #{id}"), sql);
        assertFalse(sql.contains("join"), sql);
        assertFalse(sql.contains("select"), sql);
    }

    private void assertMysqlSelfJoin(Method method, String tableName) {
        Update mysqlUpdate = Arrays.stream(method.getAnnotationsByType(Update.class))
                .filter(update -> "mysql".equals(update.databaseId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Missing MySQL update statement for " + method));

        String sql = String.join(" ", mysqlUpdate.value()).toLowerCase();
        assertTrue(sql.contains("update " + tableName), sql);
        assertTrue(sql.contains(" join " + tableName), sql);
        assertFalse(sql.contains("set parent_id = (select"), sql);
        assertFalse(sql.contains("set g1.parent_id = (select"), sql);
    }
}
