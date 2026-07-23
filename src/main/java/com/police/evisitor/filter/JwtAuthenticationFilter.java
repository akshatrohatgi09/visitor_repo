package com.police.evisitor.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.police.evisitor.util.JWTUtility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JWTUtility jwtUtility;

	public JwtAuthenticationFilter(JWTUtility jwtUtility) {
		this.jwtUtility = jwtUtility;
	}

	/**
	 * Skip JWT validation for public APIs.
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {

		String path = request.getServletPath();

		return path.equals("/visitor/login") || path.equals("/visitor/register") || path.startsWith("/swagger-ui")
				|| path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")
				|| path.startsWith("/webjars") || path.startsWith("/actuator");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String header = request.getHeader("Authorization");

		if (header != null && header.startsWith("Bearer ")) {

			String token = header.substring(7);

			try {

				String username = jwtUtility.extractUsername(token);

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null
						&& jwtUtility.validateToken(token, username)) {

					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							username, null, AuthorityUtils.NO_AUTHORITIES);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}

			} catch (Exception ex) {

				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json");
				response.getWriter().write("{\"status\":false,\"message\":\"Invalid JWT Token\"}");
				return;
			}
		}

		filterChain.doFilter(request, response);
	}
}