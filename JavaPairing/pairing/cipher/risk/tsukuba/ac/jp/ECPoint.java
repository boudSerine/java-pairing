package pairing.cipher.risk.tsukuba.ac.jp;

import java.util.Random;

/**
 * This class holds two FieldElements as point
 *
 * For the purpose of speeding up, this class is not extends java.security.spec.ECPoint.
 * BigInteger is too slow to compute pairing.
 *
 * @author í£Å@àÍñ}
 *
 */
public class ECPoint {
	/**
	 * @param ID
	 * @param map
	 * @return get new Field element
	 */
	public static FieldElement hashID(String ID, EllipticCurveGF2m map) {
		return FieldElement.newRANDOM(map.getField().getM(), new Random());
	}

	/**
	 * TODO make methods to output BigInteger to extends java.security.spec.ECPoint
	 */

	/**
	 * @param map
	 * @return new field element for get new point
	 */
	public static FieldElement newX(EllipticCurveGF2m map) {
		Random rand = new Random();
		FieldElement ret = FieldElement.newRANDOM(map.getField().getM(), rand);
		while (!map.traceTest(ret)) {
			ret = FieldElement.newRANDOM(map.getField().getM(), rand);
		}
		return ret;
	}

	FieldElement x;

	FieldElement y;

	/**
	 * @param x
	 * @param y
	 */
	public ECPoint(final FieldElement x, final FieldElement y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return y of (x,y)
	 */
	public FieldElement getAffineX() {
		return this.x.getClone();
	}

	/**
	 * @return x of (x,y)
	 */
	public FieldElement getAffineY() {
		return this.y.getClone();
	}

	/**
	 *
	 */
	public void print() {
		System.out.println(x.toBigInteger().toString());
		System.out.println(y.toBigInteger().toString());
	}

	/**
	 * @param x
	 */
	public void setAffineX(FieldElement x) {
		this.x = x;
	}

	/**
	 * @param y
	 */
	public void setAffineY(FieldElement y) {
		this.y = y;
	}
}
