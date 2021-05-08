package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable {

	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;

	public User(String username, String password, String email, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String GET_username() {
		return username;
	}

	public String GET_password() {
		return password;
	}
	
	public String GET_firstName() {
		return firstName;
	}
	public String GET_lastName() {
		return lastName;
	}
}
