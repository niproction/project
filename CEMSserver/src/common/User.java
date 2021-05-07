package common;

import java.io.Serializable;

public class user implements Serializable{

    private String username;
    private String password;

    public user(String user, String password){
        this.username = user;
        this.password = password;
    }

    public String GET_username(){
        return username;
    }
    
    public String GET_password(){
        return password;
    }
}
