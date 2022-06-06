package ma.cam.kernal.exceptions;

public class BusinessException extends RuntimeException{

	private static final String MESSAGE_SEPARATOR = "##";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private  String code;
	private final String message;
	
	public BusinessException(String code, String message){
		this.code = code;
		this.message = message;
	}
	
	public BusinessException(String message){
		if(message.contains(MESSAGE_SEPARATOR)) {
			String[] messageSplit = message.split(MESSAGE_SEPARATOR);
			this.code= messageSplit[0];
			this.message = messageSplit[1];
			return;
		}
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String getMessage() {
		return message;
	}
	

	
}
