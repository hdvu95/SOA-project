package fr.soa.insa.Doors.DoorsMangement;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.soa.insa.Doors.DoorsMangement.model.ArchiveActions;
import fr.soa.insa.Doors.DoorsMangement.model.Config;


@RestController
@RequestMapping("/features")
public class Features {
	
	private String ipAddr = Config.getIP();
	private String port = Config.getPort();
	public static ArrayList<ArchiveActions> archive=new ArrayList<ArchiveActions>();
	
	@GetMapping("/turnOff/{dept}/{room}")
	public int turnOff(@PathVariable("dept") String dept, @PathVariable("room") String room) throws IOException{
		int ans = HTTPManagement.sendPOST("http://"+ipAddr+":"+port+"/~/mn-cse-doors/mn-doors/"+dept+"/"+room, "0");
		if (ans == 201){
			ArchiveActions aa = new ArchiveActions("CLOSED",HTTPManagement.getDate(),dept, room);
			archive.add(aa);
		}
		return ans;
	}
	
	@GetMapping("/turnOn/{dept}/{room}")
	public int turnOn(@PathVariable("dept") String dept, @PathVariable("room") String room) throws IOException{
		int ans = HTTPManagement.sendPOST("http://"+ipAddr+":"+port+"/~/mn-cse-doors/mn-doors/"+dept+"/"+room, "1");
		if(ans == 201){
			ArchiveActions aa = new ArchiveActions("OPEN",HTTPManagement.getDate(), dept, room);
			archive.add(aa);
		}
		return ans;
	}
	@GetMapping("/getStatus/{dept}/{room}")
	public String getStatus(@PathVariable("dept") String dept,@PathVariable("room") String room) throws IOException{
		String status = "-1";
		String ans = HTTPManagement.sendGET("http://"+ipAddr+":"+port+"/~/mn-cse-doors/mn-doors/"+dept+"/"+room+"/la");
		status = HTTPManagement.extractAns(ans);
		System.out.println(status);
		return status;
	}
	
	@GetMapping("/archive")
	public String getArchive(){
		String tmp = "";
		for (int i = 0; i<archive.size(); i++){
			tmp = tmp + archive.get(i)+"<br/>";
		}
		
		return tmp;
	}

}
