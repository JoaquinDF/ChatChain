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
	private final int JOINPORT = 6789;
	private final String ASKFORCHAIN = "228.5.6.25";
	private final String ASNWERCHAIN = "228.5.6.8";
	
	public void run() {
			System.out.println("listening");
		try {
			MulticastSocket s;
			boolean chainreturned = false;
			InetAddress askforChain;
			askforChain = InetAddress.getByName(ASKFORCHAIN);
			
			s = new MulticastSocket(JOINPORT);
			
			s.joinGroup(askforChain);
			
			byte[] buf = new byte[1000000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			//Espera hasta que le llega una petici�n de uni�n a la BC, cuando lo recibe return BC
			s.receive(recv);
			
			ArrayList<ChatBlock> CC = askBCtoLocallhost();
			
			Gson gson = new Gson();
			String toBeSended= gson.toJson(CC);
			
			
			InetAddress group = InetAddress.getByName(ASNWERCHAIN);
			MulticastSocket answer = new MulticastSocket(JOINPORT);
			DatagramPacket cc = new DatagramPacket(toBeSended.getBytes(), toBeSended.length(), group, JOINPORT);
			s.send(cc);
			
			//SENDCHAIN
			answer.close();
			s.close();
			s.leaveGroup(askforChain);			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	private ArrayList<ChatBlock> askBCtoLocallhost() {
		
		
		
		Client client=ClientBuilder.newClient();;
	    URI uri=UriBuilder.fromUri("http://localhost:8080/ChatChain/").build();
		
	    WebTarget target = client.target(uri);
		
		
		String response = target.path("ChatChain").
		                    path("Join").
		                    request().
		                    accept(MediaType.TEXT_PLAIN).
		                    get(Response.class)
		                    .toString();
		
		Gson gson = new Gson();
		Type ChainType = new TypeToken<ArrayList<ChatBlock>>(){}.getType();
		ArrayList<ChatBlock> ObjetoMensaje = gson.fromJson(response, ChainType);
		return ObjetoMensaje;
	}
}

