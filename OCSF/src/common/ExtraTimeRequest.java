package common;

import java.io.Serializable;
import java.sql.Time;

@SuppressWarnings("serial")
public class ExtraTimeRequest implements Serializable {
	private int uID;
	private int eiID;
	private String comment;
	private String extraTime;
	private String isApproved;
	private String courseName;
	private String eID;
	private String fieldName;
	

	
	
	public ExtraTimeRequest(int uID, int eiID, String comment, String extraTime, String isApproved,
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
	public int getuID() {
		return uID;
	}
	public void setuID(int uID) {
		this.uID = uID;
	}
	public int getEiID() {
		return eiID;
	}
	public void setEiID(int eiID) {
		this.eiID = eiID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getExtraTime() {
		return extraTime;
	}
	public void setExtraTime(String extraTime) {
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