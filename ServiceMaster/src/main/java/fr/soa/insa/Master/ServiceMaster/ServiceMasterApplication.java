package fr.soa.insa.Master.ServiceMaster;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ServiceMasterApplication {
	private final static int delay = 10000; //10s de délai
	private final static String portAlarm = "8081"; //alarm
	private final static String portPresence = "8082"; //presence
	
	private final static ArrayList<String> deptList = new ArrayList<String>(){

		private static final long serialVersionUID = 1L;

		{
			add("GEI_AE");
			add("GM_AE");
			add("GP_AE");
			add("GMM_AE");
		}
		
	};
	
	private final static ArrayList<String> roomList = new ArrayList<String>(){

		private static final long serialVersionUID = 1L;

		{
			add("102_CNT");
			add("103_CNT");
			add("104_CNT");
			add("105_CNT");
		}
		
	};
	
	private final static ArrayList<String> portList = new ArrayList<String>(){

		private static final long serialVersionUID = 1L;

		{
			add("8083"); //doors
			add("8084"); //heating
			add("8085"); //light
			add("8086"); //windows
		}
		
	};
	
	public static void main(String[] args) throws InterruptedException {
		SpringApplication.run(ServiceMasterApplication.class, args);
		RestTemplate restTemplate = new RestTemplate();
		//String rep = restTemplate.getForObject("http://127.0.0.1:"+portList.get(1)+"/"+"features/getStatus/"+deptList.get(1)+"/"+roomList.get(1), String.class);
		//System.out.println("My answer: " + rep);
		
		/* Scénario 1 : Une personne entre dans une salle,
		 * le capteur de présence la détecte, la lumière s’allume automatiquement.
		 */
		/* Scénario 5 : Après 22h, toutes les portes des salles et les fenêtres sont fermées 
		 * après vérification des différents capteurs de présence.
		 */
		
		while(true){
			//On vérifie la valeur du capteur de présence
			for(int i = 0; i < deptList.size(); i++){
				for (int j = 0; j < roomList.size(); j ++){
					String rep = restTemplate.getForObject("http://"+Config.getIP()+":"+portPresence+"/"+"features/getStatus/"+deptList.get(i)+"/"+roomList.get(j), String.class);
					//System.out.println(rep);
					int nowTime = HTTPManagement.getTime();
					
					//si le capteur de présence se déclenche entre 7h et 22h on allume les lumières
					if (rep.equals("true") &&  nowTime < 22 && nowTime >= 7){
						//on allume les lumières
						restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(2)+"/"+"features/turnOn/"+deptList.get(i)+"/"+roomList.get(j), String.class);
					}
					//scénario 3 si on détecte quelqu'un dans la salle entre cette plage d'horaire l'alarme est déclenchée
					else if (rep.equals("true") && nowTime >= 22 && nowTime < 7){
						//on déclenche l'alarm
						restTemplate.getForObject("http://"+Config.getIP()+":"+portAlarm+"/"+"features/turnOn/"+deptList.get(i)+"/"+roomList.get(j), String.class);
					}
					else if (rep.equals("false") &&  nowTime < 22 && nowTime >= 7){
						//s'il y a personne on éteint les lumières
						restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(2)+"/"+"features/turnOff/"+deptList.get(i)+"/"+roomList.get(j), String.class);
					}
					//cf. scénario 5
					else{
						for (int k = 0; k < portList.size(); k++){
							restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(k)+"/"+"features/turnOff/"+deptList.get(i)+"/"+roomList.get(j), String.class);
						}
					}
				}
			}
			
			Thread.sleep(delay);
		
		//String rep = restTemplate.getForObject("http://127.0.0.1:"+portList.get(1)+"/"+"features/getStatus/"+deptList.get(1)+"/"+roomList.get(1), String.class);
		//System.out.println("My answer: " + rep);
		
		
		/* Scénario 2 : La température d'une salle passe en dessous (resp. au dessus) de 23°C,
		 * après vérification de l’état de la fenêtre, celle-ci est fermée (resp. ouverte) automatiquement.
		 * 
		 * Scénario 4 : Scénario 2 + ajustement de la température avec le radiateur si nécessaire.
		 */
			for(int i = 0; i < deptList.size(); i++){
				for (int j = 0; j < roomList.size(); j ++){
					
					String rep_heat = restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(1)+"/"+"features/getStatus/"+deptList.get(i)+"/"+roomList.get(j), String.class);
					//System.out.println(rep_heat);
					int nowTime = HTTPManagement.getTime();
					
					//si c'est la nuit où les salles ne sont pas utilisées on coupe le chauffage et ferme tout
					if(nowTime >= 22 && nowTime < 7){
						restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(1)+"/"+"features/turnOff/"+deptList.get(i)+"/"+roomList.get(j), String.class);
						restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(3)+"/"+"features/turnOff/"+deptList.get(i)+"/"+roomList.get(j), String.class);
					}
					else{
					//si le chauffage est affiche une valeur < 23
						if (Integer.parseInt(rep_heat)< 23){
							restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(1)+"/"+"features/27/"+deptList.get(i)+"/"+roomList.get(j), String.class);
							restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(3)+"/"+"features/turnOff/"+deptList.get(i)+"/"+roomList.get(j), String.class);
						
						}
						//si le chauffage affiche au-dessus de 30 par économie d'énergie on le met à 27
						else if (Integer.parseInt(rep_heat) >= 30){
							restTemplate.getForObject("http://"+Config.getIP()+":"+portList.get(1)+"/"+"features/27/"+deptList.get(i)+"/"+roomList.get(j), String.class);
						}
					}
				}
			}
			Thread.sleep(delay);
		}	
	}
}