package fr.soa.insa.Heating.HeatingMangement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HTTPManagement {
	
	public static String sendGET(String address) throws IOException{
		String rep = "...";
		URL url = new URL(address);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		//Headers section
		con.setRequestProperty("X-M2M-Origin", "admin:admin");
		con.setRequestProperty("Accept", "application/xml");
		
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			rep = response.toString();
			System.out.println(rep);
			
		} else {
			System.out.println("GET request not worked");
			rep = "-1";
		}
		return rep;
	}
	
	public static int sendPOST(String address, String value) throws IOException {
		URL obj = new URL(address);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("X-M2M-Origin","admin:admin");
		con.setRequestProperty("Content-Type","application/xml;ty=4");

		// For POST body - START
		String body = "<m2m:cin xmlns:m2m='http://www.onem2m.org/xml/protocols'><cnf>NA</cnf><con>"+value+"</con></m2m:cin>";
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(body.getBytes());
		os.flush();
		os.close();
		// For POST body - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
		return responseCode;
	}
	
	//Cette fonction permet de poster la température du chauffage
	public static int sendPOST(String address, String value, String heat_value) throws IOException {
		URL obj = new URL(address);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("X-M2M-Origin","admin:admin");
		con.setRequestProperty("Content-Type","application/xml;ty=4");

		// For POST body - START
		String body = "<m2m:cin xmlns:m2m='http://www.onem2m.org/xml/protocols'><cnf>"+heat_value+"</cnf><con>"+heat_value+"</con></m2m:cin>";
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(body.getBytes());
		os.flush();
		os.close();
		// For POST body - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
		return responseCode;
	}
	
	
	public static String extractAns(String ans){
		String rep = "-1";
		
		ans = ans.replaceFirst("</con>", "<con>");
		String[] tmp = ans.split("<con>");
		
		if(ans.equals("-1")){
			return "[ERROR] Extraction went wrong";
		}
		rep = tmp[1];
		
		return rep;
	}

	
	public static String extractHeat(String ans){
		String rep = "-1";
		
		ans = ans.replaceFirst("</cnf>", "<cnf>");
		String[] tmp = ans.split("<cnf>");
		
		if(ans.equals("-1")){
			return "[ERROR] Extraction went wrong";
		}
		rep = tmp[1];
		
		return rep;

	}

	public static String getDate(){
		 Calendar cal = Calendar.getInstance();
		 String time= new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(cal.getTime());
		 
		 return time;
	}
	
}
