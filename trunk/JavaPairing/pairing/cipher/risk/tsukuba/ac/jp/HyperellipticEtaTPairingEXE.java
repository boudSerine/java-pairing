package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;

/**
 * An Elliptic Curve Cryptography Library for Pairing Crypto<br>
 * <br>
 * This is a sample class for Tate-pairing.<br>
 * We calcurate Tate pairing whith a supersingular curve,<br>
 * with Miller's algorithm, Distortion map,Elliptic adder<br>
 * on the world of GF2m and GF2km.<br>
 * <br>
 * For the characteristic 2,<br>
 * we defined a supersingular curve y^2+y=x^3+x+1 for this pairing.<br>
 * And set Parameters:<br>
 * [r] the number of points on the finite field GF2m.<br>
 * r = 2^241+2^121+1 (prime number).<br>
 * irreduceble polynomial for GF2m [f(x)].<br>
 * f(x)=x^241+x^70+1.<br>
 * 
 * @author TyouIifan at cipher.risk.tsukuba.ac.jp
 * @version 0.1
 * @since 0.1
 */
public class HyperellipticEtaTPairingEXE {
	static FieldElement one;

	static FieldElement zero;

	/**
	 * @param a
	 * @param b
	 * @return boolean for a = b
	 */
	public static boolean equals(final FieldElement[] a, final FieldElement[] b) {
		boolean test = true;
		for (int i = 0; i <= 11; i++) {
			if (!a[i].equals(b[i])) {
				test = false;
			}
		}
		return test;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// final int m = 103;
		// final int[] ks = { 31 };
		// final int d = 0;
		final int m = 79;
		final int[] ks = { 19 };
		final int d = 1;
		final GF2m12 field = new GF2m12(m, ks);
		one = FieldElement.newONE(m);
		zero = FieldElement.newZERO(m);

		FieldElement[] tmp;
		FieldElement[] pairingOut;
		final FieldElement[] element = { one.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), };

		// read point from bitarray
		// final BigInteger[] readPoint = ReadPoint.getPoint();
		// final BigInteger x = readPoint[0];
		// final BigInteger y = readPoint[1];
		// 103bit d=0
		// final BigInteger x = new
		// BigInteger("3289025971836973637559428228187");
		// final BigInteger y = new
		// BigInteger("7351820053585994852311500787975");
		// 79bit d=1
		// final BigInteger x = new
		// BigInteger("355269194169097312611714");
		// final BigInteger y = new
		// BigInteger("512312321733408530131055");

		final FieldElement x = new FieldElement(new BigInteger(
				"506535945878958563660434"), m);
		final FieldElement y = new FieldElement(new BigInteger(
				"518884608642400753594521"), m);

		final ECPoint P = new ECPoint(x, y);

		final int t = m % 24;
		BigInteger r = null;
		if ((t == 1) | (t == 7) | (t == 17) | (t == 23)) {
			if (d == 1) {
				r = BigInteger.ZERO.setBit(2 * m).setBit(m).setBit(0).subtract(
						BigInteger.ZERO.setBit((3 * m + 1) / 2).setBit(
								(m + 1) / 2));
			} else {
				r = BigInteger.ZERO.setBit(2 * m).setBit((3 * m + 1) / 2)
						.setBit(m).setBit((m + 1) / 2).setBit(0);
			}
		} else {
			if (d == 1) {
				r = BigInteger.ZERO.setBit(2 * m).setBit((3 * m + 1) / 2)
						.setBit(m).setBit((m + 1) / 2).setBit(0);
			} else {
				r = BigInteger.ZERO.setBit(2 * m).setBit(m).setBit(0).subtract(
						BigInteger.ZERO.setBit((3 * m + 1) / 2).setBit(
								(m + 1) / 2));
			}
		}

		final HyperElliptic12EtaPairing pairing = new HyperElliptic12EtaPairing(
				field, d);
		final ECPoint P8 = pairing.hyperMul(P);
		final ECPoint P64 = pairing.hyperMul(P8);

		long t0, t1, elapsedTime;
		t0 = System.nanoTime();
		pairingOut = pairing.pairing(P, P64);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("time for one Pairing culc : " + elapsedTime);
		System.out.println("e(P,P64) = ---------------------");
		PairingOut(pairingOut);

		t0 = System.nanoTime();
		pairingOut = pairing.pairing(P64, P);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("time for one Pairing culc : " + elapsedTime);
		System.out.println("e(64P,P) = ---------------------");
		PairingOut(pairingOut);

		t0 = System.nanoTime();
		pairingOut = pairing.pairing(P, P);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("time for one Pairing culc : " + elapsedTime);
		pairingOut = field.extendPow(pairingOut, BigInteger.valueOf(64));
		System.out.println("e(P,P)^64 = ---------------------");
		PairingOut(pairingOut);

		final FieldElement[] pairingTmp = pairing.pairing(P, P8);

		System.out.print("x^2 = x*x :");
		System.out.println(equals(field.extendMul(pairingOut, pairingOut),
				field.extendSquaring(pairingOut)));

		System.out.print("x^(3) = x*x*x :");
		System.out.println(equals(field.extendMul(field.extendMul(pairingOut,
				pairingOut), pairingOut), field.extendPow(pairingOut,
				BigInteger.valueOf(3))));

		System.out.print("x*y = y*x :");
		System.out.println(equals(field.extendMul(pairingTmp, pairingOut),
				field.extendMul(pairingOut, pairingTmp)));

		tmp = field.extendPow(pairingOut, r);
		System.out.print("e(..)^r = 1 :");
		System.out.println(equals(tmp, element));

		tmp = field.extendPow(pairingOut, BigInteger.ZERO.setBit(12 * m)
				.subtract(BigInteger.ONE));
		System.out.print("e(..)^(q-1) = 1 :");
		System.out.println(equals(tmp, element));

		for (int i = 0; i < 1000; i++) {
			t0 = System.nanoTime();
			pairing.pairing(P, P8);
			t1 = System.nanoTime();
			elapsedTime = t1 - t0;
			System.out.println(elapsedTime);
			System.gc();
		}
	}

	/**
	 * @param out
	 *            a print method output out
	 */
	public static void PairingOut(final FieldElement[] out) {
		System.out.println(out[0].toBigInteger());
		System.out.println(out[1].toBigInteger());
		System.out.println(out[2].toBigInteger());
		System.out.println(out[3].toBigInteger());
		System.out.println(out[4].toBigInteger());
		System.out.println(out[5].toBigInteger());
		System.out.println(out[6].toBigInteger());
		System.out.println(out[7].toBigInteger());
		System.out.println(out[8].toBigInteger());
		System.out.println(out[9].toBigInteger());
		System.out.println(out[10].toBigInteger());
		System.out.println(out[11].toBigInteger());
	}
}
