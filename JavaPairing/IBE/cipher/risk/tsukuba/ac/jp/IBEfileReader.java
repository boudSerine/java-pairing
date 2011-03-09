/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import pairing.cipher.risk.tsukuba.ac.jp.ECPoint;
import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;
import pairing.cipher.risk.tsukuba.ac.jp.Pairing;

/**
 * @author 張 一凡
 * 
 */
public class IBEfileReader {

	public static void encrypt(String fileName, Pairing pairing, ECPoint P,
			ECPoint Q) {
		try { // (1)
			// (2)Fileオブジェクトの生成
			File inFile = new File(fileName);
			// (3)Fileオブジェクトの生成
			File outFile = new File(fileName.split(".")[0] + "encrypt."
					+ fileName.split(".")[1]);
			// (4)FileReaderクラスのオブジェクトの生成
			FileReader in = new FileReader(inFile);
			// (5)FileWriterクラスのオブジェクトの生成
			FileWriter out = new FileWriter(outFile);

			BufferedReader br = new BufferedReader(in);
			BufferedWriter bw = new BufferedWriter(out);

			String contents; // (6)

			int i = 0;

			FieldElement[] enc = pairing.pairing(P, Q);

			// // (7)読み込みデータがなくなるまで読み込み
			// while ((contents = br.readLine()) != null) {
			// // (8)書き込むデータがなくなるまで書き込み
			// x.changeToBigInteger(new BigInteger(contents));
			// i = 0;
			// while (enc[i] != null) {
			// bw.write(enc[i].toString() + "\n");
			// }
			// }

			in.close(); // (9)読み込みストリームを閉じる
			out.close(); // (10)書き込みストリームを閉じる
		} catch (IOException e) {
		}
	}
}
