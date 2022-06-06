package ma.cam.auth.config.listener;

import java.util.Date;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ListenerAuthenticationFailerEvent {

	private static final String AUTH_FAILURE = "ID: {}, User: {},Timestamp: {}, Authentication Failure: {} ";

	@EventListener
	public void onAuthenticationAuthenticationFailure(final AbstractAuthenticationFailureEvent event) {
		log.warn(AUTH_FAILURE, "SEC_LOG_FAILURE", event.getAuthentication().getName(), new Date(event.getTimestamp()),
				event.getException().getMessage());
	}

	// @EventListener
	public void onAuthenticationFailureRequestHandled(final RequestHandledEvent event) {
		log.error(AUTH_FAILURE, "SEC_LOG_FAILURE", event.getUserName(), new Date(event.getTimestamp()),
				event.getFailureCause().getMessage());
	}

}
