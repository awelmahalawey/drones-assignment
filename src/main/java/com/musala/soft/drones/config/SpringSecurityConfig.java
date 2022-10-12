package com.musala.soft.drones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
public class SpringSecurityConfig {

//    @Autowired
//    private BaseAuthenticationFilter baseAuthenticationFilter;
//
//    @Autowired
//    private ApiThrottlingFilter apiThrottlingFilter;
//
//    @Autowired
//    private CORSFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
                .and()
                .authorizeRequests()
//                .antMatchers(WebConstants.PERMITTED_URLS_ANT_PATTERNS)
                .anyRequest()
                .permitAll();
//				// following requests are Secured (checked for auth-token)
//                .anyRequest().authenticated()
//                .and()
//                .addFilterBefore(baseAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(apiThrottlingFilter, BaseAuthenticationFilter.class)
//                .addFilterBefore(corsFilter, ChannelProcessingFilter.class);
        return http.build();
    }
}
