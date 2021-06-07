package common;

import java.io.Serializable;
import java.sql.Time;

@SuppressWarnings("serial")
public class ExtraTimeRequest implements Serializable {
	private String uID;
	private String eiID;
	private String comment;
	private Time extraTime;
	private String isApproved;
	private String courseName;
	private String eID;
	private String fieldName;
	

	
	
	public ExtraTimeRequest(String uID, String eiID, String comment, Time extraTime, String isApproved,
			String courseName, String eID, String fieldName) {
		super();
		this.uID = uID;
		this.eiID = eiID;
		this.comment = comment;
		this.extraTime = extraTime;
		this.isApproved = isApproved;
		this.courseName = courseName;
		this.eID = eID;
		this.fieldName=fieldName;
	}
	public String getuID() {
		return uID;
	}
	public void setuID(String uID) {
		this.uID = uID;
	}
	public String getEiID() {
		return eiID;
	}
	public void setEiID(String eiID) {
		this.eiID = eiID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Time getExtraTime() {
		return extraTime;
	}
	public void setExtraTime(Time extraTime) {
		this.extraTime = extraTime;
	}
	public String getIsApproved() {
		return isApproved;
	}
	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}
	@Override
	public String toString() {
		return "ExtraTimeRequest [uID=" + uID + ", eiID=" + eiID + ", comment=" + comment + ", extraTime=" + extraTime
				+ ", isApproved=" + isApproved + "]";
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String geteID() {
		return eID;
	}
	public void seteID(String eID) {
		this.eID = eID;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	
	
	

}
