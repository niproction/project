package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Student extends User implements Serializable {

	public Student(String username, String password, String email, String firstName, String lastName) {
		super(username,password,email,firstName,lastName);
	}

}
