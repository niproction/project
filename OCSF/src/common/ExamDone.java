package common;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ExamDone implements Serializable {
	private String edID;
	private String eiID;
	private String uID;
	private String startTime;
	private String endTime;
	private String grade;
	private String isCheating;
	
	public String geteID() {
		return eID;
	}
	public void seteID(String eID) {
		this.eID = eID;
	}
	private String eID;
	
	
	public String getIsCheating() {
		return isCheating;
	}
	public ExamDone() {
		
	}
	public ExamDone(String edID, String eID, String uID, String startTime, String endTime, String grade,
			String isCheating) {
		super();
		this.edID = edID;
		this.eID = eID;
		this.uID = uID;
		this.startTime = startTime;
		this.endTime = endTime;
		this.grade = grade;
		this.isCheating = isCheating;
	}
	public void setIsCheating(String isCheating) {
		this.isCheating = isCheating;
	}
	public String getEdID() {
		return edID;
	}
	public void setEdID(String edID) {
		this.edID = edID;
	}
	public String getEiID() {
		return eiID;
	}
	public void setEiID(String eiID) {
		this.eiID = eiID;
	}
	public String getuID() {
		return uID;
	}
	public void setuID(String uID) {
		this.uID = uID;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}

}