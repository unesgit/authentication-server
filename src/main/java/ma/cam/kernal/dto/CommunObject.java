package ma.cam.kernal.dto;

public class CommunObject {

	protected Long userId;

	protected Long userEntityId;

	protected Long entityId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserEntityId() {
		return userEntityId;
	}

	public void setUserEntityId(Long userEntityId) {
		this.userEntityId = userEntityId;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

}
