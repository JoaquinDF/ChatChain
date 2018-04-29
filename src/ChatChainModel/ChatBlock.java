package ChatChainModel;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChatBlock {
	
	private String text;
	private long timestamp;
	private String TextHash;
	private MessageDigest messageDigest;
	private String prevBlockHash;
	
	
	public ChatBlock(String text) {
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			this.text = text;
			this.timestamp = (System.currentTimeMillis());
			messageDigest.update(this.text.getBytes());
			this.TextHash = new String(messageDigest.digest()); 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			this.text = "ERROR AT BLOCK CREATION";
		}
		
		
		
	}
	
	public static String HashString(String text) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(text.getBytes());
			return (new String(messageDigest.digest())); 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return null;
			
		}		
		
	}
	public String getPrevBlockHash() {
		return prevBlockHash;
	}
	
	public String getTextHash() {
		return TextHash;
	}
	
	public void setPrevBlockHash(ChatBlock C) {
		this.prevBlockHash = HashString(C.toString());
	}

	public String getText() {
		return text;
	}

	public long getTimestamp() {
		return timestamp;
	}


	

}
