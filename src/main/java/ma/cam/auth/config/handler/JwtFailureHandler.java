package ma.cam.auth.config.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import ma.cam.auth.api.error.CustomErrorResponse;
import ma.cam.auth.utils.JsonUtils;

@Component
public class JwtFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException e) throws IOException, ServletException {

		SecurityContextHolder.clearContext();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		out.print(JsonUtils.convertToJson(CustomErrorResponse.builder().errorMessage(e.getMessage())
				.status(HttpServletResponse.SC_UNAUTHORIZED).build()));
		out.flush();

	}
}