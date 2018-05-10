package client;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;

import ChatChainModel.ChatBlock;
import blockChange.ChatChain;

public class txtReveiver_Thread extends Thread {

	
	private Map<String,Long> msgList;
	private String originaddress = "";
	
	
	
	public void run() {
		System.out.println("recibiendo" + System.lineSeparator());
		while(true) {
			msgList = new HashMap<>();
		try {
			MulticastSocket s;
			boolean chainreturned = false;
			InetAddress askforChain;
			askforChain = InetAddress.getByName(ChatChain.BLOCKTOSHOW);
			
			s = new MulticastSocket(ChatChain.JOINPORT);

			s.joinGroup(askforChain);
			
			byte[] buf = new byte[1000000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			s.receive(recv);
			
			String msg = new String(recv.getData(), 0,recv.getLength());
			s.close();

			
			
			
			if(!msgList.containsKey(msg)) {
				msgList.put(msg, System.currentTimeMillis());
				System.out.println("< " + msg);

			}else {
				if(Math.abs((msgList.get(msg) - System.currentTimeMillis()))>1000) {
				
					System.out.println("< " + msg);
					
					msgList.put(msg, System.currentTimeMillis());
				}
			}			
				
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		}
	}
}
