package ChatChainModel;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChatBlock {
	
	private String text;
	private long timestamp;
	private String TextHash;
	private String prevBlockHash;
	
	
	public ChatBlock(String text) {
		try {
			MessageDigest messageDigest;

			messageDigest = MessageDigest.getInstance("SHA-256");
			this.text = text;
			this.timestamp = (System.currentTimeMillis());
			messageDigest.update(this.text.getBytes("UTF-8"));
			this.TextHash = new String(messageDigest.digest()); 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			this.text = "ERROR AT BLOCK CREATION";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public static String HashString(String text) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(text.getBytes("UTF-8"));
			return (new String(messageDigest.digest())); 
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return null;
			
		} catch (UnsupportedEncodingException e) {
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
