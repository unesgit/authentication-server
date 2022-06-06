/**
 * 
 */
package ma.cam.auth.api.advice;

import java.util.Date;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;
import ma.cam.auth.api.error.CustomErrorResponse;
import ma.cam.kernal.exceptions.BusinessException;

/**
 * @author y.nadir
 * 
 *         Global Exception Handler Controller Advice that gets fired whenever
 *         one of the BusinessException Hierarchy is thrown. It Handles each
 *         specific exception by returning the appropriate http status code and
 *         the {@link ma.cam.auth.api.error.api.error.CustomErrorResponse}
 *         CustomErrorResponse
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandlerAdvice {

	@ExceptionHandler({ BusinessException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<CustomErrorResponse> handleUnauthorized(BusinessException ex, WebRequest request) {
		return new ResponseEntity<>(buildCustomErrorResponse(ex, HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
	}

	private CustomErrorResponse buildCustomErrorResponse(BusinessException ex, HttpStatus httpStatus) {
		CustomErrorResponse errors = CustomErrorResponse.builder().timestamp(new Date()).errorCode(ex.getCode())
				.errorMessage(ex.getMessage()).status(httpStatus.value()).build();
		log.warn("Unauthorized Access {}", ex.getMessage());
		return errors;
	}
}
