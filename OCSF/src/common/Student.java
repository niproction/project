package common;

import java.io.Serializable;

public class Student extends User implements Serializable {

	public Student(String uid ,String username, String password, String email, String firstName, String lastName,String fid) {
		super(uid,username,password,email,firstName,lastName,fid);
	}

}
