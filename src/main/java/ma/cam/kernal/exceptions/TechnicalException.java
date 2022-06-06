package ma.cam.kernal.exceptions;

public class TechnicalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String message;

	public TechnicalException(String message) {

		this.message = message;
	}

	public TechnicalException(String message, Throwable throwable) {
		super(throwable);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
