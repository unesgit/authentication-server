package ma.cam.auth.config.matcher;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

public class SkipPathRequestMatcher implements RequestMatcher {
	private OrRequestMatcher skipMatchers;
	private List<RequestMatcher> processingMatchers;

	public SkipPathRequestMatcher(List<String> pathsToSkip, List<String> processingPath) {
		Assert.notNull(pathsToSkip, "pathsToSkip cannot be null");
		List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path))
				.collect(Collectors.toList());
		skipMatchers = new OrRequestMatcher(m);

		processingMatchers = processingPath.stream().map(path -> new AntPathRequestMatcher(path))
				.collect(Collectors.toList());
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		if (skipMatchers.matches(request)) {
			return false;
		}
		for (RequestMatcher processingPathMatcher : processingMatchers) {
			if (!processingPathMatcher.matches(request)) {
				return false;
			}
		}
		return true;
	}
}