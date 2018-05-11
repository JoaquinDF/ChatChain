package ChatChainModel;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import com.google.gson.Gson;

public class ChatBlock {

	private String text;
	private long timestamp;
	private String TextHash;
	private String prevBlockHash;

	public ChatBlock(String text) {

		this.text = text;
		this.timestamp = (System.currentTimeMillis());
		this.TextHash = HashString(this.text);

	}

	public static String HashString(String text) {
		try {
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
		Gson gson = new Gson();

		this.prevBlockHash = HashString(gson.toJson(C));
	}

	public String getText() {
		return text;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
