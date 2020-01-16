package com.kaka.webgm.configuration;


import com.kaka.webgm.security.CustomPasswordEncoder;
import com.kaka.webgm.security.IpUserDetailsChecker;
import com.kaka.webgm.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(2147483640)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    Environment env;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private DaoAuthenticationProvider authenticationProvider;

    public SecurityConfig() {
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new CustomPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().
                disable()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/index", true)
                .failureUrl("/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403")
                .and()
                .authorizeRequests()
                .antMatchers("/admin/server/index", "/admin/server/edit", "/admin/server/save").hasAnyRole("ADMIN", "SERVER")
                .antMatchers("/admin/item/index", "/admin/item/edit", "/admin/item/save").hasAnyRole("ADMIN", "ITEM")
                .antMatchers("/gm/forbid_talk/**").hasAnyRole("ADMIN", "FORBID_TALK")
                .antMatchers("/gm/forbid_login/**").hasAnyRole("ADMIN", "FORBID_LOGIN")
                .antMatchers("/gm/kick_out").hasAnyRole("ADMIN", "KICK_OUT")
                .antMatchers("/gm/item_mail/**").hasAnyRole("ADMIN", "ITEM_MAIL")
                .antMatchers("/admin/log/**").hasAnyRole("ADMIN", "OP_LOG")
                .antMatchers("/webjars/**", "/dist/**", "/403", "/login", "/bower_components/**", "/dist/**", "/plugins/**", "*.css", "*.js").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .rememberMe();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, IpUserDetailsChecker userDetailsChecker) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPostAuthenticationChecks(userDetailsChecker);
        return daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
}
