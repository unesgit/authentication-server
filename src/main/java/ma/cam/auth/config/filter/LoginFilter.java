package ma.cam.auth.config.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ma.cam.auth.dto.LoginRequest;

@Slf4j
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private ObjectMapper objectMapper;

	public LoginFilter(RequestMatcher requiresAuthenticationRequestMatcher, ObjectMapper objectMapper) {
		super(requiresAuthenticationRequestMatcher);
		this.objectMapper = objectMapper;
	}

	@Override
	public void destroy() {
		log.trace("Destroying LoginFilter");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		if (!HttpMethod.POST.name().equals(request.getMethod())) {
			throw new InsufficientAuthenticationException(
					String.format("Authentication method %s not allowed", request.getMethod()));
		}

		LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);

		return this.getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		log.info("authentication successfull");
		this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		log.info("authentication unsuccessfull");
		SecurityContextHolder.clearContext();
		this.getFailureHandler().onAuthenticationFailure(request, response, failed);
	}

}
