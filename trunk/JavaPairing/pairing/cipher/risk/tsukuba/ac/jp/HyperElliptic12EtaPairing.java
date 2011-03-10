package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;

/**
 * An Elliptic Curve Cryptography Library for Pairing Crypto<br>
 * <br>
 * This progarm calc Hyperelliptic pairing over binary filed.<br>
 * 
 * @author TyouIifan at cipher.risk.tsukuba.ac.jp
 * @version 0.1
 * @since 0.1
 */
public class HyperElliptic12EtaPairing implements Pairing {
	/**
	 * finite field for Pairing
	 */
	private GF2m12 field;

	private int lamda;

	private int m;

	/**
	 * BigInteger.ONE
	 */
	public FieldElement one;

	private int p;

	/**
	 * * BigInteger.ZERO
	 */
	public FieldElement zero;

	public final int EmbedingDegree = 12;

	private static FieldElement[] pairingReturn;

	/**
	 * @param field
	 *            finite filed for pairing
	 * @param d
	 *            the param of hyperelliptic curve equation
	 */
	public HyperElliptic12EtaPairing(final GF2m12 field, final int d) {
		this.field = field;
		this.m = field.getM();
		if ((this.m % 24 == 1) | (this.m % 24 == 7) | (this.m % 24 == 17)
				| (this.m % 24 == 23)) {
			if (d == 1) {
				this.p = 1;
			} else {
				this.p = 0;
			}
		} else {
			if (d == 1) {
				this.p = 0;
			} else {
				this.p = 1;
			}
		}
		if (this.m % 4 == 1) {
			this.lamda = 1;
		} else {
			this.lamda = 0;
		}
		this.zero = field.modulus.getZERO();
		this.one = field.modulus.getONE();
	}

