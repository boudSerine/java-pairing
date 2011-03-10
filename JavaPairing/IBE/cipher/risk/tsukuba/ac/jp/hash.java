/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * @author 張　一凡
 * 
 */
public class hash {
	public void printDigest(byte[] digest) {// ダイジェストを16進数で表示する
		for (int i = 0; i < digest.length; i++) {
			int d = digest[i];
			if (d < 0) {// byte型では128～255が負値になっているので補正
				d += 256;
			}
			if (d < 16) {// 0～15は16進数で1けたになるので、2けたになるよう頭に0を追加
				System.out.print("0");
			}
			System.out.print(Integer.toString(d, 16));// ダイジェスト値の1バイトを16進数2けたで表示
		}
		System.out.println();
	}

	public byte[] getFileDigest(String filename) throws Exception {// ファイルの中身からダイジェストを生成する
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		FileInputStream in = new FileInputStream(filename);
		byte[] dat = new byte[256];
		int len;
		while ((len = in.read(dat)) >= 0) {
			md.update(dat, 0, len);// dat配列の先頭からlenまでのダイジェストを計算する
		}
		in.close();
		return md.digest();
	}

	public byte[] getStringDigest(String data) throws Exception {// 文字列からダイジェストを生成する
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		byte[] dat = data.getBytes();
		md.update(dat);// dat配列からダイジェストを計算する
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
