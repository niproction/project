package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class examInitiated implements Serializable {
	private int eiID;
	private String eID;
	private int uID;
	private String password;
	private String initiatedDate;
	
	public examInitiated(int eiID, String eID, int uID, String password, String initiatedDate) {
		super();
		this.eiID = eiID;
		this.eID = eID;
		this.uID = uID;
		this.password = password;
		this.initiatedDate = initiatedDate;
	}
	public examInitiated() {
	
	}
	public int getEiID() {
		return eiID;
	}
	public void setEiID(int eiID) {
		this.eiID = eiID;
	}
	public String geteID() {
		return eID;
	}
	public void seteID(String eID) {
		this.eID = eID;
	}
	public int getuID() {
		return uID;
	}
	public void setuID(int uID) {
		this.uID = uID;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getInitiatedDate() {
		return initiatedDate;
	}
	public void setInitiatedDate(String initiatedDate) {
		this.initiatedDate = initiatedDate;
	}
}
