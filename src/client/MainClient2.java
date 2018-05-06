package client;

import java.net.URI;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

public class MainClient2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		Client clientjoin=ClientBuilder.newClient();;
	    URI urijoin=UriBuilder.fromUri("http://localhost:8082/ChatChain/").build();
	    WebTarget targetjoin = clientjoin.target(urijoin);
	    
	    
	    String joined = targetjoin.path("ChatChain").
                path("Join").
                request().
                accept(MediaType.TEXT_PLAIN).
                get(String.class)
                .toString();
	    
	   	   System.out.println(joined);
		   txtReveiver_Thread receiver = new txtReveiver_Thread();
		   receiver.start();
				   
			String txt = "";
			
			do {
				Scanner keyboard = new Scanner(System.in);
				System.out.println("> ");
				txt = keyboard.nextLine();
				Client client=ClientBuilder.newClient();;
			    URI uri=UriBuilder.fromUri("http://localhost:8080/ChatChain/").build();
				
			    WebTarget target = client.target(uri);
			    target.path("ChatChain").
                path("add").
                queryParam("text", txt).
                request(MediaType.TEXT_PLAIN).get(String.class);
			    
				
			}while(!txt.equals("exit"));
					
	}

}
