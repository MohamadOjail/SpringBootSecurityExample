package online.ojail.securitydemo.config.security;

import lombok.RequiredArgsConstructor;
import online.ojail.securitydemo.config.security.filter.CustomAuthenticationFilter;
import online.ojail.securitydemo.config.security.filter.CustomAuthorizationFilter;
import online.ojail.securitydemo.config.security.utils.TokenUtil;
import online.ojail.securitydemo.service.PersonServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static online.ojail.securitydemo.model.RoleEnum.ADMIN;
import static online.ojail.securitydemo.model.RoleEnum.USER;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author Mo. Ojail
 * created: 2022-09-07
 */

@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig {
    private final PersonServiceImpl personService;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authManager(), tokenUtil);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeRequests( auth -> {
                    auth.antMatchers("/api/users/**").hasAnyAuthority(ADMIN.name(), USER.name());
                    auth.antMatchers("/api/users/save/**").hasAuthority(ADMIN.name());
                    auth.antMatchers("/api/tokens/refresh").permitAll();
                    auth.anyRequest().authenticated();
                })
                .addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(tokenUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(personService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }
}
