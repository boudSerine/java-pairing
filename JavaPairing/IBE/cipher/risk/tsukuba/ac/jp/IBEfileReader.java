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
 * @author �� ��}
 * 
 */
public class IBEfileReader {

	public static void encrypt(String fileName, Pairing pairing, ECPoint P,
			ECPoint Q) {
		try { // (1)
			// (2)File�I�u�W�F�N�g�̐���
			File inFile = new File(fileName);
			// (3)File�I�u�W�F�N�g�̐���
			File outFile = new File(fileName.split(".")[0] + "encrypt."
					+ fileName.split(".")[1]);
			// (4)FileReader�N���X�̃I�u�W�F�N�g�̐���
			FileReader in = new FileReader(inFile);
			// (5)FileWriter�N���X�̃I�u�W�F�N�g�̐���
			FileWriter out = new FileWriter(outFile);

			BufferedReader br = new BufferedReader(in);
			BufferedWriter bw = new BufferedWriter(out);

			String contents; // (6)

			int i = 0;

			FieldElement[] enc = pairing.pairing(P, Q);

			// // (7)�ǂݍ��݃f�[�^���Ȃ��Ȃ�܂œǂݍ���
			// while ((contents = br.readLine()) != null) {
			// // (8)�������ރf�[�^���Ȃ��Ȃ�܂ŏ�������
			// x.changeToBigInteger(new BigInteger(contents));
			// i = 0;
			// while (enc[i] != null) {
			// bw.write(enc[i].toString() + "\n");
			// }
			// }

			in.close(); // (9)�ǂݍ��݃X�g���[�������
			out.close(); // (10)�������݃X�g���[�������
		} catch (IOException e) {
		}
	}
}
