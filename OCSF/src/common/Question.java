package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Question implements Serializable {
	private static int counter = 0;
	private String qID;
	private int authorID;
	private String info;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String answer;
	private String points;

	public Question(String qID, int authorID, String info, String option1, String option2, String option3,
			String option4, String answer) {
		this.qID = qID;
		this.authorID = authorID;
		this.info = info;
		this.option1 = option1;
		this.option2 = option2;
		this.option3 = option3;
		this.option4 = option4;
		this.answer = answer;
	}


	public Question() {
		counter++;
	}
	
	public String getqID() {
		return qID;
	}


	public void setqID(String qID) {
		this.qID = qID;
	}


	public int getAuthorID() {
		return authorID;
	}


	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	
	

	public int getcounter() {
		return counter;
	}

	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getOption1() {
		return option1;
	}

	public void setOption1(String option1) {
		this.option1 = option1;
	}

	public String getOption2() {
		return option2;
	}

	public void setOption2(String option2) {
		this.option2 = option2;
	}

	public String getOption3() {
		return option3;
	}

	public void setOption3(String option3) {
		this.option3 = option3;
	}

	public String getOption4() {
		return option4;
	}

	public void setOption4(String option4) {
		this.option4 = option4;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public String getPoints() {
		return points;
	}


	public void setPoints(String points) {
		this.points = points;
	}

}