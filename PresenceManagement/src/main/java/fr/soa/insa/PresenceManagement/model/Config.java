package fr.soa.insa.PresenceManagement.model;

public class Config {
	
	//cette classe permet de modifier l'adresse IP et le port de l'architecture oneM2M
	
	private final static String IP_ADDR = "127.0.0.1";
	private final static String PORT = "8080";
	
	public static String getIP(){
		return IP_ADDR;
	}
	
	public static String getPort(){
		return PORT;
	}
}
