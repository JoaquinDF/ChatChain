package client;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ChatChainModel.ChatBlock;

public class UpdateInitial_Thread extends Thread {
		
	
	private final int JOINPORT = 6789;
	private final String ASKFORCHAIN = "228.5.6.25";
	private final String ASNWERCHAIN = "228.5.6.8";
	
	public void run() {
			System.out.println("updating BC");
		try {
			MulticastSocket s;
			InetAddress updateChain;
			updateChain = InetAddress.getByName(ASNWERCHAIN);
			
			s = new MulticastSocket(JOINPORT);
			
			s.joinGroup(updateChain);
			
			byte[] buf = new byte[1000000000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);

			
			s.receive(recv);
			String newBC = new String(recv.getData(),0 , recv.getLength());
			setInitialBC(newBC);
			
			s.close();
			s.leaveGroup(updateChain);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}
	
	private void setInitialBC(String IBC) {
		Client client=ClientBuilder.newClient();;
	    URI uri=UriBuilder.fromUri("http://localhost:8080/ChatChain/").build();
		
	    WebTarget target = client.target(uri);
		
		
		target.path("ChatChain").
		                    path("setInitialBC").
		                    queryParam("bc", IBC).
		                    request(MediaType.TEXT_PLAIN).get(String.class);
		
		
		
	}
}

