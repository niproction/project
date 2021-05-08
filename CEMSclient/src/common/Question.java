package common;

import java.io.Serializable;

public class Question implements Serializable {
	 private String id;
	 private String info;
	 private String option1;
	 private String option2;
	 private String option3;
	 private String option4;
	 private String answer;
	 

	    public Question(String id, String info){
	        this.id=id;
	        this.info=info;
	    }

	    public String GET_id(){
	        return id;
	    }
	    
	    public String GET_info(){
	        return info;
	    }

}
