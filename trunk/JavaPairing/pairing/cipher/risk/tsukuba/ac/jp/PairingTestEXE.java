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
public class PairingTestEXE {

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
		EllipticCurveGF2m curve = factory.getCurve(aE, bE);

		Pairing pairing = new EtaTPairing(field, bE);

		long t0, t1, elapsedTime;
		final BigInteger a = new BigInteger(m - 1, new Random());
		final BigInteger b = new BigInteger(m - 1, new Random());
		final BigInteger c = new BigInteger(m - 1, new Random());
		final ECPoint P1 = curve.getNewPoint();
		final ECPoint P2 = curve.getNewPoint();
		final ECPoint P3 = curve.getNewPoint();
		final ECPoint aP1 = curve.mul(a, P1);
		final ECPoint bP2 = curve.mul(b, P2);
		final ECPoint cP3 = curve.mul(c, P3);

		t0 = System.nanoTime();
		for (int i = 0; i < 10000; i++) {
			curve.getNewPoint();
		}
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Map To point	:[" + elapsedTime / 10000 + "]");

		t0 = System.nanoTime();
		FieldElement[] out1 = pairing.pairing(cP3, curve.add(aP1, bP2));
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(aP1+bP2,cP3)");
		field.print(out1);

		t0 = System.nanoTime();
		FieldElement[] out2 = pairing.pairing(P1, P3);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P1,P3)");
		field.print(out2);

		out2 = field.extendPow(out2, a.multiply(c));

		t0 = System.nanoTime();
		FieldElement[] out3 = pairing.pairing(P2, P3);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P2,P3)");
		field.print(out3);

		out3 = field.extendPow(out3, b.multiply(c));

		FieldElement[] out4 = field.extendMul(out2, out3);
		System.out.println("e(P2,P3)^bc * e(P1,P3)^ac");
		field.print(out4);

		t0 = System.nanoTime();
		out1 = pairing.pairing(aP1, P1);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(aP,P)");
		field.print(out1);

		t0 = System.nanoTime();
		out4 = pairing.pairing(P1, aP1);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P,aP)");
		field.print(out4);

		t0 = System.nanoTime();
		out4 = pairing.pairing(P1, P1);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.println("Pairing:[" + elapsedTime + "]");
		System.out.println("e(P,P)^a");
		out4 = field.extendPow(out4, a);
		field.print(out4);
	}
}
