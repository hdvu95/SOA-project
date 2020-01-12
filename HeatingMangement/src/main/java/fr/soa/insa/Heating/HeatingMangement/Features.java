package fr.soa.insa.Heating.HeatingMangement;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.soa.insa.Heating.HeatingMangement.model.ArchiveActions;
import fr.soa.insa.Heating.HeatingMangement.model.Config;



@RestController
@RequestMapping("/features")
public class Features {
	
	private String ipAddr = Config.getIP();
	private String port = Config.getPort();
	public static ArrayList<ArchiveActions> archive=new ArrayList<ArchiveActions>();
	
	@GetMapping("/turnOff/{dept}/{room}")
	public int turnOff(@PathVariable("dept") String dept, @PathVariable("room") String room) throws IOException{
		int ans = HTTPManagement.sendPOST("http://"+ipAddr+":"+port+"/~/mn-cse-heating/mn-heating/"+dept+"/"+room, "0");
		if (ans == 201){
			ArchiveActions aa = new ArchiveActions("OFF",HTTPManagement.getDate(),dept, room);
			archive.add(aa);
		}
		return ans;
	}
	
	//Allumer le chauffage avec une certaine valeur de température
	@GetMapping("/{value}/{dept}/{room}")
	public int turnOn(@PathVariable("value") String value, @PathVariable("dept") String dept, @PathVariable("room") String room) throws IOException{
		int ans = HTTPManagement.sendPOST("http://"+ipAddr+":"+port+"/~/mn-cse-heating/mn-heating/"+dept+"/"+room, "1", value);
		if(ans == 201){
			ArchiveActions aa = new ArchiveActions("ON: "+value+"°C",HTTPManagement.getDate(), dept, room);
			archive.add(aa);
		}
		return ans;
	}
	
	@GetMapping("/getStatus/{dept}/{room}")
	public String getStatus(@PathVariable("dept") String dept,@PathVariable("room") String room) throws IOException{
		String status = "-1";
		String ans = HTTPManagement.sendGET("http://"+ipAddr+":"+port+"/~/mn-cse-heating/mn-heating/"+dept+"/"+room+"/la");
		status = HTTPManagement.extractAns(ans);
		System.out.println(status);
		return status;
	}
	
	@GetMapping("/getHeat/{dept}/{room}")
	public String getHeat(@PathVariable("dept") String dept,@PathVariable("room") String room) throws IOException{
		String status = "-1";
		String ans = HTTPManagement.sendGET("http://"+ipAddr+":"+port+"/~/mn-cse-heating/mn-heating/"+dept+"/"+room+"/la");
		status = HTTPManagement.extractHeat(ans);
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
