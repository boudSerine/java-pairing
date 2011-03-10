/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * @author ���@��}
 * 
 */
public class hash {
	public void printDigest(byte[] digest) {// �_�C�W�F�X�g��16�i���ŕ\������
		for (int i = 0; i < digest.length; i++) {
			int d = digest[i];
			if (d < 0) {// byte�^�ł�128�`255�����l�ɂȂ��Ă���̂ŕ␳
				d += 256;
			}
			if (d < 16) {// 0�`15��16�i����1�����ɂȂ�̂ŁA2�����ɂȂ�悤����0��ǉ�
				System.out.print("0");
			}
			System.out.print(Integer.toString(d, 16));// �_�C�W�F�X�g�l��1�o�C�g��16�i��2�����ŕ\��
		}
		System.out.println();
	}

	public byte[] getFileDigest(String filename) throws Exception {// �t�@�C���̒��g����_�C�W�F�X�g�𐶐�����
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		FileInputStream in = new FileInputStream(filename);
		byte[] dat = new byte[256];
		int len;
		while ((len = in.read(dat)) >= 0) {
			md.update(dat, 0, len);// dat�z��̐擪����len�܂ł̃_�C�W�F�X�g���v�Z����
		}
		in.close();
		return md.digest();
	}

	public byte[] getStringDigest(String data) throws Exception {// �����񂩂�_�C�W�F�X�g�𐶐�����
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] dat = data.getBytes();
		md.update(dat);// dat�z�񂩂�_�C�W�F�X�g���v�Z����
		return md.digest();
	}

	public static void getHash(String pairing[]) throws Exception {
		hash d = new hash();
		d.printDigest(d.getStringDigest(pairing[0]));
		hash d2 = new hash();
		d2.printDigest(d2.getStringDigest(pairing[1]));
	}

	public static void main(String args[]) throws Exception {
		hash d = new hash();
		d.printDigest(d.getFileDigest("secret.txt"));
		d.printDigest(d.getStringDigest("1234"));
	}
}
