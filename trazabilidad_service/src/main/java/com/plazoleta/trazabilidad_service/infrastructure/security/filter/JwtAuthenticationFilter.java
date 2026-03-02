package com.plazoleta.trazabilidad_service.infrastructure.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.plazoleta.trazabilidad_service.infrastructure.security.JwtService;
import com.plazoleta.trazabilidad_service.infrastructure.security.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader(SecurityConstants.ENCABEZADO_AUTORIZACION);

        if (authHeader == null || !authHeader.startsWith(SecurityConstants.PREFIJO_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(SecurityConstants.PREFIJO_BEARER.length());

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        Integer id = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
        String username = jwtService.extractUsername(token);
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        if (role == null || role.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("JWT autenticado para id={}, role={}", id, role);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(SecurityConstants.PREFIJO_ROL + role);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    id,
                    null,
                    List.of(authority));
            authToken.setDetails(username);

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
