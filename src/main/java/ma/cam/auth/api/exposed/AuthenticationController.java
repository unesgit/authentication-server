package ma.cam.auth.api.exposed;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ma.cam.auth.dto.LogoutUser;

/**
 * @author Younes Nadir
 * @date
 */
@RestController
public class AuthenticationController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields();
	}

	@PostMapping(value = "/api/v1/auth/logout")
	@ApiOperation(value = "Logout from IDMS")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful"),
			@ApiResponse(code = 500, message = "Internal server error", response = InternalError.class) })
	public void logout(@Valid @RequestBody LogoutUser logout, HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(value = "userId", required = false) String userId,
			@RequestHeader(value = "officeId", required = false) String officeId,
			@RequestHeader(value = "workstationId", required = false) String workstationId) {
		LogoutHelper.logLogout(logout.getUsername());
	}

}
