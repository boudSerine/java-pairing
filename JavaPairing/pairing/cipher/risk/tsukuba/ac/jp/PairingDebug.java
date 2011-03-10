/**
 * 
 */
package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author í£Å@àÍñ}
 * 
 */
public class PairingDebug {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int m = 241;
		int k = 70;
		PairingFactory factory = PairingFactory.getFactory(true, m, k);
		ExtendField field = factory.getField();
		int aE = 1;
		int bE = 1;
		// EllipticCurveGF2m curve = new EllipticCurveGF2m(field, 1, bE);
		EllipticCurveGF2m curve = factory.getCurve(aE, bE);

		Pairing pairing = new EtaTPairing(field, bE);

		long t0, t1, elapsedTime;
		final BigInteger a = new BigInteger(m - 1, new Random());
		final BigInteger b = new BigInteger(m - 1, new Random());
		final BigInteger c = new BigInteger(m - 1, new Random());
		// final BigInteger a = new BigInteger(
		// "2124611639599969119649336047558116195217990132706252586186173541083961426");
		// final BigInteger b = new BigInteger(
		// "2866410189599688061608108956566471394753332679533340589384245876262201426");
		// final BigInteger c = new BigInteger(
		// "3363272369749965600051804906167217470456086290702032656983590135351022871");
		ECPoint P1 = curve.getNewPoint();
		ECPoint aP1 = curve.mul(a, P1);

		for (int i = 0; i < 100; i++) {
			P1 = curve.getNewPoint();
			aP1 = curve.mul(a, P1);

			// System.out.println("P1	");
			// P1.print();
			// System.out.println(P1.getAffineX().bitLength());
			// System.out.println(P1.getAffineY().bitLength());
			// System.out.println("aP1	");
			// aP1.print();
			// System.out.println(aP1.getAffineX().bitLength());
			// System.out.println(aP1.getAffineY().bitLength());

			// t0 = System.nanoTime();
			FieldElement[] out1 = pairing.pairing(aP1, P1);
			// t1 = System.nanoTime();
			// elapsedTime = t1 - t0;
			// System.out.println("Pairing:[" + elapsedTime + "]");
			//			System.out.println("e(aP,P)"); //$NON-NLS-1$
			// field.print(out1);

			// t0 = System.nanoTime();
			FieldElement[] out4 = pairing.pairing(P1, aP1);
			// t1 = System.nanoTime();
			// elapsedTime = t1 - t0;
			// System.out.println("Pairing:[" + elapsedTime + "]");
			//			System.out.println("e(P,aP)"); //$NON-NLS-1$
			// field.print(out4);

			System.out.println(field.equals(out1, out4));
			// if(field.equals(out1, out4)){
			// P1.print();
			// }
		}
	}
}
