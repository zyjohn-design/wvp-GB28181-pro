package com.genersoft.iot.vmp.conf.security;

import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.conf.security.dto.LoginUser;
import com.genersoft.iot.vmp.conf.security.dto.JwtUser;
import com.genersoft.iot.vmp.service.IUserService;
import com.genersoft.iot.vmp.storager.dao.dto.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * jwt token 过滤器
 */

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static String WSHeader = "sec-websocket-protocol";


    @Autowired
    private UserSetting userSetting;

    @Autowired
    private IUserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = servletRequest;
        // 忽略登录请求的token验证
        String requestURI = request.getRequestURI();
        if ((requestURI.startsWith("/doc.html") || requestURI.startsWith("/swagger-ui")  ) && !userSetting.getDocEnable()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (requestURI.equalsIgnoreCase("/api/user/login")) {
            chain.doFilter(request, response);
            return;
        }

        if (!userSetting.getInterfaceAuthentication()) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(null, null, List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_OPERATOR"),
                    new SimpleGrantedAuthority("ROLE_VIEWER")));
            SecurityContextHolder.getContext().setAuthentication(token);
            chain.doFilter(request, response);
            return;
        }

        String jwt = request.getHeader(JwtUtils.getHeader());
        // 这里如果没有jwt，继续往后走，因为后面还有鉴权管理器等去判断是否拥有身份凭证，所以是可以放行的
        // 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口

        // websocket 鉴权信息默认存储在这里
        String secWebsocketProtocolHeader = request.getHeader(WSHeader);
        if (StringUtils.isBlank(jwt)) {

            if (secWebsocketProtocolHeader != null) {
                jwt = secWebsocketProtocolHeader;
                response.setHeader(WSHeader, secWebsocketProtocolHeader);
            }else {
                jwt = request.getParameter(JwtUtils.getHeader());
            }
            if (StringUtils.isBlank(jwt)) {
                jwt = request.getHeader(JwtUtils.getApiKeyHeader());
                if (StringUtils.isBlank(jwt)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        JwtUser jwtUser = JwtUtils.verifyToken(jwt);
        String username = jwtUser.getUserName();
        // TODO 处理各个状态
        switch (jwtUser.getStatus()){
            case EXPIRED:
                response.setStatus(401);
                chain.doFilter(request, response);
                // 异常
                return;
            case EXCEPTION:
                // 过期
                response.setStatus(400);
                chain.doFilter(request, response);
                return;
            case EXPIRING_SOON:
                // 即将过期
//                return;
            default:
        }
        // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
        // 使用数据库中的实时角色，角色修改或用户删除后立即生效
        User dbUser = userService.getUserById(jwtUser.getUserId());
        if (dbUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 默认密码用户：仅允许修改密码与登出接口
        if (dbUser.isDefaultPassword()) {
            if (!requestURI.equalsIgnoreCase("/api/user/changePassword")
                    && !requestURI.equalsIgnoreCase("/api/user/logout")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        LoginUser loginUser = new LoginUser(dbUser, LocalDateTime.now());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                dbUser, null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(request, response);
    }
}
