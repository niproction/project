package common;

import java.io.IOException;
import java.io.Serializable;

import ocsf.server.ConnectionToClient;


@SuppressWarnings("serial")
public class User implements Serializable {
	
	private int uID;
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String fid;
	private String roleType;
	
	public User(int uID, String username, String password, String email, String firstName, String lastName,String fid) {
		this.uID=uID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fid=fid;
	}
	public User(int uID, String username, String password, String email, String firstName, String lastName,String fid, String roleType) {
		this.uID=uID;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fid=fid;
		this.roleType = roleType;
	}
	
	public int getuID() {
		return uID;
	}

	public void setuID(int uID) {
		this.uID = uID;
	}


	public String getfid() {
		return fid;
	}
	public void setfid(String fid) {
		this.fid=fid;
	}
	

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRoleType() {
		return roleType;
	}
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
	@Override
	public String toString() {
		return firstName  +" "+ lastName ;
	}
	
}
