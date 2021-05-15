package server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.DataPacket;
import common.IncomingDataPacketHandler;
import common.Principal;
import common.Question;
import common.Student;
import common.Teacher;
import common.User;

public class ServerDataPacketHandler implements IncomingDataPacketHandler {

	private Connection con;

	public ServerDataPacketHandler(Connection con) {
		this.con = con;
	}

	@Override
	public DataPacket CheckRequestExecuteCreateResponce(Object msg) {
		if (msg instanceof DataPacket && ((DataPacket) msg).GET_SendTo() == DataPacket.SendTo.SERVER)
			return ParsingDataPacket((DataPacket) msg);
		else
			System.out.println("not instance of");
		return null;
	}

	@Override
	public DataPacket ParsingDataPacket(DataPacket dataPacket) {
		DataPacket Responce_dataPacket = null;

		if (dataPacket.GET_Request() == DataPacket.Request.LOGIN) {
			if (dataPacket.GET_Data_parameters().get(0) instanceof String
					&& dataPacket.GET_Data_parameters().get(1) instanceof String) {
				Statement stmt;
				try {
					stmt = con.createStatement();

					ResultSet rs = stmt.executeQuery(
							"SELECT * from users WHERE (username='" + (String) (dataPacket.GET_Data_parameters().get(0))
									+ "' OR email='" + (String) (dataPacket.GET_Data_parameters().get(0))
									+ "') AND password='" + (String) (dataPacket.GET_Data_parameters().get(1)) + "'");
					System.out.println(dataPacket.GET_Data_parameters().get(0) + "< looking for in in DB");
					if (rs.next()) {

						System.out.println("found");
						System.out.println(rs.getString(2));

						ArrayList<Object> parameter = new ArrayList<Object>();
						// Object pass_user=null;
						String roleType = rs.getString(8);

						if (roleType.equals("student")) {
							System.out.println("detected student user");
							Student pass_user = new Student(rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getString(5), rs.getString(6));
							parameter.add(pass_user);
						} else if (roleType.equals("teacher")) {
							System.out.println("detected teacher user");
							Teacher pass_user = new Teacher(rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getString(5), rs.getString(6));
							parameter.add(pass_user);

						} else if (roleType.equals("principle")) {
							System.out.println("detected principal user");
							Principal pass_user = new Principal(rs.getString(2), rs.getString(3), rs.getString(4),
									rs.getString(5), rs.getString(6));
							parameter.add(pass_user);
						} else {
							System.out.println("detected Problem");
						}

						Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN,
								parameter, "", true); // create DataPacket that contains true to indicate that the user
														// information is correct
						System.out.println("end search");
					} else
						Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null,
								"", false); // create DataPacket user information is inccorect...
				} catch (SQLException e) {
					e.printStackTrace();
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null, "",
							false); // create DataPacket user information is inccorect...
				}
			} else
				System.out.println("not instance of");

		} else if (dataPacket.GET_Request() == DataPacket.Request.GET_QUESTION) {
			System.out.println("get questionnnnn");
			String questionID = (String) dataPacket.GET_Data_parameters().get(0);
			Statement stmt;
			try {
				stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT * from questions WHERE (qid='" + questionID + "')");
				System.out.println(questionID + "< looking for in in DB");
				if (rs.next()) {

					System.out.println("found question");
					System.out.println(rs.getString(2));

					ArrayList<Object> parameter = new ArrayList<Object>();
					// Object pass_user=null;
					Question question = new Question();
					question.setId(rs.getString(1));
					question.setInfo(rs.getString(2));
					question.setOption1(rs.getString(3));
					question.setOption2(rs.getString(4));
					question.setOption3(rs.getString(5));
					question.setOption4(rs.getString(6));
					question.setAnswer(rs.getString(7));
					question.setAutor(rs.getString(8));
					parameter.add(question);
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION,
							parameter, "", true);
				}
			} catch (Exception e) {
				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.GET_QUESTION, null,
						"", true);
			}

		} else if (dataPacket.GET_Request() == DataPacket.Request.EDIT_QUESTION) {
			ArrayList<Object> parameters = new ArrayList<>();
			Question question = (Question) dataPacket.GET_Data_parameters().get(0);
			PreparedStatement ps;
			try {
				ps = con.prepareStatement(
						"update questions set qid=?,question=?,option1=?,option2=?,option3=?,option4=?,answer=?,author=? where qid =?)");

				ps.setString(1, question.getId());
				ps.setString(2, question.getInfo());
				ps.setString(3, question.getOption1());
				ps.setString(4, question.getOption2());
				ps.setString(5, question.getOption3());
				ps.setString(6, question.getOption4());
				ps.setString(7, question.getAnswer());
				ps.setString(8, question.getAutor());
				ps.setString(9, question.getId());
				int success = ps.executeUpdate();
				parameters.add(question);
				if (success == 1)
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.EDIT_QUESTION,
							parameters, "", true);
				else
					Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.EDIT_QUESTION,
							null, "", true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (dataPacket.GET_Request() == DataPacket.Request.ADD_NEW_QUESTION) {
			Statement stmt2;

			Question question = (Question) dataPacket.GET_Data_parameters().get(0);
			try {
				stmt2 = con.createStatement();
				String query = "select count(*) from questions";
				// Executing the query
				ResultSet rs2 = stmt2.executeQuery(query);
				// Retrieving the result
				rs2.next();
				int count = rs2.getInt(1);
				count++;
				String myStatement = " INSERT INTO questions (qid, question, option1, option2, option3, option4, answer, author) VALUES (?,?,?,?,?,?,?,?)";
				PreparedStatement statement = con.prepareStatement(myStatement);
				statement.setString(1, question.getId() + count);
				statement.setString(2, question.getInfo());
				statement.setString(3, question.getOption1());
				statement.setString(4, question.getOption2());
				statement.setString(5, question.getOption3());
				statement.setString(6, question.getOption4());
				statement.setString(7, question.getAnswer());
				statement.setString(8, ((User) dataPacket.GET_Data_parameters().get(1)).getFirstName() + " "
						+ ((User) dataPacket.GET_Data_parameters().get(1)).getLastName());
				statement.executeUpdate();
				System.out.println("question has been saved");

				Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ADD_NEW_QUESTION,
						null, "", true); // create DataPacket that contains true to indicate that the user information
											// is correct

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return Responce_dataPacket;
	}

}
