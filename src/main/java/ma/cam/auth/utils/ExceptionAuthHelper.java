package ma.cam.auth.utils;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;

public class ExceptionAuthHelper {

	// Authentication
	private static final String BAD_CREDENTIALS_KO = "Authentication Failure : Bad credentials";

	// Exc general

	// Authorization
	public static final String AUTHORIZE_FAILER = "ID: {}, Timestamp: {}, Authorization Failure: {}, ex : {}";
	public static final String INVALIDE_TOKEN = "Authorization Failure : Invalide token";
	public static final String MANDATORY_TOKEN = "Authorization Failure : token is mandatory";
	public static final String INVALIDE_TOKEN_EXPIRED = "Authorization Failure : JWT token is expired";
	public static final String INVALIDE_CLAIMS_EMPTY = "Authorization Failure : JWT claims string is empty";
	public static final String INVALIDE_TOKEN_UNSUPPORTED = "Authorization Failure : JWT token is unsupported";
	public static final String INVALIDE_TOKEN_CLAIMS_ISEMPTY = "Authorization Failure : JWT claims string is empty";
	public static final String INVALIDE_TOKEN_SIGNATURE_KO = "Authorization Failure : Invalid JWT signature";

	private ExceptionAuthHelper() {

	}

	/**
	 * Log and throw BadCredentialsException
	 * 
	 * @param userName
	 */
	public static void invalidTokenException(final String msg) {
		throw new AuthenticationServiceException(msg);
	}

	/**
	 * Log and throw BadCredentialsException
	 * 
	 * @param userName
	 */
	public static void badCredentialsException() {
		throw new BadCredentialsException(BAD_CREDENTIALS_KO);
	}

}
