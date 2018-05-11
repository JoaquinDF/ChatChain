package client;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

public class ChainClient {



	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		String Address = "localhost";
		
		
		
		
		Client clientjoin=ClientBuilder.newClient();;
		String url = "http://" + Address+":8080/ChatChain/";
	    URI urijoin=UriBuilder.fromUri(url).build();
	    WebTarget targetjoin = clientjoin.target(urijoin);
	    
	    
	    String joined = targetjoin.path("ChatChain").
                path("Join").
                request().
                accept(MediaType.TEXT_PLAIN).
                get(String.class)
                .toString();
	    
	   	   System.out.println("Success BlockChain Join" + System.lineSeparator() + "Ready to Receive" );
	   	   
		   txtReveiver_Thread receiver = new txtReveiver_Thread();
		   receiver.start();
				   
			String txt = "";
			
			do {
				Scanner keyboard = new Scanner(System.in);
				System.out.println("> ");
				txt = keyboard.nextLine();
				
				Client client=ClientBuilder.newClient();;
			    URI uri=UriBuilder.fromUri(url).build();
				
			    WebTarget target = client.target(uri);
			    target.path("ChatChain").
                path("add").
                queryParam("text",  txt).
                queryParam("justadd" , "false").
                request(MediaType.TEXT_PLAIN).get(String.class);
			    
				
			}while(!txt.equals("exit"));
					
	}

}
