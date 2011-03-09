package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;
import java.util.Random;

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
public class ModifiedTatePairingEXE {
	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final int m = 241;
		final int[] ks = { 70 };
		final GF2m4 field = new GF2m4(m, ks);
		final int bE = 1;

		final EllipticCurveGF2m curve = new EllipticCurveGF2m(field, 1, bE);
		// final BigInteger x = ReadPoint.getPoint()[0];
		// final BigInteger y = ReadPoint.getPoint()[1];

		final int a = new Random().nextInt(10000);
		final FieldElement x1 = new FieldElement(
				new BigInteger(
						"632339556305180632117439061020992460234085159493166733110190446375604882"),
				m);
		final FieldElement y1 = new FieldElement(
				new BigInteger(
						"2343246538743628208776751585690708384614457342502342829230656262040574601"),
				m);
		final ECPoint P1 = new ECPoint(x1, y1);

		final int b = new Random().nextInt(10000);
		final FieldElement x2 = new FieldElement(
				new BigInteger(
						"835472525089007404659377797704063966221443189193931255188963973035036084"),
				m);
		final FieldElement y2 = new FieldElement(
				new BigInteger(
						"668918863234757085904318057717239595025961308343648712033950603315929751"),
				m);
		final ECPoint P2 = new ECPoint(x2, y2);

		final int c = new Random().nextInt(10000);
		final FieldElement x3 = new FieldElement(
				new BigInteger(
						"3329310949690210493564575294092260719999988233600337513359596248097149225"),
				m);
		final FieldElement y3 = new FieldElement(
				new BigInteger(
						"785102023414967955694578090580660992307140023903550337166951229119267235"),
				m);
		final ECPoint P3 = new ECPoint(x3, y3);

		final ECPoint aP1 = curve.mul(BigInteger.valueOf(a), P1);
		final ECPoint bP2 = curve.mul(BigInteger.valueOf(b), P2);
		final ECPoint cP3 = curve.mul(BigInteger.valueOf(c), P3);

		final ModifiedTatePairing pairing = new ModifiedTatePairing(field,
				curve, b);

		// final BigInteger r = BigInteger.valueOf(2).pow(241).subtract(
		// BigInteger.valueOf(2).pow(121)).add(BigInteger.ONE);
		// System.out.println(curve.mul(r, P1).getAffineX().toBigInteger());
		// System.out.println(curve.mul(r, P1).getAffineY().toBigInteger());

		long t0, t1, elapsedTime;

		t0 = System.nanoTime();
		final FieldElement[] out1 = pairing.pairing(curve.add(aP1, bP2), cP3);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(aP1+bP2,cP3)"); //$NON-NLS-1$
		PairingOut(out1);

		t0 = System.nanoTime();
		FieldElement[] out2 = pairing.pairing(P1, P3);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P1,P3)"); //$NON-NLS-1$
		PairingOut(out2);

		out2 = field.extendPow(out2, BigInteger.valueOf(a * c));

		t0 = System.nanoTime();
		FieldElement[] out3 = pairing.pairing(P2, P3);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P2,P3)"); //$NON-NLS-1$
		PairingOut(out3);

		out3 = field.extendPow(out3, BigInteger.valueOf(b * c));

		FieldElement[] out4 = field.extendMul(out2, out3);
		System.out.println("e(P2,P3)^bc * e(P1,P3)^ac"); //$NON-NLS-1$
		PairingOut(out4);

		t0 = System.nanoTime();
		out4 = pairing.pairing(aP1, P1);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(aP,P)"); //$NON-NLS-1$
		PairingOut(out4);

		t0 = System.nanoTime();
		out4 = pairing.pairing(P1, aP1);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P,aP)"); //$NON-NLS-1$
		PairingOut(out4);

		t0 = System.nanoTime();
		out4 = pairing.pairing(P1, P1);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P,P)^a"); //$NON-NLS-1$
		PairingOut(field.extendPow(out4, BigInteger.valueOf(a)));

		// t0 = System.nanoTime();
		for (int i = 0; i < 1000; i++) {
			t0 = System.nanoTime();
			pairing.pairing(P2, P3);
			t1 = System.nanoTime();
			elapsedTime = t1 - t0;
			System.out.println(elapsedTime);
			// System.gc();
		}
		// t1 = System.nanoTime();
		// elapsedTime = t1 - t0;
		// System.out.println(elapsedTime/1000);

	}

	/**
	 * @param out
	 */
	public static void PairingOut(final FieldElement[] out) {
		System.out.println(out[0].toBigInteger());
		System.out.println(out[1].toBigInteger());
		System.out.println(out[2].toBigInteger());
		System.out.println(out[3].toBigInteger());
	}
}
