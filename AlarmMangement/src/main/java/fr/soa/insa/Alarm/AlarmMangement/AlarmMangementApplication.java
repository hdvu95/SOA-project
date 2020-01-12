package fr.soa.insa.Alarm.AlarmMangement;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.soa.insa.Alarm.AlarmMangement.model.ArchiveActions;

@SpringBootApplication
public class AlarmMangementApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AlarmMangementApplication.class, args);
		//ArrayList <ArchiveActions> archiveList = new ArrayList <ArchiveActions>();
		//Features f = new Features();
		//f.getStatus("GEI","106");
		//f.turnOff("GEI","106", archiveList);
		//System.out.println(archiveList.get(0).getAction()+"_____"+archiveList.get(0).getTime());
	}

}
