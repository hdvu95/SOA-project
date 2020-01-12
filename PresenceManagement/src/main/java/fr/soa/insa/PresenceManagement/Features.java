package fr.soa.insa.PresenceManagement;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.soa.insa.PresenceManagement.model.ArchiveActions;
import fr.soa.insa.PresenceManagement.model.Config;


@RestController
@RequestMapping("/features")
public class Features {
	
	private String ipAddr = Config.getIP();
	private String port = Config.getPort();
	public static ArrayList<ArchiveActions> archive=new ArrayList<ArchiveActions>();
	
	
	//ces fonctions permettent de créer des cin sans utiliser Postman (pratique pour la simulation)
	@GetMapping("/status/{dept}/{room}/{status}")
	public int changeStatus(@PathVariable("dept") String dept, @PathVariable("room") String room, @PathVariable("status") Boolean status) throws IOException{
		int ans = HTTPManagement.sendPOST("http://"+ipAddr+":"+port+"/~/mn-cse-presence/mn-presence/"+dept+"/"+room, Boolean.toString(status));
		if (ans == 201){
			ArchiveActions aa = new ArchiveActions(Boolean.toString(status),HTTPManagement.getDate(),dept, room);
			archive.add(aa);
		}
		return ans;
	}
	
	@GetMapping("/temp/{val}")
	public int changeStatus(@PathVariable("val") String val) throws IOException{
		int ans = HTTPManagement.sendPOST("http://"+ipAddr+":"+port+"/~/mn-cse-presence/mn-presence/TEMP/DATA", val);
		return ans;
	}
	
	@GetMapping("/getStatus/{dept}/{room}")
	public String getStatus(@PathVariable("dept") String dept,@PathVariable("room") String room) throws IOException{
		String status = "-1";
		String ans = HTTPManagement.sendGET("http://"+ipAddr+":"+port+"/~/mn-cse-presence/mn-presence/"+dept+"/"+room+"/la");
		status = HTTPManagement.extractAns(ans);
		System.out.println(status);
		return status;
	}

	@GetMapping("/getTemp")
	public String getTemp() throws IOException{
		String status = "-1";
		String ans = HTTPManagement.sendGET("http://"+ipAddr+":"+port+"/~/mn-cse-presence/mn-presence/TEMP/DATA/la");
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