	private FieldElement[] finalExpo(final FieldElement[] u) {
		// if (this.p == 1) {
		// final BigInteger pow = BigInteger.ZERO.setBit(9 * this.m).setBit(
		// (21 * this.m + 1) / 2).setBit(0).subtract(
		// BigInteger.ZERO.setBit(6 * this.m).setBit(3 * this.m)
		// .setBit((9 * this.m + 1) / 2)).mod(
		// BigInteger.ZERO.setBit(12 * this.m)
		// .subtract(BigInteger.ONE));
		// return this.field.extendPow(u, pow);
		// } else {
		// final BigInteger pow = BigInteger.ZERO.setBit(9 * this.m).setBit(
		// (9 * this.m + 1) / 2).setBit(0).subtract(
		// BigInteger.ZERO.setBit((21 * this.m + 1) / 2).setBit(
		// 6 * this.m).setBit(3 * this.m)).mod(
		// BigInteger.ZERO.setBit(12 * this.m)
		// .subtract(BigInteger.ONE));
		// return this.field.extendPow(u, pow);
		// }
		return field.finalExp(u);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.Pairing#getEmbedingDegree()
	 */
	@Override
	public int getEmbedingDegree() {
		return EmbedingDegree;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.Pairing#getField()
	 */
	@Override
	public ExtendField getField() {
		// TODO Auto-generated method stub
		return field;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.Pairing#getpPairingLength()
	 */
	@Override
	public int getPairingLength() {
		return EmbedingDegree * m;
	}

	/**
	 * a method for octupling points for a form of Divisor
	 * 
	 * @param P
	 * @return 8P
	 */
	public ECPoint hyperMul(final ECPoint P) {
		final FieldElement x = P.getAffineX();
		final FieldElement y = P.getAffineY();
		final ECPoint P8 = new ECPoint(this.field.pow(x
				.xor(this.one.getClone()), BigInteger.valueOf(64)), this.field
				.pow(y.xor(this.field.squaring(x)).xor(this.one.getClone()),
						BigInteger.valueOf(64)));
		return P8;
	}

	private FieldElement[] lamda0(final ECPoint P, final ECPoint Q) {
		// x,y for P=(x1,y1),Q=(x2,y2)
		final FieldElement[] x1 = new FieldElement[this.m];
		final FieldElement[] y1 = new FieldElement[this.m];
		final FieldElement[] x2 = new FieldElement[this.m];
		final FieldElement[] y2 = new FieldElement[this.m];

		// a for alpha on Algorithm 4
		final FieldElement[] a = { this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.one.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(), };
		// b for beta on Algorithm 4
		final FieldElement[] b = { this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.one.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(), };
		// f is the output
		FieldElement[] f = { this.one.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(), };

		int k1, k2, k3, k4, k5, k6;

		x1[0] = P.getAffineX();
		y1[0] = P.getAffineY();
		x2[0] = Q.getAffineX();
		y2[0] = Q.getAffineY();

		// Precompute powers of P and Q
		// xi[j]=x^2^j,yi[j]=y^2^j <j
		for (int j = 1; j < this.m; j++) {
			x1[j] = this.field.squaring(x1[j - 1]);
			y1[j] = this.field.squaring(y1[j - 1]);
			x2[j] = this.field.squaring(x2[j - 1]);
			y2[j] = this.field.squaring(y2[j - 1]);
		}

		long t0, t1, elapsedTime;
		t0 = System.nanoTime();
		for (int i = 0; i <= (this.m - 3) / 2; i++) {
			// all ki = in the next 2 line to be considered modulo m
			k1 = ((3 * this.m - 9 - 6 * i) / 2) % this.m;
			k2 = (k1 + 1) % this.m;
			k3 = (k2 + 1) % this.m;
			k4 = ((3 * this.m - 3 + 6 * i) / 2) % this.m;
			k5 = (k4 + 1) % this.m;
			k6 = (k5 + 1) % this.m;

			// calculate a
			a[4] = x1[k4].xor(x1[k5]);
			a[0] = y2[k2].xor(y1[k4]).xor(this.field.mul(a[4], x2[k3])).xor(
					this.field.mul(x1[k4].xor(x2[k3]).xor(this.one.getClone()),
							x2[k2]));
			a[1] = x2[k3].xor(x2[k2]);
			a[2] = x2[k3].xor(x1[k4]).xor(this.one.getClone());

			// calculate b
			b[1] = x1[k5].xor(x1[k6]);
			b[0] = y2[k1].xor(x1[k5]).xor(y1[k5]).xor(
					this.field.mul(b[1], x2[k1])).xor(
					this.field.mul(x1[k5].xor(x2[k2]), x1[k6]));
			b[2] = x2[k1].xor(x1[k6]).xor(this.one.getClone());
			b[4] = x2[k2].xor(x2[k1]);

			f = this.field.extendMul(f, this.mulAB(a, b));
		}
		// extract current point xp,yp
		final FieldElement xp4 = this.field.squaring(this.field
				.squaring(x1[this.m - 3].xor(this.one.getClone())));
		final FieldElement yp4 = this.field.squaring(this.field
				.squaring(y1[this.m - 3].xor(x1[this.m - 2])));
		final FieldElement xp8 = this.field.squaring(xp4);

		// perform the final doublings / addition
		a[0] = y2[0].xor(yp4).xor(this.field.mul(xp4, x2[0])).xor(
				this.field
						.mul(x2[0].xor(this.one.getClone()).xor(xp8).xor(xp4),
								x2[1]));
		a[1] = x2[1].xor(xp4);
		a[2] = xp8.xor(xp4);
		a[3] = this.one.getClone();
		a[4] = x2[1].xor(x2[0]);
		a[6] = this.one.getClone();

		f = this.field.extendMul(this.field.extendSquaring(this.field
				.extendSquaring(f)), a);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.print("pairing main loop" + elapsedTime + ",");

		// perform the final exponentiation
		t0 = System.nanoTime();
		f = this.finalExpo(f);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.print("final exp " + elapsedTime + ",");

		pairingReturn = f;
		return f;
	}

	private FieldElement[] lamda1(final ECPoint P, final ECPoint Q) {
		// x,y for P=(x1,y1),Q=(x2,y2)
		final FieldElement[] x1 = new FieldElement[this.m];
		final FieldElement[] y1 = new FieldElement[this.m];
		final FieldElement[] x2 = new FieldElement[this.m];
		final FieldElement[] y2 = new FieldElement[this.m];

		// a for alpha on Algorithm 4
		final FieldElement[] a = { this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.one.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(), };
		// b for beta on Algorithm 4
		final FieldElement[] b = { this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.one.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(), };
		// f is the output
		FieldElement[] f = { this.one.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone(), };

		int k1, k2, k3, k4, k5, k6;

		x1[0] = P.getAffineX();
		y1[0] = P.getAffineY();
		x2[0] = Q.getAffineX();
		y2[0] = Q.getAffineY();

		// Precompute powers of P and Q
		// xi[j]=x^2^j,yi[j]=y^2^j <j
		for (int j = 1; j < this.m; j++) {
			x1[j] = this.field.squaring(x1[j - 1]);
			y1[j] = this.field.squaring(y1[j - 1]);
			x2[j] = this.field.squaring(x2[j - 1]);
			y2[j] = this.field.squaring(y2[j - 1]);
		}

		long t0, t1, elapsedTime;
		t0 = System.nanoTime();
		for (int i = 0; i <= (this.m - 3) / 2; i++) {
			// all ki = in the next 2 line to be considered modulo m
			k1 = ((3 * this.m - 9 - 6 * i) / 2) % this.m;
			k2 = (k1 + 1) % this.m;
			k3 = (k2 + 1) % this.m;
			k4 = ((3 * this.m - 3 + 6 * i) / 2) % this.m;
			k5 = (k4 + 1) % this.m;
			k6 = (k5 + 1) % this.m;

			// calculate a
			a[4] = x1[k4].xor(x1[k5]);
			a[0] = y2[k2].xor(y1[k4]).xor(this.field.mul(a[4], x2[k3])).xor(
					this.field.mul(x1[k4].xor(x2[k3]).xor(this.one.getClone()),
							x2[k2])).xor(this.one.getClone());
			a[1] = x2[k3].xor(x2[k2]);
			a[2] = x2[k3].xor(x1[k4]).xor(this.one.getClone());

			// calculate b
			b[1] = x1[k5].xor(x1[k6]);
			b[0] = y2[k1].xor(x1[k5]).xor(y1[k5]).xor(
					this.field.mul(b[1], x2[k1])).xor(
					this.field.mul(x1[k5].xor(x2[k2]), x1[k6])).xor(
					this.one.getClone());
			b[2] = x2[k1].xor(x1[k6]).xor(this.one.getClone());
			b[4] = x2[k2].xor(x2[k1]);

			f = this.field.extendMul(f, this.mulAB(a, b));
		}
		// extract current point xp,yp
		final FieldElement xp4 = this.field.squaring(this.field
				.squaring(x1[this.m - 3].xor(this.one.getClone())));
		final FieldElement yp4 = this.field.squaring(this.field
				.squaring(y1[this.m - 3].xor(x1[this.m - 2])));
		final FieldElement xp8 = this.field.squaring(xp4);

		// perform the final doublings / addition
		a[0] = y2[0].xor(yp4).xor(this.field.mul(xp4, x2[0])).xor(
				this.field
						.mul(x2[0].xor(this.one.getClone()).xor(xp8).xor(xp4),
								x2[1]));
		a[1] = x2[1].xor(xp4);
		a[2] = xp8.xor(xp4);
		a[3] = this.one.getClone();
		a[4] = x2[1].xor(x2[0]);
		a[6] = this.one.getClone();

		f = this.field.extendMul(this.field.extendSquaring(this.field
				.extendSquaring(f)), a);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.print(elapsedTime + ",");

		// perform the final exponentiation
		t0 = System.nanoTime();
		f = this.finalExpo(f);
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.print(elapsedTime + ",");

		pairingReturn = f;
		return f;
	}

	/**
	 * @param a
	 * @param b
	 * @return extend mul but save 0 multiplication for pairing
	 */
	public FieldElement[] mulAB(final FieldElement[] a, final FieldElement[] b) {
		final FieldElement mul0 = this.field.mul(a[0], b[0]);
		final FieldElement mul1 = this.field.mul(a[1], b[1]);
		final FieldElement mul2 = this.field.mul(a[2], b[2]);
		final FieldElement mul4 = this.field.mul(a[4], b[4]);

		final FieldElement kr01 = this.field
				.mul(a[0].xor(a[1]), b[0].xor(b[1]));
		final FieldElement kr02 = this.field
				.mul(a[0].xor(a[2]), b[0].xor(b[2]));
		final FieldElement kr04 = this.field
				.mul(a[0].xor(a[4]), b[0].xor(b[4]));
		final FieldElement kr12 = this.field
				.mul(a[1].xor(a[2]), b[1].xor(b[2]));
		final FieldElement kr14 = this.field
				.mul(a[1].xor(a[4]), b[1].xor(b[4]));
		final FieldElement kr24 = this.field
				.mul(a[2].xor(a[4]), b[2].xor(b[4]));

		final FieldElement[] tmp = new FieldElement[12];
		tmp[0] = kr24.xor(mul0).xor(mul2);
		tmp[1] = kr01.xor(mul0).xor(mul1).xor(mul4);
		tmp[2] = kr02.xor(kr24).xor(mul0).xor(mul1).xor(mul4);
		tmp[3] = kr12.xor(kr24).xor(mul1).xor(mul4).xor(this.one.getClone());
		tmp[4] = kr04.xor(mul0).xor(mul2).xor(mul4);
		tmp[5] = kr14.xor(kr24).xor(mul1).xor(mul2).xor(this.one.getClone());
		tmp[6] = a[0].xor(b[0]).xor(this.one.getClone());
		tmp[7] = a[1].xor(b[1]);
		tmp[8] = a[2].xor(b[2]);
		tmp[9] = this.zero.getClone().getClone();
		tmp[10] = a[4].xor(b[4]);
		tmp[11] = this.zero.getClone();
		return tmp;
	}

	/**
	 * calc Pairing in this method using the algorithm on Efficient Pairing
	 * Computation on Supersingular Abelian Varieties Paulo S.L.M.Barreto,Steven
	 * Galbraith,Colm O hEigertaigh P.24 Algorithm 4 The genul 2 etaT pairing
	 * when m=103
	 * 
	 * @param P
	 *            Divisor as ECpoint for input.
	 * @param Q
	 *            Divisor as ECpoint for input and it will distortion for
	 *            pairing.
	 * @return Hyperelliptic pairing response
	 */
	public FieldElement[] pairing(final ECPoint P, final ECPoint Q) {
		if (this.lamda == 1) {
			return this.lamda1(P, Q);
		} else {
			return this.lamda0(P, Q);
		}

	}

	/**
	 * @param out
	 *            a print method output out
	 */
	public void PairingOut(final BigInteger[] out) {
		System.out.println(out[0]);
		System.out.println(out[1]);
		System.out.println(out[2]);
		System.out.println(out[3]);
		System.out.println(out[4]);
		System.out.println(out[5]);
		System.out.println(out[6]);
		System.out.println(out[7]);
		System.out.println(out[8]);
		System.out.println(out[9]);
		System.out.println(out[10]);
		System.out.println(out[11]);
	}

	public void print() {
		// TODO Auto-generated method stub
		System.out.println(pairingReturn[0].toBigInteger().toString());
		System.out.println(pairingReturn[1].toBigInteger().toString());
		System.out.println(pairingReturn[2].toBigInteger().toString());
		System.out.println(pairingReturn[3].toBigInteger().toString());
		System.out.println(pairingReturn[4].toBigInteger().toString());
		System.out.println(pairingReturn[5].toBigInteger().toString());
		System.out.println(pairingReturn[6].toBigInteger().toString());
		System.out.println(pairingReturn[7].toBigInteger().toString());
		System.out.println(pairingReturn[8].toBigInteger().toString());
		System.out.println(pairingReturn[9].toBigInteger().toString());
		System.out.println(pairingReturn[10].toBigInteger().toString());
		System.out.println(pairingReturn[11].toBigInteger().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tyouiifan.cipher.risk.tsukuba.ac.jp.Pairing#setup(tyouiifan.cipher.risk
	 * .tsukuba.ac.jp.GF2m4, int)
	 */
	@Override
	public void setup(ExtendField field, int b) {
		this.field = (GF2m12) field;
		this.m = this.field.getM();
		if ((this.m % 24 == 1) | (this.m % 24 == 7) | (this.m % 24 == 17)
				| (this.m % 24 == 23)) {
			if (b == 1) {
				this.p = 1;
			} else {
				this.p = 0;
			}
		} else {
			if (b == 1) {
				this.p = 0;
			} else {
				this.p = 1;
			}
		}
		if (this.m % 4 == 1) {
			this.lamda = 1;
		} else {
			this.lamda = 0;
		}
		this.zero = this.field.modulus.getZERO();
		this.one = this.field.modulus.getONE();
	}
}