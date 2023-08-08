package com.example.peeppo.global.security;

import com.example.peeppo.global.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.RemoteException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String tokenValue = jwtUtil.getJwtFromHeader(req);
        if (StringUtils.hasText(tokenValue)) {
            if (!jwtUtil.validateToken(tokenValue, req, res)) {
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new AuthorizationServiceException(e.getMessage());

            }
        }

        filterChain.doFilter(req, res);
    }
    // 인증 처리
    public void setAuthentication(String username) {
        Authentication authentication = createAuthentication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("인증처리: {}", (SecurityContextHolder.getContext().getAuthentication()));
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}