package com.rephoto.rephoto_api.jwt;

import com.rephoto.rephoto_api.domain.User;
import com.rephoto.rephoto_api.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String loginId = jwtUtil.getLoginIdFromToken(token);
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
