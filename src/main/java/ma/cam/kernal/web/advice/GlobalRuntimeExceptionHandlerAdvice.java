package ma.cam.kernal.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;
import ma.cam.kernal.exceptions.BusinessException;
import ma.cam.kernal.exceptions.NotFoundException;
import ma.cam.kernal.exceptions.TechnicalException;
import ma.cam.kernal.web.response.ApiResponse;

@ControllerAdvice
@Slf4j
public class GlobalRuntimeExceptionHandlerAdvice {

	@ExceptionHandler({ TechnicalException.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse handleBusinessException(TechnicalException technicalException, WebRequest webRequest) {
		log.error("une erreur technique est survenue: ", technicalException);
		return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), technicalException.getMessage());
	}

	@ExceptionHandler({ BusinessException.class })
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse handleBusinessException(BusinessException businessException, WebRequest webRequest) {
		return new ApiResponse(HttpStatus.CONFLICT.value(), businessException.getMessage());
	}

	@ExceptionHandler({ NotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse handleBusinessException(NotFoundException businessException, WebRequest webRequest) {
		return new ApiResponse(HttpStatus.NOT_FOUND.value(), businessException.getMessage());
	}
}
