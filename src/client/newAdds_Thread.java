package client;


import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
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

import org.eclipse.yasson.internal.serializer.AbstractNumberDeserializer;
import org.glassfish.jersey.client.ClientConfig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ChatChainModel.ChatBlock;
import blockChange.ChatChain;

public class newAdds_Thread extends Thread  {
		
	
	

	public void run() {
			System.out.println("updating BC");
		try {
			MulticastSocket s;
			InetAddress updateChain;
			updateChain = InetAddress.getByName(ChatChain.MULTICASTBLOCK);
			while(true) {
			s = new MulticastSocket(ChatChain.JOINPORT);
			
			s.joinGroup(updateChain);
			
			byte[] buf = new byte[10000000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			
			//Espera hasta que le llega una petici�n de uni�n a la BC, cuando lo recibe return BC
			s.receive(recv);
			String newBC = new String(recv.getData(), 0, 
                    recv.getLength(), "UTF-8");
			

			Gson gson = new Gson();
			
			
			ChatBlock newBlock = gson.fromJson(newBC,ChatBlock.class);
			ArrayList<ChatBlock> CC = askBCtoLocallhost();
			ChatBlock prevBlock = CC.get(CC.size()-1);
			
			
			
			
			
			
			if((ChatBlock.HashString(prevBlock.toString())== newBlock.getPrevBlockHash()) && newBlock.getTextHash()== ChatBlock.HashString(newBlock.getText())) {
				//hay que borrar el ultimo elemento de la BC
				
				//ENVIAR UN ERROR.
				String msg = "ERROR";
				System.out.println(msg);
				//respuesta a la direcci�n que env�a
				InetAddress unicastanswer = recv.getAddress();
				
				DatagramSocket answerblock = new DatagramSocket();

				 DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), unicastanswer, ChatChain.SINGLECASTPORT);
				 answerblock.send(packet);
				
				 answerblock.close();
				
				System.err.println("BLOQUES CORRUPTOS");
			}else {
				
				String msg = "OK";
				
				InetAddress unicastanswer = recv.getAddress();
				
				DatagramSocket answerblock = new DatagramSocket();

				DatagramPacket packet = new DatagramPacket(msg.getBytes(), msg.length(), unicastanswer, ChatChain.SINGLECASTPORT);
				answerblock.send(packet);
				
				
				
			    
			    
				
				
				
				
			}
			}
			
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

