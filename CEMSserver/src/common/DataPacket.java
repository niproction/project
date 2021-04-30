// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This interface implements the abstract method used to display
 * objects onto the client or server UIs.
 *
 * @author Dr Robert Lagani&egrave;re
 * @author Dr Timothy C. Lethbridge
 * @version July 2000
 */
public class DataPacket implements Serializable
{
  public enum SendTo {
		SERVER,CLIENT
	}

  public enum Request {
		LOGIN, SECCSESFULLY_LOGINED, LOGOUT,READ_EXAMS, ADD_NEW_QUESTION






	}
  
/*public enum MySQL_action{
  SELECT,UPDATE,INSERT, DELETE
}*/

  private SendTo sendTo;
  private Request dataType;
  //private MySQL_action mySql_action;
  private ArrayList<Object> Data_parameters;
  private Boolean result_boolean;

  public DataPacket(){
    this.sendTo =null;
    this.dataType =null;
    this.Data_parameters =null;
    //this.dataType =null;
    this.result_boolean=null;
  }

  public DataPacket(SendTo sendTo, Request dataType, ArrayList<Object> Data_parameters, String ReturnBackMessageType, boolean result_boolean){
    this.sendTo = sendTo;
    this.dataType = dataType;
    //this.mySql_action = null;
    this.Data_parameters = Data_parameters;
    this.result_boolean = result_boolean;
  }
  /*public DataPacket(SendTo sendTo, Request dataType, ArrayList<?> Data_parameters, String ReturnBackMessageType, boolean result_boolean){
    this.sendTo = sendTo;
    this.dataType = dataType;
    //this.mySql_action = mySql_action;
    this.Data_parameters = Data_parameters;
    this.result_boolean = result_boolean;
  }*/

  public SendTo GET_SendTo()
  {
    return sendTo;
  }

  public void SET_SendTo(SendTo sendTo)
  {
    this.sendTo = sendTo;
  }
  
  public Request GET_Request()
  {
    return dataType;
  }

  public void SET_Request(Request dataType)
  {
    this.dataType = dataType;
  }
  
  public ArrayList<Object> GET_Data_parameters()
  {
    return Data_parameters;
  }
  public void SET_Data_parameters(ArrayList<Object> Data_parameters)
  {
    this.Data_parameters = Data_parameters;
  }

  public Boolean GET_result_boolean()
  {
    return result_boolean;
  }

  public void SET_result_boolean(Boolean result_boolean)
  {
    this.result_boolean = result_boolean;
  }

  public String toString(){
    return sendTo.toString()+" "+dataType.toString()+" "+Data_parameters.toString()+" "+result_boolean.toString();
  }
  
}
