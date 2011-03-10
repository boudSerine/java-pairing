/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import pairing.cipher.risk.tsukuba.ac.jp.ECPoint;
import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;

/**
 * @author í£Å@àÍñ}
 * 
 */
public class Decrypt {
	private dataCtrl data;

	private HTTPgetdata getData;
	private int length;
	private ECPoint sQid;

	private ECPoint rP;

	private int skip;

	private int all;

	private int size;

	public Decrypt(String keyFile, String url) {
		getData = new HTTPgetdata(url);

		length = getData.getIbe().getPairing().getPairingLength() / 8;

		data = new dataCtrl(getData.getIbe(), length);

		try {
			loadKey(keyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Decrypt(String url) {
		getData = new HTTPgetdata(url);

		length = getData.getIbe().getPairing().getPairingLength() / 8;

		data = new dataCtrl(getData.getIbe(), length);
	}

	/**
	 * @param string
	 * @param string2
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	void decrypt(String fileName, String keyFile) throws IOException,
			NoSuchAlgorithmException {

		try {
			loadKey(keyFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// rP reading
		rP = rPReader(fileName);

		// new file name is fileName.encrypt.xxx
		String newFileName = fileName.split("\\.")[0] + ".dec."
				+ fileName.split("\\.")[1];

		skip += 2;

		data.fileWite(fileName, newFileName, skip, data.H2(getData.getIbe()
				.getPairing().pairing(rP, sQid)));
	}

	public ECPoint ecMul(ECPoint P, BigInteger mul) {
		ECPoint mP = null;
		if (getData.getIbe().getPairing().getEmbedingDegree() <= 4) {
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
		// sQid.print();
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
		skip = line.getBytes().length;
		xtmp.changeToBigInteger(new BigInteger(line));
		line = br.readLine();
		skip += line.getBytes().length;
		ytmp.changeToBigInteger(new BigInteger(line));
		rP = new ECPoint(xtmp, ytmp);

		br.close();
		in.close();
		return rP;
	}
	
	public dataCtrl getWriter() {
		return this.data;
	}

}
