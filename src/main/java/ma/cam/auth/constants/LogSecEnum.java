package ma.cam.auth.constants;

import lombok.Getter;

@Getter
public enum LogSecEnum {
	SEC_LOG_AUTH("SEC_LOG_AUTH", "Log authentication and authorization success and failure events"),
	SEC_LOG_IO("SEC_LOG_IO", "Log input validation failures"),
	SEC_LOG_ERR("SEC_LOG_ERR", "Log potential security events"),
	SEC_LOG_CRYPTO("SEC_LOG_CRYPTO", "Log Errors and Failures in Cryptographic Operations"),
	SEC_LOG_SM("SEC_LOG_SM", "Log session management failures"),
	SEC_LOG_UM("SEC_LOG_UM", "Log identity and access management activities"),
	SEC_LOG_AUD("SEC_LOG_AUD", "Audit log activity");

	private String code;
	private String label;

	/**
	 * @param code
	 * @param label
	 */
	private LogSecEnum(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/**
	 * @param code
	 * @return
	 */
	public static String getEnumCodeByString(String code) {
		for (LogSecEnum e : LogSecEnum.values()) {
			if (code == e.label)
				return e.code;
		}
		return "";
	}

	/**
	 * @param label
	 * @return
	 */
	public static LogSecEnum getEnumByString(String label) {
		for (LogSecEnum e : LogSecEnum.values()) {
			if (label.equals(e.label))
				return e;
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
