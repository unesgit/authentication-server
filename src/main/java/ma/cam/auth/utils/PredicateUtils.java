package ma.cam.auth.utils;

import static org.apache.commons.lang3.StringUtils.removeEnd;

import java.util.function.Predicate;

public class PredicateUtils {

	private static final String STAR = "*";

	private PredicateUtils() {
	}

	/**
	 * Check the request url match with the configured String url
	 * 
	 * @param requestUrl
	 * @return
	 */
	public static Predicate<String> urlMatch(final String requestUrl) {
		return u -> u.equals(requestUrl) || STAR.equals(u)
				|| (u.indexOf(STAR) >= 0 && requestUrl.startsWith(removeEnd(u, STAR)));
	}

}
