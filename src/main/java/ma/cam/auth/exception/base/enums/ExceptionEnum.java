
package ma.cam.auth.exception.base.enums;

public enum ExceptionEnum {

	AUTH_FAILED("ERR_AUT_001", "The authentication failed: %s"),
	JWT_FAILED("ERR_AUT_002", "The jwt token validation failed: %s");

	private String code;
	private String label;

	private ExceptionEnum(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

}
