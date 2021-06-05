package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class examInitiated implements Serializable {
	private String eiID;
	private String eID;
	private String uID;
	private String time;
	private String password;
	public String getEiID() {
		return eiID;
	}
	public void setEiID(String eiID) {
		this.eiID = eiID;
	}
	public String geteID() {
		return eID;
	}
	public void seteID(String eID) {
		this.eID = eID;
	}
	public String getuID() {
		return uID;
	}
	public void setuID(String uID) {
		this.uID = uID;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
