/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author í£Å@àÍñ}
 * 
 */
public class DecryptTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String ID = "tyouiifan@yahoo.co.jp";
		String keyFileName = ID + ".key";
		String encfile = "report.pdf.encrypt";
		Decrypt dec = new Decrypt(keyFileName, "http://localhost:8080/PairingWeb/PKGserver");

		try {
			dec.decrypt(encfile, keyFileName);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
