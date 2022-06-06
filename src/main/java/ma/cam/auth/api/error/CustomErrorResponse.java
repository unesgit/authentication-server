package ma.cam.auth.api.error;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author y.nadir
 * 
 *         This class holds the error response structure that will be returned
 *         in case any of the exception is thrown
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CustomErrorResponse {

	private Date timestamp;
	private int status;
	private String errorMessage;
	@JsonInclude(Include.NON_NULL)
	private String errorCode;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", locale = "en")
	public Date getTimestamp() {
		return (timestamp != null) ? timestamp : new Date();
	}

}
