package common;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("serial")
public class Exam implements Serializable {
	private int examID;
	private ArrayList<Question> questions;
	private Date date;
	private Date time;

	public void addQuestion(Question question)
	{
		questions.add(question);
	}

	private void set_time(String time) {
		SimpleDateFormat ft = new SimpleDateFormat("HH:MM");

		System.out.print(time + " Parses as ");
		Date t=null;
		try {
			t = ft.parse(time);
			System.out.println(t);
		} catch (ParseException e) {
			System.out.println("Unparseable using " + ft);
		}
		this.time = t;
	}
	
	
	private void set_date(String date) {
		SimpleDateFormat ft = new SimpleDateFormat("dd/mm/yyyy");
		
		System.out.print(date + " Parses as ");
		Date t=null;
		try {
			t = ft.parse(date);
			System.out.println(t);
		} catch (ParseException e) {
			System.out.println("Unparseable using " + ft);
		}
		this.date=t;
	}
}
