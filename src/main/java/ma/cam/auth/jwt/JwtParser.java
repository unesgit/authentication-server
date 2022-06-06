package ma.cam.auth.jwt;

import java.util.List;

import io.jsonwebtoken.Claims;

public abstract class JwtParser {

	private static final String JWT_BEARER = "Bearer ";
	private static final String GROUPS = "GROUPS";
	private static final String USER = "USER";

	protected Claims claims;
	protected String jwtToken;

	protected JwtParser(String jwtHeader) {
		jwtToken = parseJwtToken(jwtHeader);
	}

	public String getUserId() {
		if (claims.containsKey(USER) && !claims.get(USER).toString().isEmpty()) {
			return claims.get(USER).toString();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<String> getGroups() {
		return claims.get(GROUPS, List.class);
	}

	private String parseJwtToken(String authHeader) {
		return authHeader != null ? authHeader.replace(JWT_BEARER, "") : null;
	}

}
