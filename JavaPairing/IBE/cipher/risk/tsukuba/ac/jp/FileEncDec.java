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
 * @author ���@��}
 * 
 */
public class FileEncDec {

	public boolean encrypt(PairingKeySpec key) {
		try {
			// ��
			String kagi = "abc12345";
			DESKeySpec dk = key.getDESKey();
			SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
			SecretKey sk = kf.generateSecret(dk);

			// �Í���
			Cipher c = Cipher.getInstance("DES");
			c.init(Cipher.ENCRYPT_MODE, sk);
			byte input[] = "This ia an original message.".getBytes();
			byte encrypted[] = c.doFinal(input); // �Í����f�[�^

			// ��������
			c.init(Cipher.DECRYPT_MODE, sk);
			byte output[] = c.doFinal(encrypted); // ���������f�[�^

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
