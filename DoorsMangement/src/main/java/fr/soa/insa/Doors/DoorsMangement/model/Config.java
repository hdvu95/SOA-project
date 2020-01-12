package fr.soa.insa.Doors.DoorsMangement.model;

public class Config {
	
	private final static String IP_ADDR = "127.0.0.1";
	private final static String PORT = "8080";
	
	public static String getIP(){
		return IP_ADDR;
	}
	
	public static String getPort(){
		return PORT;
	}
}
