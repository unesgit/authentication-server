package ma.cam.auth.api.exposed;

import java.time.LocalDate;

import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class LogoutHelper {

	private static final String AUTH_OK = "ID: {}, User: {},Timestamp: {}, Logout  Success";

	private LogoutHelper() {

	}

	/**
	 * Log the logout operation
	 * 
	 * @param userName
	 */
	public static void logLogout(final String actualUser) {
		SecurityContextHolder.clearContext();
		if (log.isDebugEnabled()) {
			log.debug(AUTH_OK, "logout", actualUser, LocalDate.now());
		}
	}

}
