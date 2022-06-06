package ma.cam.auth.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import ma.cam.auth.dto.result.authentication.UserResult;

public class JwtTokenCreator {

	private static final String USER_INFO = "USERINFO";
	private static final String APPLICATION = "APPLICATION";
	private static final String PROFIL = "PROFIL";

	private long validityInMilliseconds;

	@Getter
	private String token;
	@Getter(value = AccessLevel.NONE)
	@Setter(value = AccessLevel.NONE)
	private Key privateKey;

	private UserResult userResult;

	public JwtTokenCreator(Authentication authentication, Key privateKey, long tokenExpirationTime) {
		userResult = (UserResult) authentication.getPrincipal();
		this.privateKey = privateKey;
		validityInMilliseconds = tokenExpirationTime;
	}

	/**
	 * Generate a Token
	 * 
	 * @param username
	 * @param role
	 * @return
	 */
	public JwtTokenCreator create() {

		Claims claims = Jwts.claims()
				.setSubject(userResult.getUserInfoResult().getNom().concat(userResult.getUserInfoResult().getPrenom()));
		claims.put(USER_INFO, userResult.getUserInfoResult());
		claims.put(APPLICATION, userResult.getApplicationResultList());
		claims.put(PROFIL, userResult.getProfilResultList());

		token = buildToken(claims);
		return this;
	}

	/**
	 * @param claims
	 * @return
	 */
	private String buildToken(Claims claims) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		return Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.RS512, privateKey)//
				.compact();
	}

}
