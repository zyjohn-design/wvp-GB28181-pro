package com.genersoft.iot.vmp.conf.security.dto;

import com.genersoft.iot.vmp.storager.dao.dto.Role;
import com.genersoft.iot.vmp.storager.dao.dto.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginUserTest {

    @Test
    void adminRoleGetsAllAuthorities() {
        LoginUser loginUser = loginUser(1, "0");

        assertEquals(3, loginUser.getAuthorities().size());
        assertTrue(loginUser.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(loginUser.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("ROLE_OPERATOR")));
        assertTrue(loginUser.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("ROLE_VIEWER")));
    }

    @Test
    void operatorCanWriteButUnknownRoleIsReadOnly() {
        LoginUser operator = loginUser(2, "operator");
        LoginUser unknown = loginUser(3, "custom-value");

        assertTrue(operator.getAuthorities().stream().anyMatch(it -> it.getAuthority().equals("ROLE_OPERATOR")));
        assertEquals(1, unknown.getAuthorities().size());
        assertEquals("ROLE_VIEWER", unknown.getAuthorities().iterator().next().getAuthority());
    }

    private LoginUser loginUser(int roleId, String authority) {
        Role role = new Role();
        role.setId(roleId);
        role.setAuthority(authority);
        User user = new User();
        user.setUsername("user-" + roleId);
        user.setRole(role);
        return new LoginUser(user, LocalDateTime.now());
    }
}
