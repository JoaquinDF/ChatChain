package client;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

import com.google.gson.Gson;

import ChatChainModel.ChatBlock;

public class txtReveiver_Thread extends Thread {

	private final int JOINPORT = 65535;
	private final String BLOCKTOSHOW = "228.5.6.10";

	public void run() {
		
		System.out.println("recibiendo" + System.lineSeparator());
		while(true) {
		try {
			MulticastSocket s;
			boolean chainreturned = false;
			InetAddress askforChain;
			askforChain = InetAddress.getByName(BLOCKTOSHOW);

			s = new MulticastSocket(JOINPORT);

			s.joinGroup(askforChain);

			byte[] buf = new byte[1000000];
			DatagramPacket recv = new DatagramPacket(buf, buf.length);
			s.receive(recv);
			System.out.write(recv.getData(), 0,recv.getLength());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		}
	}
}
