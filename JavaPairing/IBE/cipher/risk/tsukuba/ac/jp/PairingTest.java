/**
 *
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import pairing.cipher.risk.tsukuba.ac.jp.ECPoint;
import pairing.cipher.risk.tsukuba.ac.jp.EllipticCurveGF2m;
import pairing.cipher.risk.tsukuba.ac.jp.ExtendField;
import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;
import pairing.cipher.risk.tsukuba.ac.jp.GF2m;
import pairing.cipher.risk.tsukuba.ac.jp.PairingFactory;

/**
 * @author í£Å@àÍñ}
 *
 */
public class PairingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PairingTest test = new PairingTest();
		test.setup("tyouiifan@yahoo.co.jp.key");

	}

	private dataCtrl data;

	private HTTPgetdata getData;
	private int length;
	private ECPoint sQid;

	private ECPoint Q;

	private BigInteger rBig;

	private ECPoint Qid;

	private BigInteger sKey;

	public void setup(String keyFile) {
		getData = new HTTPgetdata("http://localhost:8080/PairingWeb/PKGserver");

		length = getData.getIbe().getPairing().getPairingLength() / 8;

		data = new dataCtrl(getData.getIbe(), length);

		// befor maptopoint complete
		int m = 241;
		int k = 70;
		PairingFactory factory = PairingFactory.getFactory(true, m, k);
		ExtendField field = factory.getField();
		int aE = 1;
		int bE = 1;
		EllipticCurveGF2m curve = factory.getCurve(aE, bE);
		Q = curve.getNewPoint();

		try {
			loadKey(keyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String ID = "tyouiifan@yahoo.co.jp";
		String messageFile = "report.pdf";
		String cipherFile = "report.pdf.encrypt";

		System.out.println("P condition:");
		getData.getIbe().getP().print();

		System.out.println("sP condition:");
		getData.getIbe().getSP().print();

		try {
			encryptCondition(ID, messageFile);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			decryptCondition(ID, cipherFile);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void encryptCondition(String ID, String fileName)
			throws IOException, NoSuchAlgorithmException {
		// make random number r
		rBig = new BigInteger(getData.getIbe().getField().getM(), new Random());
		FieldElement r = getData.getIbe().getField().getZERO();
		r.changeToBigInteger(rBig);

		System.out.println("r = :" + r.toBigInteger().toString());
		System.out.println("rBig = :" + r.toBigInteger().toString());

		// make rP
		ECPoint rP = ecMul(getData.getIbe().getP(), rBig);
		System.out.println("rP :");
		rP.print();

		// new file name is fileName.encrypt.xxx
		String newFileName = fileName + ".encrypt";

		FileWriter in = new FileWriter(newFileName);
		BufferedWriter br = new BufferedWriter(in);
		br.write(rP.getAffineX().toBigInteger().toString() + "\n");
		br.write(rP.getAffineY().toBigInteger().toString() + "\n");
		br.close();
		in.close();

		System.out.println("ID to BigInteger = :" + data.idHash(ID));
		System.out.println("ID to BigInteger = :" + data.idHash(ID));
		System.out.println("ID to BigInteger.length = :"
				+ data.idHash(ID).bitLength());

		Qid = ecMul(Q, data.idHash(ID));
		System.out.println(ID);
		System.out.println("Qid :");
		Qid.print();

		FieldElement[] pairing = getData.getIbe().getField().extendPow(
				getData.getIbe().getPairing().pairing(getData.getIbe().getSP(),
						Qid), rBig);
		System.out.println(" encrypt pairing e(sP, Qid)^r");
		System.out.println(pairing[0].toBigInteger().toString());
		System.out.println(pairing[1].toBigInteger().toString());
		System.out.println(pairing[2].toBigInteger().toString());
		System.out.println(pairing[3].toBigInteger().toString());

		// data.fileWriter(fileName, newFileName, data.H2(getData.getField()
		// .extendPow(
		// getData.getIbe().getPairing().pairing(getData.getSP(),
		// Qid), rBig)));
	}

	/**
	 * @param string
	 * @param string2
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	void decryptCondition(String ID, String fileName) throws IOException,
			NoSuchAlgorithmException {
		// rP reading
		ECPoint rP = rPReader(fileName);
		System.out.println("file get key rP:");
		rP.print();

		// new file name is fileName.encrypt.xxx
		String newFileName = fileName.split("\\.")[0] + ".dec."
				+ fileName.split("\\.")[1];

		System.out.println("http get sQid");
		sQid.print();

		ECPoint tmp = ecMul(Q, data.idHash(ID));
		tmp = ecMul(tmp, sKey);
		System.out.println("computed sQid");
		tmp.print();

		System.out.println("computed sQid");

		tmp = null;
		getData.getIbe().getP().print();
		tmp = getData.getIbe().getCurve().mul(sKey, getData.getIbe().getP());
		System.out.println("computed sP");
		tmp.print();

		FieldElement[] pairing = getData.getIbe().getPairing()
				.pairing(rP, sQid);
		System.out.println(" decrypt pairing e(rP,sQid)");
		System.out.println(pairing[0].toBigInteger().toString());
		System.out.println(pairing[1].toBigInteger().toString());
		System.out.println(pairing[2].toBigInteger().toString());
		System.out.println(pairing[3].toBigInteger().toString());

		// data.fileWriter(newFileName, data.H2(getData.getIbe().getPairing()
		// .pairing(rP, sQid)));
	}

	public ECPoint ecMul(ECPoint P, BigInteger mul) {
		ECPoint mP = null;
		if (getData.getIbe().isEllipticPairing()) {
			mP = getData.getIbe().getCurve().mul(mul, P);
		} else {
			mP = getData.getIbe().getPairing().hyperMul(P);
		}
		return mP;
	}

	public void loadKey(String keyFile) throws IOException {
		FieldElement xtmp = getData.getIbe().getField().getZERO();
		FieldElement ytmp = getData.getIbe().getField().getZERO();
		FileReader in = new FileReader(keyFile);
		BufferedReader br = new BufferedReader(in);
		String line;
		line = br.readLine();
		xtmp.changeToBigInteger(new BigInteger(line));
		line = br.readLine();
		ytmp.changeToBigInteger(new BigInteger(line));
		sQid = new ECPoint(xtmp, ytmp);
		line = br.readLine();
		sKey = new BigInteger(line);
		System.out.println("skey : " + line);
		br.close();
		in.close();
	}

	public ECPoint rPReader(String fileName) throws IOException {
		// key reading
		ECPoint rP = null;
		FieldElement xtmp = getData.getIbe().getPairing().getField().getZERO();
		FieldElement ytmp = getData.getIbe().getPairing().getField().getZERO();
		FileReader in = new FileReader(fileName);
		BufferedReader br = new BufferedReader(in);

		String line;
		line = br.readLine();
		xtmp.changeToBigInteger(new BigInteger(line));
		line = br.readLine();
		ytmp.changeToBigInteger(new BigInteger(line));
		rP = new ECPoint(xtmp, ytmp);

		br.close();
		in.close();
		return rP;
	}
}
