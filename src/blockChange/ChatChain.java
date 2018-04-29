package blockChange;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
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
import client.UpdateInitial_Thread;

@Path("ChatChain") // ruta a la clase
@Singleton
public class ChatChain {

	private MulticastSocket s;
	private ArrayList<ChatBlock> ChatChain = new ArrayList<>();
	

	private final int JOINPORT = 6789;
	private final String ASKFORCHAIN = "228.5.6.25";
	private final String ASNWERCHAIN = "228.5.6.8";
	private final String MULTICASTBLOCK = "228.5.6.9";
	private final String BLOCKTOSHOW = "228.5.6.10";



	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("Join") // ruta al método
	public String Join() { // el método debe retornar String

		try {

			String msg = "join";
			InetAddress group = InetAddress.getByName(ASKFORCHAIN);
			s = new MulticastSocket(JOINPORT);
			DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, 6789);
			s.send(hi);
			
			UpdateInitial_Thread InitialThread = new UpdateInitial_Thread();
			InitialThread.run();
			

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
		return gson.toJson(getChatChain()).toString();

	}
	
	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("setInitialBC") // ruta al método
	public String setBC(@QueryParam(value="bc") String bc)  { // el método debe retornar String

		try {
		Gson gson = new Gson();
		Type ChainType = new TypeToken<ArrayList<ChatBlock>>(){}.getType();
		ArrayList<ChatBlock> newBlockChain = gson.fromJson(bc, ChainType);
		setChatChain(newBlockChain);
		}catch (Exception e) {
			return "Ilegal BC UPdate";
		}
		
		
		ListenAndReturn_Thread listenandReturn = new ListenAndReturn_Thread();
		listenandReturn.run();
		
		
		return "updatedBC";
		
	}
	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("SetUp") // ruta al método
	public String setUP()  { // el método debe retornar String

		ChatBlock initial = new ChatBlock("initial");
		getChatChain().add(initial);
		
		ListenAndReturn_Thread listenandReturn = new ListenAndReturn_Thread();
		listenandReturn.run();
		
		
		return "updatedBC";
		
	}
	
	
	
	
	@GET // tipo de petición HTTP
	@Produces(MediaType.TEXT_PLAIN) // tipo de texto devuelto
	@Path("add") // ruta al método
	public String addtext(@QueryParam(value="text") String text)  { // el método debe retornar String

		ChatBlock Block = new ChatBlock(text);
		if(getChatChain().indexOf(Block)!=-1) {
			
			//Ya existe luego se valida.
			String msg = text;
			InetAddress group;
			try {
				group = InetAddress.getByName(BLOCKTOSHOW);
				s = new MulticastSocket(JOINPORT);
				DatagramPacket datagram = new DatagramPacket(msg.getBytes(), msg.length(), group, 6789);
				s.send(datagram);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
			return "";
		}
		
		if(getChatChain().size()==0) {
			getChatChain().add(Block);
		}else {
			if(getLastElement(getChatChain()).getTimestamp() < System.currentTimeMillis()) {
				
				Block.setPrevBlockHash(getLastElement(getChatChain()));
				getChatChain().add(Block);
				
				String msg = text + "BLOCKCHAIN" + Block.getTextHash();
				
				InetAddress group;
				try {
					group = InetAddress.getByName(MULTICASTBLOCK);
					s = new MulticastSocket(JOINPORT);
					DatagramPacket hi = new DatagramPacket(msg.getBytes(), msg.length(), group, 6789);
					s.send(hi);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				
			}else {
				return "Error";
			}
		}
		
		return "added";
	}
	
	
	
	public  ArrayList<ChatBlock> getChatChain() {
		return ChatChain;
	}

	public void setChatChain(ArrayList<ChatBlock> chatChain) {
		ChatChain = chatChain;
	}
	
	public ChatBlock getLastElement(ArrayList<ChatBlock> chatChain) {
		
		return ChatChain.get(chatChain.size()-1);
		
	}
	
	
}