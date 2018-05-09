package client;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import ChatChainModel.ChatBlock;

public class txtReveiver_Thread extends Thread {

	private final int JOINPORT = 65535;
	private final String BLOCKTOSHOW = "228.5.6.10";
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
			askforChain = InetAddress.getByName(BLOCKTOSHOW);

			s = new MulticastSocket(JOINPORT);

			s.joinGroup(askforChain);
			
			byte[] buf = new byte[1000000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			System.out.println("a la escucha del multicast");
			s.receive(recv);
			
			String msg = new String(recv.getData(), 0,recv.getLength());
			
			
			if(!msgList.containsKey(msg)) {
				msgList.put(msg, System.currentTimeMillis());
				System.out.println(msg);

			}else {
				if(Math.abs((msgList.get(msg) - System.currentTimeMillis()))>1000) {
				
					System.out.println(msg);
					
					msgList.put(msg, System.currentTimeMillis());
				}
			}			
				
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		}
	}
}
