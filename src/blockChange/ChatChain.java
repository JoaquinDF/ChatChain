package blockChange;

import java.io.IOException;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ChatChainModel.ChatBlock;
import client.ListenAndReturn_Thread;
import client.newAdds_Thread;

@Path("ChatChain") // ruta a la clase
@Singleton
public class ChatChain {

	private ArrayList<ChatBlock> ChatChain = new ArrayList<>();

	public static final String ASKFORCHAIN = "228.5.6.25";
	public final static int JOINPORT = 65535;
	public final static int SINGLECASTPORT = 5000;
	public static final int ASNWERCHAINPORT = 5050;
	public static final String MULTICASTBLOCK = "228.5.6.9";
	public static final String BLOCKTOSHOW = "228.5.6.10";
	public static final String ASNWERMULTICASTBLOCK = "228.5.6.19";

	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("Join") // ruta al método
	public String Join() { // el método debe retornar String

		try {

			String msg = "Join";
			System.out.println(msg);

			InetAddress group = InetAddress.getByName(ASKFORCHAIN);
			MulticastSocket s = new MulticastSocket(JOINPORT);
			DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, JOINPORT);
			s.send(hi);

			
			byte[] buf = new byte[10000000];
			DatagramPacket recv  = new DatagramPacket(buf, buf.length);
			DatagramSocket socket = new DatagramSocket(ASNWERCHAINPORT);
			
			socket.receive(recv);

			
			String response = new String(recv.getData(), 0, recv.getLength(), "UTF-8");

			setInitialBC(response);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return "ok";

	}

	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("getBlockChain") // ruta al método
	public String getBC() { // el método debe retornar String

		Gson gson = new Gson();
		String togo = gson.toJson(getChatChain()).toString();
		return togo;

	}

	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("setInitialBC") // ruta al método
	public String setBC(@QueryParam(value = "bc") String bc) { // el método debe
																// retornar
																// String

		try {
			String chain = URLDecoder.decode(bc, "UTF-8");
			Gson gson = new Gson();
			ArrayList<ChatBlock> newBlockChain = null;

			newBlockChain = gson.fromJson(chain, new TypeToken<ArrayList<ChatBlock>>() {
			}.getType());

			setChatChain(newBlockChain);
		} catch (Exception e) {
			return "Ilegal BC UPdate";
		}

		ListenAndReturn_Thread listenandReturn = new ListenAndReturn_Thread();
		listenandReturn.start();

		return "updatedBC";

	}

	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("SetUp") // ruta al método
	public String setUP() { // el método debe retornar String

		ChatBlock initial = new ChatBlock("initial");
		getChatChain().add(initial);

		ListenAndReturn_Thread listenandReturn = new ListenAndReturn_Thread();
		listenandReturn.start();

		return "updatedBC";

	}

	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("add") // ruta al método
	public String addtext(@QueryParam(value = "text") String text, @QueryParam(value = "justadd") String justadd) { 

		ChatBlock Block = new ChatBlock(text);
		if (justadd != null && justadd.equals("true")) {
			
			Block.setPrevBlockHash(getLastElement(getChatChain()));
			getChatChain().add(Block);
			return "ok";

		}

		

		if (getLastElement(getChatChain()).getTimestamp() < System.currentTimeMillis()) {

			Block.setPrevBlockHash(getLastElement(getChatChain()));

			Gson gson = new Gson();
			String msg = gson.toJson(Block);

			InetAddress group;
			try {
				group = InetAddress.getByName(MULTICASTBLOCK);
				MulticastSocket MulticastBlock = new MulticastSocket(JOINPORT);
				DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, JOINPORT);
				MulticastBlock.send(hi);

				

				byte[] buf = new byte[10000000];
				DatagramPacket recv  = new DatagramPacket(buf, buf.length);
				DatagramSocket socket = new DatagramSocket(SINGLECASTPORT);
				
						
				socket.receive(recv);

				
				String response = new String(recv.getData(), 0, recv.getLength(), "UTF-8");
				if (response.equals("ERROR")) {

				} else if (response.equals("OK")) {

					getChatChain().add(Block);

					group = InetAddress.getByName(BLOCKTOSHOW);
					MulticastSocket MulticastShow = new MulticastSocket(JOINPORT);
					DatagramPacket sendShow = new DatagramPacket(Block.getText().getBytes(), Block.getText().length(),
							group, JOINPORT);
					MulticastBlock.send(sendShow);

				}
				socket.close();

			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			return "Error";
		}

		return "added";
	}

	public ArrayList<ChatBlock> getChatChain() {
		return ChatChain;
	}

	public void setChatChain(ArrayList<ChatBlock> chatChain) {
		this.ChatChain = chatChain;

	}

	public ChatBlock getLastElement(ArrayList<ChatBlock> chatChain) {

		return ChatChain.get(chatChain.size() - 1);

	}

	private void setInitialBC(String IBC) {

		try {
			String chain = IBC;
			Gson gson = new Gson();
			ArrayList<ChatBlock> ObjetoMensaje = null;
			try {
				ObjetoMensaje = gson.fromJson(IBC, new TypeToken<ArrayList<ChatBlock>>() {
				}.getType());
			} catch (Exception e) {
				System.err.println(e);
			}

			setChatChain(ObjetoMensaje);
			ListenAndReturn_Thread listenandReturn = new ListenAndReturn_Thread();
			newAdds_Thread newAdds = new newAdds_Thread();

			listenandReturn.start();
			newAdds.start();
		} catch (Exception e) {

		}

	}

}