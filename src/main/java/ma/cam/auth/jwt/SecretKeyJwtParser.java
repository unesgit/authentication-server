package ma.cam.auth.jwt;

import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class SecretKeyJwtParser extends JwtParser {

	private static final String SECRET_KEY = "secret-key";

	public SecretKeyJwtParser(String jwtHeader) {
		super(jwtHeader);
		this.claims = parseStringToken(jwtToken, SECRET_KEY);
	}

	private static Claims parseStringToken(final String token, final String secretKey) {
		return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey)).parseClaimsJws(token)
				.getBody();
	}

}
