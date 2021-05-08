package controllers;

import client.App_client;
import common.DataPacket;
import common.IncomingDataPacketHandler;
import javafx.stage.Stage;
import common.Student;
import common.Teacher;
import common.Principal;

public class ClientDataPacketHandler implements IncomingDataPacketHandler {
    private DataPacket Responce_dataPacket;

    public ClientDataPacketHandler(){}
    public ClientDataPacketHandler(Stage primaryStage, Object controller)
    {
        //this.controller = controller;
        //this.primaryStage = primaryStage;
        //if(primaryStage == null)
        //    System.out.println("prob");
    }

    @Override
    public DataPacket CheckRequestExecuteCreateResponce(Object msg) {
        if(msg instanceof DataPacket)
        {
            System.out.println("recived DataPacket");
            return  ParsingDataPacket((DataPacket)msg);
            //return GET_responce_DataPacket();
        }
        else
            System.out.println("not DataPacket");
        return null;
    }

    
    @Override
    public DataPacket ParsingDataPacket(DataPacket dataPacket) {
        Responce_dataPacket = null;

        if( dataPacket.GET_Request() == DataPacket.Request.LOGIN)
        {
            if(dataPacket.GET_result_boolean())
            {
                System.out.println("user corrent");
                if(dataPacket.GET_Data_parameters().get(0) instanceof Student)
                {
                	App_client.user = (Student)(dataPacket.GET_Data_parameters().get(0));
                	System.out.println("user setted to student");
                }
                else if(dataPacket.GET_Data_parameters().get(0) instanceof Teacher)
                {
                	App_client.user = (Teacher)(dataPacket.GET_Data_parameters().get(0));
                	System.out.println("user setted to teacher");
                }
                else if(dataPacket.GET_Data_parameters().get(0) instanceof Principal)
                {
                	App_client.user = (Principal)(dataPacket.GET_Data_parameters().get(0));
                	System.out.println("user setted to principle");
                }else {}
                
            }
            else
                System.out.println("incorrect user");

            return Responce_dataPacket;
        }
        return Responce_dataPacket;
    }

    public void ParsingDataPacket111(DataPacket dataPacket) {
        Responce_dataPacket = null;

        if( dataPacket.GET_Request() == DataPacket.Request.LOGIN)
        {
            if(dataPacket.GET_result_boolean())
            {
                System.out.println("user corrent");
                if(dataPacket.GET_Data_parameters().get(0) instanceof Student)
                {
                	App_client.user = (Student)(dataPacket.GET_Data_parameters().get(0));
                }
                else if(dataPacket.GET_Data_parameters().get(0) instanceof Teacher)
                {
                	App_client.user = (Teacher)(dataPacket.GET_Data_parameters().get(0));
                }
                else if(dataPacket.GET_Data_parameters().get(0) instanceof Principal)
                {
                	App_client.user = (Principal)(dataPacket.GET_Data_parameters().get(0));
                }else {}
                
                //App_client.user = (User)(dataPacket.GET_Data_parameters().get(0));

                
                //SceenController sceen = new SceenController(primaryStage, FxmlSceen.HOME, Lon);
			    //sceen.LoadSceen();
                //loginController.GET_controller().LoadNextSceen();
            }
            else
                System.out.println("incorrect user");

            Responce_dataPacket = new DataPacket(DataPacket.SendTo.CLIENT, DataPacket.Request.LOGIN, null, "hi there", false);
        }
    }

    public DataPacket GET_responce_DataPacket()
    {
        return Responce_dataPacket;
    }
}
