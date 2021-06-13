package server;

import common.DataPacket;
import server.CEMSServer.WhoToNotify;

public class Notification {
	private WhoToNotify who;
	private Integer Idintifier;
	private DataPacket dataPacket;
	
	
	public Notification(WhoToNotify who, Integer idintifier, DataPacket dataPacket) {
		super();
		this.who = who;
		this.Idintifier = idintifier;
		this.dataPacket = dataPacket;
	}


	public WhoToNotify getWho() {
		return who;
	}


	public void setWho(WhoToNotify who) {
		this.who = who;
	}


	public Integer getIdintifier() {
		return Idintifier;
	}


	public void setIdintifier(Integer idintifier) {
		Idintifier = idintifier;
	}


	public DataPacket getDataPacket() {
		return dataPacket;
	}


	public void setDataPacket(DataPacket dataPacket) {
		this.dataPacket = dataPacket;
	}
	
	
}
