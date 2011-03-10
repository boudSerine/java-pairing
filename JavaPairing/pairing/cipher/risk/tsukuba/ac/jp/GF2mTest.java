package pairing.cipher.risk.tsukuba.ac.jp;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;

/**
 * @author ’£@ˆê–}
 *
 */
public class GF2mTest {
	final int m = 241;
	final int k = 70;
	final int[] ks = { k };
	BigInteger xBig = BigInteger.probablePrime(m, new Random());
	BigInteger yBig = BigInteger.probablePrime(m, new Random());
	FieldElement xF = new FieldElement(this.xBig, m);
	FieldElement yF = new FieldElement(this.yBig, m);
	// you can do this by next in general.
	// FieldElement xF = new FieldElement(m).getRANDOM(new Random());
	// FieldElement yF = new FieldElement(m).getRANDOM(new Random());
	// We us BigInteger to test this computing methods.


	// final int m = 79;
	//
	// final int k = 19;
	//
	// final int[] ks = { k };
	//
	// final BigInteger xBig = new BigInteger("506535945878958563660434");
	//
	// final BigInteger yBig = new BigInteger("518884608642400753594521");
	//
	// final private Field96 xF = new Field96(this.xBig);
	//
	// final private Field96 yF = new Field96(this.yBig);

	private FieldElement root;

	private final FieldElement one = xF.getONE();

	private final FieldElement zero = xF.getZERO();

	final GF2m field = new GF2m(this.m, this.ks);

	/**
	 *
	 */
	@Test
	public void testAdd() {
		System.out.println("xor test");
		System.out.println("coef x		:" + this.xBig);
		System.out.println("Big x		:" + this.xF.toBigInteger());
		System.out.println("coef y		:" + this.yBig);
		System.out.println("Big y		:" + this.yF.toBigInteger());

		System.out.println("field mod	:" + this.field.modulus.toBigInteger());
		System.out.println();
		long t0 = System.nanoTime();
		for (int i = 100000000; i >= 0; i--) {
			xF.xor(yF);
		}
		long t1 = System.nanoTime();
		System.out.println((t1 - t0) / 100000000 + " XOR time");

		assertTrue(this.xF.xor(this.yF).toBigInteger().equals(
				this.xBig.xor(this.yBig)));
		assertTrue(this.field.add(this.xF, this.yF)
				.equals(this.xF.xor(this.yF)));
	}

	/**
	 *
	 */
	@Test
	public void testInv() {
		System.out.println();
		System.out.println("div test");
		long t0 = System.nanoTime();
		for (int i = 100000; i >= 0; i--) {
			this.field.inv(xF);
		}
		long t1 = System.nanoTime();
		System.out.println((t1 - t0) / 100000 + " inv time");
		System.out.println(this.field.div(this.yF, this.yF).toBigInteger());
		assertTrue(this.field.div(this.yF, this.yF).equals(
				new FieldElement(m).getONE()));
	}

	/**
	 *
	 */
	@Test
	public void testMul() {
		System.out.println();
		System.out.println("mul test");

		BigInteger fieldW = BigInteger.ZERO;
		BigInteger fieldU = this.xBig;
		final BigInteger fieldV = this.yBig;
		BigInteger tmp = BigInteger.ZERO;
		final BigInteger modulus = this.field.modulus.toBigInteger();

		if (fieldU.compareTo(fieldV) > 0) {
			tmp = fieldU;
			fieldU = fieldV;
		} else {
			tmp = fieldV;
		}
		for (int i = 0; i < fieldU.bitLength(); i++) {
			if (fieldU.testBit(i)) {
				fieldW = fieldW.xor(tmp);
			}
			tmp = tmp.shiftLeft(1);
			if (tmp.testBit(this.m)) {
				tmp = tmp.xor(modulus);
			}
		}

		System.out.println("BigInteger mul	:" + fieldW);
		final FieldElement mul = this.field.mul(this.xF, this.yF);
		long t0 = System.nanoTime();
		for (int i = 1000000; i >= 0; i--) {
			this.field.mul(this.xF, this.yF);
		}
		long t1 = System.nanoTime();
		System.out.println((t1 - t0) / 1000000 + " mul time");

		System.out.println("Field 256  mul	:" + mul.toBigInteger());

		System.out.println("Field 256  mul(x,y)	:"
				+ this.field.mul(this.xF, this.yF).toBigInteger());
		System.out.println("Field 256  mul(y,x)	:"
				+ this.field.mul(this.yF, this.xF).toBigInteger());
		assertTrue(mul.toBigInteger().equals(fieldW));
	}

	/**
	 *
	 */
	@Test
	public void testPow() {
		assertTrue(this.field.pow(this.xF,
				BigInteger.ONE.shiftLeft(this.field.getM())).equals(this.xF));
	}

	/**
	 *
	 */
	@Test
	public void testRand() {
		System.out.println();
		System.out.println("div test");
		long t0 = System.nanoTime();
		Random rand = new Random();
		for (int i = 100000; i >= 0; i--) {
			FieldElement.newRANDOM(241, rand);
			// rand.nextInt();
		}
		long t1 = System.nanoTime();
		System.out.println((t1 - t0) / 100000 + " random time");

		assertTrue(true);
	}

	/**
	 *
	 */
	@Test
	public void testSquareRoot() {
		System.out.println();
		System.out.println("square root test");
		FieldElement tmp;

		if (k % 2 == 0) {
			if (1 + 2 * k - this.m < 0) {
				this.root = field.mul(zero.getClone().setBit(
						(1 + 4 * k - this.m) / 2).setBit(
						(this.m + 2 * k + 1) / 2).setBit((this.m + 1) / 2),
						zero.getClone().setBit(k / 2).setBit(0));
			} else {
				this.root = field.mul(zero.getClone().setBit(
						(1 + 2 * k - this.m)).setBit((this.m + 1) / 2), zero
						.getClone().setBit(k / 2).setBit(0));
			}
		} else {
			this.root = zero.getClone();
			this.root.toSetBit((this.m + 1) / 2);
			this.root.toSetBit((k + 1) / 2);
		}

		long t0 = System.nanoTime();
		for (int i = 1000000; i >= 0; i--) {
			this.field.squareRoot(this.xF, root);
		}
		long t1 = System.nanoTime();
		System.out.println((t1 - t0) / 1000000 + " square time");
		tmp = this.field.squareRoot(this.xF, root);
		assertTrue(this.xF.toBigInteger().equals(
				this.field.squaring(tmp).toBigInteger()));
	}

	/**
	 *
	 */
	@Test
	public void testSquaring() {
		System.out.println();
		System.out.println("squaring test");
		long t0 = System.nanoTime();
		for (int i = 10000000; i >= 0; i--) {
			this.field.squaring(this.xF);
		}
		long t1 = System.nanoTime();
		System.out.println((t1 - t0) / 10000000 + " square time");
		System.out.println("mul		:"
				+ this.field.mul(this.xF, this.xF).toBigInteger());
		System.out.println("squaring	:"
				+ this.field.squaring(this.xF).toBigInteger());
		assertTrue(this.field.mul(this.xF, this.xF).toBigInteger().equals(
				this.field.squaring(this.xF).toBigInteger()));
	}

}
