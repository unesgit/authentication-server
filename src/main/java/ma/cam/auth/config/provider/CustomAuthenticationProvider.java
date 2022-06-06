package ma.cam.auth.config.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ma.cam.auth.business.UserServiceImpl;
import ma.cam.auth.dto.result.authentication.UserResult;
import ma.cam.kernal.exceptions.BusinessException;
import ma.cam.kernal.exceptions.NotFoundException;
import ma.cam.kernal.exceptions.TechnicalException;

/**
 * @author y.nadir
 */

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserServiceImpl userService;

	private static final String INCORRECT_CREDENTIALS = "Username/Password does not match for : %s";

	@Override
	public Authentication authenticate(final Authentication authentication) {

		log.debug("Processing authentication request for user: {}", authentication.getPrincipal());
		UserResult user;
		try {
			user = userService.findByUserNameAndPassword(authentication.getName(),
					authentication.getCredentials().toString());

			if (user == null) {
				throw new BadCredentialsException(String.format(INCORRECT_CREDENTIALS, authentication.getPrincipal()));
			}
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

		} catch (NotFoundException e) {
			throw new BadCredentialsException(String.format(INCORRECT_CREDENTIALS, authentication.getPrincipal()));
		} catch (TechnicalException | BusinessException e) {
			throw new BadCredentialsException(e.getMessage());
		}

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
