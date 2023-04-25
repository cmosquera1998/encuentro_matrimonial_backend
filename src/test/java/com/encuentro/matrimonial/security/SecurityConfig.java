package com.encuentro.matrimonial.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailService myAppUserDetailsService;	

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/pilar").permitAll()
		.antMatchers("/user/**").hasAnyAuthority("ADMIN_PRIVILEGE")
		.antMatchers("/pilar/primerPilar/**").hasAnyAuthority("ADMIN_PRIVILEGE")
		.antMatchers("/xxxx/**").hasAnyAuthority("ADMIN_PRIVILEGE")
		.antMatchers("/xxx/**").hasAnyAuthority("ADMIN_PRIVILEGE")
		.and().sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	} 
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myAppUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}