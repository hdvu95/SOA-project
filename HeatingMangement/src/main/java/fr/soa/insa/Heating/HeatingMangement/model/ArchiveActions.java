package fr.soa.insa.Heating.HeatingMangement.model;


public class ArchiveActions {
	
	private String action;
	private String time;
	private String room;
	private String dept;
	
	public ArchiveActions(String action, String time, String dept, String room){
		this.action = action;
		this.time = time;
		this.dept = dept;
		this.room = room;
	}

	public String getAction(){
		return action;
	}
	
	public String getTime(){
		return time;
	}
	
	public String toString(){
		return this.dept + "_" + this.room + " : "+ this.action + " at "+this.time;
	}

}
