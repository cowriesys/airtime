import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import javax.xml.bind.DatatypeConverter;

public class AirtimeClient {

	private String url;
	private String clientId;
	private String clientKey;
	public static String TerminalId = "0001";

	public AirtimeClient(String url, String clientId, String clientKey) {
		this.url = url;
		this.clientId = clientId;
		this.clientKey = clientKey;
	}

	private String nonce() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		Date now = new Date();
		return dateFormat.format(now);
	}

	private String sign(String nonce, String query) throws NoSuchAlgorithmException, InvalidKeyException {
		byte[] data = (nonce + query).getBytes();
		byte[] secretKeyBytes = Base64.decodeBase64(clientKey);
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
		sha256_HMAC.init(secret_key);
		//return Base64.encodeBase64String(sha256_HMAC.doFinal(data));
		return DatatypeConverter.printBase64Binary(sha256_HMAC.doFinal(data));
	}

	public JsonObject balance() {

		JsonObject balance = null;

		try {
			URL rest = new URL(url + "/airtime/Balance");
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, "");

			connection.setRequestProperty("ClientId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}
			
			JsonReader jreader = Json.createReader(connection.getInputStream());
			balance = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return balance;
	}

	public JsonObject credit(String net, String msisdn, int amount, String xref) {

		JsonObject credit = null;

		try {
			String query = MessageFormat.format("?net={0}&msisdn={1}&amount={2}&xref={3}", net, msisdn, amount, xref);
			URL rest = new URL(url + "/airtime/Credit" + query);
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, query);

			connection.setRequestProperty("ClientId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}

			JsonReader jreader = Json.createReader(connection.getInputStream());
			credit = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return credit;
	}

	public JsonObject dataCredit(String net, String msisdn, int amount, String xref) {

		JsonObject credit = null;

		try {
			String query = MessageFormat.format("?net={0}&msisdn={1}&amount={2}&xref={3}", net, msisdn, amount, xref);
			URL rest = new URL(url + "/data/Credit" + query);
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, query);

			connection.setRequestProperty("ClientId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}

			JsonReader jreader = Json.createReader(connection.getInputStream());
			credit = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return credit;
	}

	public JsonObject check(String xref) {

		JsonObject check = null;

		try {
			String query = MessageFormat.format("?reference={0}", xref);
			URL rest = new URL(url + "/airtime/Check" + query);
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, query);

			connection.setRequestProperty("ClientId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}
			
			JsonReader jreader = Json.createReader(connection.getInputStream());
			check = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return check;
	}

	public JsonObject agentBalance() {

		JsonObject balance = null;

		try {
			URL rest = new URL(url + "/terminal/Balance");
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, "");

			connection.setRequestProperty("AgentId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);
			connection.setRequestProperty("TerminalId", TerminalId);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}
			
			JsonReader jreader = Json.createReader(connection.getInputStream());
			balance = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return balance;
	}

	public JsonObject agentCredit(String net, String msisdn, int amount, String xref) {

		JsonObject credit = null;

		try {
			String query = MessageFormat.format("?net={0}&msisdn={1}&amount={2}&xref={3}", net, msisdn, amount, xref);
			URL rest = new URL(url + "/terminal/Credit" + query);
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, query);

			connection.setRequestProperty("AgentId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);
			connection.setRequestProperty("TerminalId", TerminalId);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}

			JsonReader jreader = Json.createReader(connection.getInputStream());
			credit = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return credit;
	}

	public JsonObject agentDataCredit(String net, String msisdn, int amount, String xref) {

		JsonObject credit = null;

		try {
			String query = MessageFormat.format("?net={0}&msisdn={1}&amount={2}&xref={3}", net, msisdn, amount, xref);
			URL rest = new URL(url + "/terminal/data/Credit" + query);
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, query);

			connection.setRequestProperty("AgentId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);
			connection.setRequestProperty("TerminalId", TerminalId);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}

			JsonReader jreader = Json.createReader(connection.getInputStream());
			credit = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return credit;
	}

	public JsonObject agentCheck(String xref) {

		JsonObject check = null;

		try {
			String query = MessageFormat.format("?reference={0}", xref);
			URL rest = new URL(url + "/terminal/Check" + query);
			HttpURLConnection connection = (HttpURLConnection) rest.openConnection();
			connection.setRequestMethod("GET");

			String nonce = nonce();
			String signature = sign(nonce, query);

			connection.setRequestProperty("AgentId", clientId);
			connection.setRequestProperty("Signature", signature);
			connection.setRequestProperty("Nonce", nonce);
			connection.setRequestProperty("TerminalId", TerminalId);

			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
			}
			
			JsonReader jreader = Json.createReader(connection.getInputStream());
			check = jreader.readObject();
			connection.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (InvalidKeyException e) {
			e.printStackTrace();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		}

		return check;
	}

	public static void main(String[] args) {

		String url = "https://api.cowriesys.com:2443";
		String clientId = "ClientId";
		String clientKey = "ClientKey";

		String net = args[0]; // net can be AIR, ETI, GLO, MTN
		String msisdn = args[1]; // msisdn in international format 234 prefix
		int amount =  Integer.parseInt(args[2]); // minimum varies across networks
		String xref = UUID.randomUUID().toString(); // any unique reference

		AirtimeClient airtime = new AirtimeClient(url, clientId, clientKey);
		JsonObject balance = airtime.balance();
		System.out.println(balance.toString());
		
		/**
		JsonObject credit = airtime.credit(net, msisdn, amount, xref);
		System.out.println(credit.toString());
				
		JsonObject check = airtime.check(xref);
		System.out.println(check.toString());
		*/
	}
}
