package ma.cam.auth.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthenticationHelper {

	private AuthenticationHelper() {

	}

	/**
	 * Add Ldap siteCode attribute to UsernamePasswordAuthenticationToken auth
	 * 
	 * @param auth
	 * @param details
	 * @param userData
	 */
	@SuppressWarnings("unchecked")
	public static void addDetail(Authentication auth, final String key, final String value) {
		UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.class.cast(auth);
		Map<String, Object> details = new HashMap<>();
		if (auth.getDetails() != null) {
			details = (Map<String, Object>) auth.getDetails();
		}
		details.put(key, value);
		authentication.setDetails(details);
	}

}
