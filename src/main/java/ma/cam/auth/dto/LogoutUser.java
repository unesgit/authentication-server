package ma.cam.auth.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * This class is used to create the bean used for communication with front part.
 *
 * @author y.nadir
 */
public class LogoutUser {
	@Getter
	@Setter
	@NonNull
	private String username;

}