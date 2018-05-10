package client;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;

import ChatChainModel.ChatBlock;

public class ListenAndReturn_Thread extends Thread {
	private final int JOINPORT = 65535;
	private final String ASKFORCHAIN = "228.5.6.25";
	private final String ASNWERCHAIN = "228.5.6.8";
	private final static int SINGLECASTPORT = 5000;

	
	public void run() {
			System.out.println("listening");
		try {
			
			boolean chainreturned = false;
			
			while(true) {
			InetAddress askforChain = InetAddress.getByName(ASKFORCHAIN);

			MulticastSocket s = new MulticastSocket(JOINPORT);
			
			s.joinGroup(askforChain);
			
			byte[] buf = new byte[1000000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			s.receive(recv);
			
	ArrayList<ChatBlock> CC = askBCtoLocallhost();
			
			Gson gson = new Gson();
			String toBeSended= gson.toJson(CC);
			
	        byte[] output = toBeSended.getBytes("UTF-8");

			
			
			
			
			
			InetAddress group = InetAddress.getByName(ASNWERCHAIN);
			MulticastSocket answer = new MulticastSocket(JOINPORT);
			DatagramPacket cc = new DatagramPacket(output, output.length, group, JOINPORT);
			Thread.sleep(500);
			answer.send(cc);
			}
			//SENDCHAIN
				
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	private ArrayList<ChatBlock> askBCtoLocallhost() {
		
		
		
		Client client=ClientBuilder.newClient();;
	    URI uri=UriBuilder.fromUri("http://localhost:8080/ChatChain/").build();
		
	    WebTarget target = client.target(uri);
		
		
		String response = target.path("ChatChain").
		                    path("getBlockChain").
		                    request().
		                    accept(MediaType.TEXT_PLAIN).
		                    get(String.class)
		                    .toString();
		
		Gson gson = new Gson();
		ArrayList<ChatBlock> ObjetoMensaje = null;
		
		 ObjetoMensaje = gson.fromJson(response, new TypeToken<ArrayList<ChatBlock>>(){}.getType());

		return ObjetoMensaje;
	}
}

