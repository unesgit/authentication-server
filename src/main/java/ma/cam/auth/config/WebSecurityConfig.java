package ma.cam.auth.config;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.cam.auth.config.filter.JwtProcessingFilter;
import ma.cam.auth.config.filter.LoginFilter;
import ma.cam.auth.config.handler.JwtFailureHandler;
import ma.cam.auth.config.handler.LoginFailureHandler;
import ma.cam.auth.config.handler.LoginSuccessHandler;
import ma.cam.auth.config.matcher.SkipPathRequestMatcher;
import ma.cam.auth.config.provider.CustomAuthenticationProvider;
import ma.cam.auth.jwt.JwtAuthenticationEntryPoint;
import ma.cam.auth.jwt.JwtPublicKeyProvider;

/**
 * This class is used for principal security configuration.
 *
 * @author 10061004
 */
@Configuration
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String LOGIN_ENTRY_POINT = "/api/auth/login";

	public static final String TOKEN_BASED_ENTRY_POINT = "/api/**";

	public static final String PUBLIC_ENTRY_POINT = "/api/public/**";

	public static final String ERROR_ENTRY_POINT = "/error";

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private LoginFailureHandler loginFailureHandler;

	@Autowired
	private JwtFailureHandler jwtFailureHandler;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private JwtPublicKeyProvider jwtPublicKeyProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public RequestMatcher authMatcher() {
		return new OrRequestMatcher(new AntPathRequestMatcher(LOGIN_ENTRY_POINT));
	}

	@Bean
	public RequestMatcher jwtMatcher() {
		return new SkipPathRequestMatcher(Arrays.asList(LOGIN_ENTRY_POINT, PUBLIC_ENTRY_POINT, ERROR_ENTRY_POINT),
				Arrays.asList(TOKEN_BASED_ENTRY_POINT));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Enable CORS and disable CSRF
		http = http.cors().and().csrf().disable();

		// Set session management to stateless
		http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

		// exception handling
		http = http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and();

		// Set permissions on endpoints
		http.authorizeRequests()
				// Our public endpoints
				.antMatchers(PUBLIC_ENTRY_POINT).permitAll()//
				.antMatchers(HttpMethod.POST, LOGIN_ENTRY_POINT).permitAll()//
				// Our private endpoints
				.anyRequest().authenticated();

		http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	private Filter jwtFilter() {
		JwtProcessingFilter jwtProcessingFilter = new JwtProcessingFilter(this.jwtMatcher(), jwtPublicKeyProvider);
		jwtProcessingFilter.setAuthenticationFailureHandler(this.jwtFailureHandler);
		return jwtProcessingFilter;
	}

	private Filter authFilter() throws Exception {
		LoginFilter loginFilter = new LoginFilter(this.authMatcher(), objectMapper);
		loginFilter.setAuthenticationManager(this.authenticationManagerBean());
		loginFilter.setAuthenticationSuccessHandler(this.loginSuccessHandler);
		loginFilter.setAuthenticationFailureHandler(this.loginFailureHandler);
		return loginFilter;
	}

	// Used by spring security if CORS is enabled.
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

}
