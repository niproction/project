package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.DataPacket;
import common.IncomingDataPacketHandler;
import common.qustion;
import common.user;

public class ServerDataPacketHandler implements IncomingDataPacketHandler {

    private Connection con;

    public ServerDataPacketHandler(Connection con)
    {
        this.con = con;
    }

    @Override
    public DataPacket CheckRequestExecuteCreateResponce(Object msg) {
        if(msg instanceof DataPacket && ((DataPacket)msg).GET_SendTo() == DataPacket.SendTo.SERVER)
            return ParsingDataPacket((DataPacket)msg);
        else
            System.out.println("not instance of");
        return null;
    }

    
    @Override
    public DataPacket ParsingDataPacket(DataPacket dataPacket) {
        DataPacket Responce_dataPacket = null;

        if( dataPacket.GET_Request() == DataPacket.Request.LOGIN)
        {
            if(dataPacket.GET_Data_parameters().get(0) instanceof String && dataPacket.GET_Data_parameters().get(1) instanceof String)
            {
                Statement stmt;
                try {
                    stmt=con.createStatement();
                    
                    ResultSet rs=stmt.executeQuery("SELECT * from admins WHERE email='"+(String)( dataPacket.GET_Data_parameters().get(0))+"' AND password='"+(String)(dataPacket.GET_Data_parameters().get(1))+"'");

                    if(rs.next())
                    {
                        System.out.println("found");
                        ArrayList<Object> arr = new ArrayList<Object>();
                        arr.add(new user( (String)(dataPacket.GET_Data_parameters().get(0)), (String)(dataPacket.GET_Data_parameters().get(1))  ));
                        Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, arr, "", true);    // create DataPacket that contains true to indicate that the user information is correct
                    }
                    else
                        Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null, "", false);   // create DataPacket user information is inccorect...
                } catch (SQLException e) {
                    e.printStackTrace();
                    Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null, "", false);      // create DataPacket user information is inccorect...
                }
            }
            else
                System.out.println("not instance of");
            return Responce_dataPacket;
        }
        else if( dataPacket.GET_Request() == DataPacket.Request.ADD_NEW_QUESTION) {
        	Statement stmt;
        	try {
				stmt=con.createStatement();
				int rs=stmt.executeUpdate("INSERT INTO questions (ID,info,option1,option2,option3,option4,answer) VALUES ('"+Integer.parseInt((String)( dataPacket.GET_Data_parameters().get(0)))+"','"+(String)( dataPacket.GET_Data_parameters().get(1))+"','"+
						(String)( dataPacket.GET_Data_parameters().get(2))+"','"+(String)( dataPacket.GET_Data_parameters().get(3))+"','"+(String)( dataPacket.GET_Data_parameters().get(4))+"','"+(String)( dataPacket.GET_Data_parameters().get(5))+"','"+
						(String)( dataPacket.GET_Data_parameters().get(6))+"') ");
				
				System.out.println("question has been saved");
				ArrayList<Object> arr = new ArrayList<Object>();
				 arr.add(new qustion( (String)(dataPacket.GET_Data_parameters().get(0)), (String)(dataPacket.GET_Data_parameters().get(1))  ));
                 Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.ADD_NEW_QUESTION, arr, "", true);    // create DataPacket that contains true to indicate that the user information is correct
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
  


        return Responce_dataPacket;
    }
    
}
