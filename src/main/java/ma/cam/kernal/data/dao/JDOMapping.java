package ma.cam.kernal.data.dao;

public class JDOMapping {
	
	private int index;
	
	private Class<?> jdo;
	
	public JDOMapping(int index, Class<?> jdo) {
		this.index = index;
		this.jdo = jdo;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Class<?> getJdo() {
		return jdo;
	}

	public void setJdo(Class<?> jdo) {
		this.jdo = jdo;
	}
	
}
