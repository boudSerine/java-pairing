/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

import pairing.cipher.risk.tsukuba.ac.jp.ECPoint;
import pairing.cipher.risk.tsukuba.ac.jp.EllipticCurveGF2m;
import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;
import pairing.cipher.risk.tsukuba.ac.jp.GF2m12;
import pairing.cipher.risk.tsukuba.ac.jp.GF2m4;
import pairing.cipher.risk.tsukuba.ac.jp.Pairing;
import pairing.cipher.risk.tsukuba.ac.jp.ExtendField;

/**
 * @author 張 一凡
 * 
 */
public class IBE {

	private int aE;

	private int bE;

	private EllipticCurveGF2m curve;

	private BigInteger dID;

	private boolean ellipticPairing;

	private ExtendField field;

	private int k;

	private int m;

	private ECPoint P;

	private Pairing pairing = null;

	private ECPoint Qid;

	private FieldElement r;

	private BigInteger rBigInt;

	private BigInteger Skey;

	private ECPoint sP;

	private ECPoint sQid;

	/**
	 * @param i
	 * @param j
	 */
	public IBE(int m, int k, int aE, int bE, boolean elliptic) {
		// TODO 自動生成されたコンストラクター・スタブ
		this.m = m;
		this.k = k;
		final int[] ks = { k };
		this.aE = aE;
		this.bE = bE;
		this.ellipticPairing = elliptic;
		if (elliptic) {
			setField(new GF2m4(m, ks));
		} else {
			setField(new GF2m12(m, ks));
		}
		if (ellipticPairing) {
			curve = new EllipticCurveGF2m(field, aE, bE);
		}
	}

	public int getAE() {
		return aE;
	}

	public int getBE() {
		return bE;
	}

	public EllipticCurveGF2m getCurve() {
		return curve;
	}

	public ExtendField getField() {
		return field;
	}

	public int getK() {
		return k;
	}

	public int getM() {
		return m;
	}

	public ECPoint getP() {
		return P;
	}

	/**
	 * @return the pairing
	 */
	public Pairing getPairing() {
		return pairing;
	}

	/**
	 * @return the skey
	 */
	public BigInteger getSkey() {
		return Skey;
	}

	public ECPoint getSP() {
		return sP;
	}

	public void IBE(int m, int k, int aE, int bE) {
		this.m = m;
		this.k = k;
		final int[] ks = { k };
		this.aE = aE;
		this.bE = bE;
		setField(new GF2m4(m, ks));
		if (ellipticPairing) {
			curve = new EllipticCurveGF2m(field, aE, bE);
		}
	}

	public boolean isEllipticPairing() {
		return ellipticPairing;
	}

	public void print() {
		System.out.println(m + "\n");
		System.out.println(k + "\n");
		System.out.println(aE + "\n");
		System.out.println(bE + "\n");
		System.out.println(ellipticPairing + "\n");
		System.out.println(getSkey().toString() + "\n");
		System.out.println(P.getAffineX().toBigInteger().toString() + "\n");
		System.out.println(P.getAffineY().toBigInteger().toString() + "\n");
		System.out.println(sP.getAffineX().toBigInteger().toString() + "\n");
		System.out.println(sP.getAffineY().toBigInteger().toString() + "\n");
	}

	public void save(String fileName) {
		try {
			FileWriter out = new FileWriter(fileName);
			BufferedWriter wr = new BufferedWriter(out);
			wr.write(m + "\n");
			wr.write(k + "\n");
			wr.write(aE + "\n");
			wr.write(bE + "\n");
			wr.write(ellipticPairing + "\n");
			wr.write(getSkey().toString() + "\n");
			wr.write(P.getAffineX().toBigInteger().toString() + "\n");
			wr.write(P.getAffineY().toBigInteger().toString() + "\n");
			wr.write(sP.getAffineX().toBigInteger().toString() + "\n");
			wr.write(sP.getAffineY().toBigInteger().toString() + "\n");
			wr.close();
			out.close();
		} catch (IOException e) {
			System.out.println("save missed");
			System.out.println(e);
		}
	}

	public void setEllipticPairing(boolean ellipticPairing) {
		this.ellipticPairing = ellipticPairing;
	}

	public void setField(ExtendField field) {
		this.field = field;
	}

	public void setK(int k) {
		this.k = k;
	}

	public void setM(int m) {
		this.m = m;
	}

	public void setP(ECPoint p) {
		P = p;
	}

	/**
	 * @param pairing
	 *            the pairing to set
	 */
	public void setPairing(Pairing pairing) {
		this.pairing = pairing;
	}

	/**
	 * @param skey
	 *            the skey to set
	 */
	public void setSkey(BigInteger skey) {
		Skey = skey;
	}

	public void setSP(ECPoint sp) {
		sP = sp;
	}

	public void setup() {
		setSkey(new BigInteger(m - 1, new Random()));
		FieldElement x = field.getZERO();

		// set P directry befor we implement MapToPoint
		x
				.changeToBigInteger(new BigInteger(
						"3329310949690210493564575294092260719999988233600337513359596248097149225"));
		FieldElement y = field.getZERO();
		y
				.changeToBigInteger(new BigInteger(
						"785102023414967955694578090580660992307140023903550337166951229119267235"));
		P = new ECPoint(x, y);

		// mapToPoint = new MapToPoint(field, aE, bE);
		// x.changeToBigInteger(new BigInteger(m, new Random()));
		// P = mapToPoint.getPoint(x);

		sP = curve.mul(Skey, P);
	}

	public void computeSP() {
		sP = curve.mul(Skey, P);
	}
}
