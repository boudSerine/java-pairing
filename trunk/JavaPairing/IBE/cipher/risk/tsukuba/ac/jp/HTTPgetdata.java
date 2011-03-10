/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;

import pairing.cipher.risk.tsukuba.ac.jp.ECPoint;
import pairing.cipher.risk.tsukuba.ac.jp.EtaTPairing;
import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;

/**
 * @author 張　一凡
 * 
 */
public class HTTPgetdata {
	private IBE ibe;

	public HTTPgetdata(String url) {
		try {
			getDatas(url, "POST");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public HTTPgetdata(String url, String req) {
		try {
			getDatas(url, req);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getDatas(String url, String req) throws UnknownHostException,
			IOException, URISyntaxException {
		URL uri = new URL(url);
		HttpURLConnection urlconn = (HttpURLConnection) uri.openConnection();
		urlconn.setRequestMethod("POST");
		urlconn.setRequestProperty("Accept-Language", "ja;q=0.7,en;q=0.3");
		urlconn.connect();

		// コンテンツを出力
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				urlconn.getInputStream()));
		String buffer = reader.readLine();
		int m = Integer.parseInt(buffer);
		buffer = reader.readLine();
		int k = Integer.parseInt(buffer);
		buffer = reader.readLine();
		int aE = Integer.parseInt(buffer);
		buffer = reader.readLine();
		int bE = Integer.parseInt(buffer);
		buffer = reader.readLine();
		// System.out.println(m + "," + k + "," + aE + "," + bE + ",");
		ibe = new IBE(m, k, aE, bE, new Boolean(buffer));
		ibe.setPairing(new EtaTPairing(ibe.getField(), bE));

		FieldElement px = ibe.getField().getZERO();
		FieldElement py = ibe.getField().getZERO();

		buffer = reader.readLine();
		px.changeToBigInteger(new BigInteger(buffer));
		buffer = reader.readLine();
		py.changeToBigInteger(new BigInteger(buffer));
		ibe.setP(new ECPoint(px, py));
		// ibe.getP().print();

		FieldElement spx = ibe.getField().getZERO();
		FieldElement spy = ibe.getField().getZERO();

		buffer = reader.readLine();
		spx.changeToBigInteger(new BigInteger(buffer));
		buffer = reader.readLine();
		spy.changeToBigInteger(new BigInteger(buffer));
		ibe.setSP(new ECPoint(spx, spy));
		// ibe.getSP().print();

		reader.close();
		urlconn.disconnect();
	}

	public IBE getIbe() {
		return ibe;
	}
}
