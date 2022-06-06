package ma.cam.auth.jwt;

import java.security.PublicKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class PublicKeyJwtParser extends JwtParser {

	public PublicKeyJwtParser(String jwtHeader, PublicKey pk) {
		super(jwtHeader);
		this.claims = parseStringToken(jwtToken, pk);
	}

	private static Claims parseStringToken(final String token, final PublicKey tokenSigningKey) {
		return Jwts.parser().setSigningKey(tokenSigningKey).parseClaimsJws(token).getBody();
	}
}
