package ma.cam.kernal.dto.result;



import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cam
 * 
 */
public class MessageOracle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4411655858207857494L;
	private String typeMessage;
	private String message;
	private Map<String, Object> retours = new HashMap<>();

	/**
	 * @return the typeMessage
	 */
	public String getTypeMessage() {
		return typeMessage;
	}

	/**
	 * @param typeMessage
	 *            the typeMessage to set
	 */
	public void setTypeMessage(String typeMessage) {
		this.typeMessage = typeMessage;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the retours
	 */
	public Map<String, Object> getRetours() {
		return retours;
	}

	/**
	 * @param retours
	 *            the retours to set
	 */
	public void setRetours(Map<String, Object> retours) {
		this.retours = retours;
	}

}
