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
public class EncryptTest {
	public static void main(String args[]) {

		String ID = "tyouiifan@yahoo.co.jp";
		String encfile = "report.pdf";
		Encrypt enc = new Encrypt("http://localhost:8080/PairingWeb/PKGserver");

		try {
			enc.encrypt(ID, encfile);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
