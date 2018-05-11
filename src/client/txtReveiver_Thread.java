package client;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import blockChange.ChatChain;

public class txtReveiver_Thread extends Thread {

	public void run() {
		System.out.println("Listening" + System.lineSeparator());
		while (true) {
			try {
				MulticastSocket s;
				InetAddress askforChain;
				askforChain = InetAddress.getByName(ChatChain.BLOCKTOSHOW);

				s = new MulticastSocket(ChatChain.JOINPORT);

				s.joinGroup(askforChain);

				byte[] buf = new byte[1000000];
				DatagramPacket recv = new DatagramPacket(buf, buf.length);
				s.receive(recv);
				String msg = new String(recv.getData(), 0, recv.getLength());
				s.close();

				System.out.println("< " + msg);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
