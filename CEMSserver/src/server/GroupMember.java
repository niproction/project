package server;

import java.io.IOException;

import common.User;
import ocsf.server.ConnectionToClient;

public class GroupMember
{
	private User user;
	private ConnectionToClient client;
	
	public GroupMember(User user,ConnectionToClient client)
	{
		this.user=user;
		this.client=client;
	}
	
	public void notify(Object msg)
	{
		try {
			client.sendToClient(msg);
			System.out.println("Member "+user.toString()+" notified");
		} catch (IOException e) {
			System.out.println("Member "+user.toString()+" error notifying");
			e.printStackTrace();
		}
	}
	
	public User getUser() {
		return user;
	}


	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return user.getuID() == ((GroupMember)obj).getUser().getuID();
	}
	

	@Override
	public String toString() {
		return user.toString();
	}
	
}
