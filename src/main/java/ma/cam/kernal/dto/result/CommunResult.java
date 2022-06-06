package ma.cam.kernal.dto.result;

public class CommunResult {

	protected Long rowsNumber;

	protected String statusColor;

	protected String statusName;

	protected String statusCode;

	public Long getRowsNumber() {
		return rowsNumber;
	}

	public void setRowsNumber(Long rowsNumber) {
		this.rowsNumber = rowsNumber;
	}

	public String getStatusColor() {
		return statusColor;
	}

	public void setStatusColor(String statusColor) {
		this.statusColor = statusColor;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
