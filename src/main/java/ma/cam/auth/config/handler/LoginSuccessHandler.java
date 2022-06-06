package ma.cam.auth.config.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.cam.auth.jwt.JwtTokenCreator;
import ma.cam.auth.jwt.signature.JwtPrivateKeyProvider;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private JwtPrivateKeyProvider jwtPrivateKeyProvider;

	@Autowired
	private ObjectMapper mapper;

	@Value("${jwt.token.tokenExpirationTime}")
	private long tokenExpirationTime;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authentication);
		JwtTokenCreator jwtTokenCreator = new JwtTokenCreator(authentication, jwtPrivateKeyProvider.getKey(),
				tokenExpirationTime).create();
		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("token", jwtTokenCreator.getToken());
		// tokenMap.put("refreshToken", refreshToken.getToken());
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		mapper.writeValue(response.getWriter(), tokenMap);
		clearAuthenticationAttributes(request);
	}

	/**
	 * Removes temporary authentication-related data which may have been stored in
	 * the session during the authentication process..
	 * 
	 */
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

}
