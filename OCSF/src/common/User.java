package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {
	
	private String uid;
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String fid;
	
	public User(String uid, String username, String password, String email, String firstName, String lastName,String fid) {
		this.uid=uid;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fid=fid;
	}
	public String getfid() {
		return fid;
	}
	public void setfid(String fid) {
		this.fid=fid;
	}
	public String getuid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid=uid;
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

	
}
