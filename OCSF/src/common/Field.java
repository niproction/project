package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Field implements Serializable {
	private String filedId;
	private String filedName;

	public Field() {}
	
	public Field(String filedId, String filedName) {
		super();
		this.filedId = filedId;
		this.filedName = filedName;
	}

	public String getFiledId() {
		return filedId;
	}

	public void setFiledId(String filedId) {
		this.filedId = filedId;
	}

	public String getFiledName() {
		return filedName;
	}

	public void setFiledName(String filedName) {
		this.filedName = filedName;
	}
}