package ma.cam.auth.config.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.cam.auth.api.error.CustomErrorResponse;
import ma.cam.auth.exception.base.enums.ExceptionEnum;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	private final ObjectMapper mapper;

	@Autowired
	public LoginFailureHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		mapper.writeValue(response.getWriter(),
				CustomErrorResponse.builder()
						.errorMessage(String.format(ExceptionEnum.AUTH_FAILED.getLabel(), e.getMessage()))//
						.errorCode(ExceptionEnum.AUTH_FAILED.getCode())//
						.status(HttpStatus.UNAUTHORIZED.value()).build());

	}
}