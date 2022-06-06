package ma.cam.auth.config.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;
import ma.cam.auth.dto.PayloadData;
import ma.cam.auth.jwt.JwtPublicKeyProvider;
import ma.cam.auth.jwt.JwtTokenUtil;
import ma.cam.auth.utils.ExceptionAuthHelper;

@Slf4j
public class JwtProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";

	private JwtPublicKeyProvider jwtPublicKeyProvider;

	public JwtProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher,
			JwtPublicKeyProvider jwtPublicKeyProvider) {
		super(requiresAuthenticationRequestMatcher);
		this.jwtPublicKeyProvider = jwtPublicKeyProvider;
	}

	@Override
	public void destroy() {
		log.trace("Destroying LoginFilter");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		if (!requiresAuthentication(request, response)) {
			chain.doFilter(request, response);
			return;
		}

		Authentication authResult;
		try {
			authResult = attemptAuthentication(request, response);
			if (authResult == null) {
				unsuccessfulAuthentication(request, response,
						new AuthenticationServiceException(ExceptionAuthHelper.INVALIDE_TOKEN));
				return;
			}
		} catch (AuthenticationServiceException authServiceExp) {
			unsuccessfulAuthentication(request, response, authServiceExp);
			return;
		} catch (Exception e) {
			unsuccessfulAuthentication(request, response, new AuthenticationServiceException(e.getMessage()));
			return;
		}
		successfulAuthentication(request, response, chain, authResult);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		HttpServletRequest httpServletRequest = HttpServletRequest.class.cast(request);
		final Optional<String> jwt = retrieveJwt(httpServletRequest);

		PayloadData pd = jwt
				.map(token -> JwtTokenUtil.retreiveDataFromJwtToken(token, jwtPublicKeyProvider.getPublicKey()))
				.orElseThrow(() -> new AuthorizationServiceException(ExceptionAuthHelper.MANDATORY_TOKEN));

		UserDetails context = new UserDetails() {

			@Override
			public boolean isEnabled() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCredentialsNonExpired() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isAccountNonLocked() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isAccountNonExpired() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String getUsername() {
				// TODO Auto-generated method stub
				return pd.getUserName();
			}

			@Override
			public String getPassword() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				// TODO Auto-generated method stub
				return null;
			}
		};

		return new UsernamePasswordAuthenticationToken(context, null, context.getAuthorities());

	}

	private Optional<String> retrieveJwt(HttpServletRequest request) {
		final String headerAuth = request.getHeader(AUTHORIZATION);

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER)) {
			log.debug("[Authorization ZUUL] : {} key is present in the request Headres : {}", AUTHORIZATION,
					request.getRequestURI());
			return Optional.of(headerAuth.substring(7, headerAuth.length()));
		}
		log.debug("[Authorization ZUUL] : {} key is not present in the request Headres : {}", AUTHORIZATION,
				request.getRequestURI());
		return Optional.empty();
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authResult);
		SecurityContextHolder.setContext(context);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		this.getFailureHandler().onAuthenticationFailure(request, response, failed);
	}

}
