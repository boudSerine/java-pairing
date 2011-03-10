package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;

/**
 * @author ’£ ˆê–}
 * 
 */
public class Speed {
	private final static BigInteger x1 = new BigInteger(
			"632339556305180632117439061020992460234085159493166733110190446375604882");

	private final static BigInteger y1 = new BigInteger(
			"2343246538743628208776751585690708384614457342502342829230656262040574601");

	private static final FieldElement x = new FieldElement(x1, 241);

	private static final FieldElement y = new FieldElement(y1, 241);

	private static void Field256Debug() {
		System.out.println("Field256 test"); //$NON-NLS-1$
		System.out.println(Messages.getString("Debug.1")); //$NON-NLS-1$

		final FieldElement coefX = x;
		final FieldElement coefY = y;
		System.out.println(coefX.toString());
		System.out.println(x1);
		System.out.println(coefY.toString());
		System.out.println(y1);

		// for (int i = 0; i < 241; i++) {
		// if (coefX.testBit(i)) {
		// System.out.println("coeff " + i);
		// }
		// if (x1.testBit(i)) {
		// System.out.println("Bigint " + i);
		// }
		// }

		// test shiftleft
		// for (int i = 0; i < 241; i++) {
		// coefX.toShiftLeft();
		// System.out.println("coeff " + coefX.toString());
		// x2 = x2.shiftLeft(1);
		// System.out.println("Bigint " + x2.toString());
		// }

		// test shiftleft
		// for (int i = 0; i < 241; i++) {
		// coefX.toShiftRight();
		// System.out.println("coeff " + coefX.toString());
		// x2 = x2.shiftRight(1);
		// System.out.println("Bigint " + x2.toString());
		// }

		long t0 = System.nanoTime();
		for (int i = 0; i <= 10000000; i++) {
			coefX.toXor(coefY);
		}
		long elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc add in nanoSec : " + elapsedTime);

		t0 = System.nanoTime();
		for (int i = 0; i <= 10000000; i++) {
			x1.xor(y1);
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc add in nanoSec : " + elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 1000000; j++) {
			for (int i = 0; i < 241; i++) {
				coefX.testBit(i);
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc bitTest in nanoSec : " + elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 1000000; j++) {
			for (int i = 0; i < 241; i++) {
				x1.testBit(i);
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc bitTest in nanoSec : " + elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			for (int i = 0; i < 241; i++) {
				coefX.shiftLeft();
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc shiftLeft in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			for (int i = 0; i < 241; i++) {
				x1.shiftLeft(1);
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc shiftLeft in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			for (int i = 0; i < 241; i++) {
				coefX.shiftRight();
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc shiftRight in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			for (int i = 0; i < 241; i++) {
				x1.shiftRight(1);
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc shiftRight in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			coefX.hammingCount();
		}
		elapsedTime = System.nanoTime() - t0;
		System.out
				.println("time for calc bitCount in nanoSec : " + elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			x1.bitCount();
		}
		elapsedTime = System.nanoTime() - t0;
		System.out
				.println("time for calc bitCount in nanoSec : " + elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			coefX.bitLength();
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc bitLength in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			x1.bitLength();
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc bitLength in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 1000000; j++) {
			coefX.compareTo(coefY);
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc compareTo in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 1000000; j++) {
			x1.compareTo(y1);
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc compareTo in nanoSec : "
				+ elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			for (int i = 0; i < 256; i++) {
				coefX.setBit(i);
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc setBit in nanoSec : " + elapsedTime);

		t0 = System.nanoTime();
		for (int j = 0; j <= 100000; j++) {
			for (int i = 0; i < 256; i++) {
				x1.setBit(i);
			}
		}
		elapsedTime = System.nanoTime() - t0;
		System.out.println("time for calc setBit in nanoSec : " + elapsedTime);
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		Field256Debug();
	}
}
