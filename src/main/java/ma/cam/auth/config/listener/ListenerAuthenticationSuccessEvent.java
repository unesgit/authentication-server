package ma.cam.auth.config.listener;

import java.util.Date;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ListenerAuthenticationSuccessEvent {

	private static final String AUTH_OK = "ID: {}, User: {},Timestamp: {}, Authentication Success";

	@EventListener
	public void onAuthenticationSuccess(final AuthenticationSuccessEvent event) {
		if (log.isDebugEnabled()) {
			log.debug(AUTH_OK, "auth_failure", event.getAuthentication().getName(), new Date(event.getTimestamp()));
		}
	}

}
