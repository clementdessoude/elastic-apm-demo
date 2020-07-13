package fr.clementdessoude.accounting.security;

import fr.clementdessoude.accounting.security.basic.CustomBasicAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

@EnableWebSecurity
@Slf4j
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @RequiredArgsConstructor
    @Configuration
    @Order(1)
    public static class BasicAuthSecurityConfig extends WebSecurityConfigurerAdapter {
        @Value("${application.services.api.basic-authentication.user}")
        private String adminBasicUser;

        @Value("${application.services.api.basic-authentication.password}")
        private String adminBasicPassword;

        private final CustomBasicAuthenticationEntryPoint basicAuthenticationEntryPoint;
        private final PasswordEncoder passwordEncoder;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // prettier-ignore
            http
                .requestMatcher(new BasicAuthRequestMatcher())
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthenticationEntryPoint);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            log.info("User try to connect");
            auth
                .inMemoryAuthentication()
                .withUser(adminBasicUser)
                .password(passwordEncoder.encode(adminBasicPassword))
                .authorities(AuthoritiesConstants.ROLE_ADMIN);
        }

        public static class BasicAuthRequestMatcher implements RequestMatcher {

            public boolean matches(HttpServletRequest request) {
                log.info("request");
                return (
                    request.getHeader("Authorization") != null && request.getHeader("Authorization").startsWith("Basic")
                );
            }
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

