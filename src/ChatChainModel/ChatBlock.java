package ChatChainModel;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ChatBlock {
	
	private String text;
	private long timestamp;
	private String TextHash;
	private String prevBlockHash;
	
	
	public ChatBlock(String text) {
		

			this.text = text;
			this.timestamp = (System.currentTimeMillis());
			this.TextHash =  HashString(this.text);
		
		
		
		
	}
	
	public static String HashString(String text) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			byte[] hash = digest.digest(text.getBytes("UTF-8"));
			String encoded = Base64.getEncoder().encodeToString(hash);
			
			return (encoded); 
			
			
			
			
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
