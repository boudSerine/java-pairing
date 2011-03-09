/**
 *
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.swing.JProgressBar;

import com.sun.xml.internal.bind.v2.TODO;

import pairing.cipher.risk.tsukuba.ac.jp.ECPoint;
import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;

/**
 * @author í£Å@àÍñ}
 *
 */
public class Encrypt {
	private dataCtrl data;

	private HTTPgetdata getData;
	private int length;

	private ECPoint Q;

	public ECPoint ecMul(ECPoint P, BigInteger mul) {
		ECPoint mP = null;
		if (getData.getIbe().getPairing().getEmbedingDegree() <= 4) {
			mP = getData.getIbe().getCurve().mul(mul, P);
		} else {
			mP = getData.getIbe().getPairing().hyperMul(P);
		}
		return mP;
	}

	// private static Pairing pairing;
	/**
	 * @param Q
	 * @param string
	 * @param string2
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 *             W
	 */
	public void encrypt(String ID, String fileName) throws IOException,
			NoSuchAlgorithmException {
		// make random number r
		BigInteger rBig = new BigInteger(
				getData.getIbe().getField().getM() - 2, new Random());
		FieldElement r = getData.getIbe().getField().getZERO();
		r.changeToBigInteger(rBig);

		// make rP
		ECPoint rP = ecMul(getData.getIbe().getP(), rBig);
		// System.out.println("encrypt key rP");
		// rP.print();

		// new file name is fileName.encrypt.xxx
		String newFileName = fileName + ".encrypt";
		FileWriter in = new FileWriter(newFileName);
		BufferedWriter br = new BufferedWriter(in);
		br.write(rP.getAffineX().toBigInteger().toString() + "\n");
		br.write(rP.getAffineY().toBigInteger().toString() + "\n");
		br.close();
		in.close();

		ECPoint Qid = ecMul(Q, data.idHash(ID));
		data.fileWriter(fileName, newFileName, data.H2(getData.getIbe()
				.getField().extendPow(
						getData.getIbe().getPairing().pairing(
								getData.getIbe().getSP(), Qid), rBig)));
	}

	public Encrypt(String url) {
		getData = new HTTPgetdata(url);

		length = getData.getIbe().getPairing().getPairingLength() / 8;

		this.data = new dataCtrl(getData.getIbe(), length);

		// befor maptopoint complete
		//TODO
	}

	public dataCtrl getWriter() {
		return this.data;
	}

}
