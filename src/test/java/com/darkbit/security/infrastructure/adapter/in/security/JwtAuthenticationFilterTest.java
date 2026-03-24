package com.darkbit.security.infrastructure.adapter.in.security;

import com.darkbit.security.application.service.JwtService;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

  private static final String SECRET = "my-super-secret-key-for-tests-12345";

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void shouldAuthenticateWithBearerPrefixedToken() throws ServletException, IOException {
    JwtService jwtService = new JwtService(SECRET, 60_000);
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    UserDetails userDetails = User.withUsername("alice").password("secret").authorities("ROLE_USER").build();
    when(userDetailsService.loadUserByUsername("alice")).thenReturn(userDetails);

    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
    String token = jwtService.generateToken("alice", Set.of("ROLE_USER"), Set.of("read"));
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "bearer " + token);

    filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

    assertEquals("alice", SecurityContextHolder.getContext().getAuthentication().getName());
    assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
  }

  @Test
  void shouldAuthenticateWithRawToken() throws ServletException, IOException {
    JwtService jwtService = new JwtService(SECRET, 60_000);
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    UserDetails userDetails = User.withUsername("alice").password("secret").authorities("ROLE_USER").build();
    when(userDetailsService.loadUserByUsername("alice")).thenReturn(userDetails);

    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);
    String token = jwtService.generateToken("alice", Set.of("ROLE_USER"), Set.of("read"));
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", token);

    filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());

    assertEquals("alice", SecurityContextHolder.getContext().getAuthentication().getName());
  }

  @Test
  void shouldLeaveContextEmptyWhenAuthorizationHeaderIsMissing() throws ServletException, IOException {
    JwtService jwtService = new JwtService(SECRET, 60_000);
    UserDetailsService userDetailsService = mock(UserDetailsService.class);
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtService, userDetailsService);

    filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), new MockFilterChain());

    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }
}

