package logic;

import javafx.beans.property.SimpleStringProperty;

public class test {
	private SimpleStringProperty id;
	private SimpleStringProperty profession;
	private SimpleStringProperty course;
	private SimpleStringProperty testDuration;
	private SimpleStringProperty points;
	
	public test(String id,String profession,String course,String testDuration,String points) {
		this.id=new SimpleStringProperty(id);
		this.profession=new SimpleStringProperty(profession);
		this.course=new SimpleStringProperty(course);
		this.testDuration=new SimpleStringProperty(testDuration);
		this.points=new SimpleStringProperty(points);
	}

	public String getId() {
		return id.get();
	}

	public void setId(String id) {
		this.id.set(id);
	}

	public String getProfession() {
		return profession.get();
	}

	public void setProfession(String profession) {
		this.profession.set(profession);
	}

	public String getCourse() {
		return course.get();
	}

	public void setCourse(String course) {
		this.course.set(course);
	}

	public String getTestDuration() {
		return testDuration.get();
	}

	public void setTestDuration(String testDuration) {
		this.testDuration.set(testDuration);
	}

	public String getPoints() {
		return points.get();
	}

	public void setPoints(String points) {
		this.points.set(points);
	}
	@Override
	public boolean equals(Object other) {
		return this.id.get().equals(((test)other).getId());
	}

}
