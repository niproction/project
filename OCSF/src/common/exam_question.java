package common;

import java.io.Serializable;

@SuppressWarnings("serial")
public class exam_question implements Serializable {
	private String qID;
	private String description;
	private String points;
	public String getqID() {
		return qID;
	}
	public void setqID(String qID) {
		this.qID = qID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}

}
