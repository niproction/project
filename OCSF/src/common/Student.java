package common;

import java.io.Serializable;

public class Student extends User implements Serializable {

	public Student(int uID ,String username, String password, String email, String firstName, String lastName,String fid) {
		super(uID,username,password,email,firstName,lastName,fid);
	}

	@Override
	public String toString() {
		return "Student - "+super.toString();
	}

}
