package org.realworld.demo.config;

import org.realworld.demo.domain.user.service.UserService;
import org.realworld.demo.jwt.Jwt;
import org.realworld.demo.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private Jwt jwt;

  @Autowired
  private UserService userService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests(
            request -> request
                .antMatchers(HttpMethod.POST, "/api/users/login", "/api/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/profiles/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/articles/*").permitAll()
                .anyRequest().authenticated())
        .csrf().disable()
        .addFilterAfter(new JwtAuthenticationFilter(jwt),
            SecurityContextPersistenceFilter.class);
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers("/h2-console/**");
  }
}
