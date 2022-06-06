package ma.cam.auth.jwt;

import java.security.PublicKey;
import java.time.LocalDate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import ma.cam.auth.constants.LogSecEnum;
import ma.cam.auth.dto.PayloadData;
import ma.cam.auth.utils.ExceptionAuthHelper;

@Slf4j
public class JwtTokenUtil {

	private JwtTokenUtil() {
	}

	public static final String DOT = "\\.";

	/**
	 * 
	 * @param publicKey
	 * @param loginResponse
	 * @return
	 */
	public static PayloadData retreiveDataFromJwtToken(final String token, PublicKey publicKey) {
		return new PayloadData(parseStringToken(token, publicKey));
	}

	/**
	 * Verify the Token
	 * 
	 * @param authToken
	 * @param publicKey
	 * @return
	 */
	public static Claims parseStringToken(final String authToken, PublicKey publicKey) {
		String error = null;
		try {
			return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(authToken).getBody();

		} catch (MalformedJwtException e) {
			error = ExceptionAuthHelper.INVALIDE_TOKEN;
			log.error(ExceptionAuthHelper.AUTHORIZE_FAILER, LogSecEnum.SEC_LOG_CRYPTO, LocalDate.now(), error,
					e.getMessage());

		} catch (ExpiredJwtException e) {
			error = ExceptionAuthHelper.INVALIDE_TOKEN_EXPIRED;
			log.error(ExceptionAuthHelper.AUTHORIZE_FAILER, LogSecEnum.SEC_LOG_SM, LocalDate.now(), error,
					e.getMessage());

		} catch (UnsupportedJwtException e) {
			error = ExceptionAuthHelper.INVALIDE_TOKEN_UNSUPPORTED;
			log.error(ExceptionAuthHelper.AUTHORIZE_FAILER, LogSecEnum.SEC_LOG_CRYPTO, LocalDate.now(), error,
					e.getMessage());

		} catch (SignatureException e) {
			error = ExceptionAuthHelper.INVALIDE_TOKEN_SIGNATURE_KO;
			log.error(ExceptionAuthHelper.AUTHORIZE_FAILER, LogSecEnum.SEC_LOG_CRYPTO, LocalDate.now(), error,
					e.getMessage());

		} catch (IllegalArgumentException e) {
			error = ExceptionAuthHelper.INVALIDE_TOKEN_CLAIMS_ISEMPTY;
			log.error(ExceptionAuthHelper.AUTHORIZE_FAILER, LogSecEnum.SEC_LOG_CRYPTO, LocalDate.now(), error,
					e.getMessage());
		}
		ExceptionAuthHelper.invalidTokenException(error);
		return null;
	}

}
