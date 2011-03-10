/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import pairing.cipher.risk.tsukuba.ac.jp.PairingKeySpec;

/**
 * @author 張　一凡
 * 
 */
public class FileEncDec {

	public boolean encrypt(PairingKeySpec key) {
		try {
			// 鍵
			String kagi = "abc12345";
			DESKeySpec dk = key.getDESKey();
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
			SecretKey sk = kf.generateSecret(dk);

			// 暗号化
			Cipher c = Cipher.getInstance("DES");
			c.init(Cipher.ENCRYPT_MODE, sk);
			byte input[] = "This ia an original message.".getBytes();
			byte encrypted[] = c.doFinal(input); // 暗号化データ

			// 復号処理
			c.init(Cipher.DECRYPT_MODE, sk);
			byte output[] = c.doFinal(encrypted); // 復号したデータ

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
