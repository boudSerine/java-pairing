package pairing.cipher.risk.tsukuba.ac.jp;

/**
 * An Elliptic Curve Cryptography Library for Pairing Crypto<br>
 * <br>
 * This is a miller algorithm class for Tate-pairing.<br>
 * We calcurate Tate pairing e(P,Q) whith this class.<br>
 * <br>
 * 
 * @author TyouIifan at cipher.risk.tsukuba.ac.jp
 * @version 0.1
 * @since 0.1
 */
public class EtaTPairing implements Pairing {
	private int e;

	FieldElement[] f;

	private GF2m4 field;

	FieldElement[] g = new FieldElement[4];

	private int lambda;

	private int m;

	private FieldElement one;

	private int pow;

	private FieldElement root;

	/**
	 * 
	 */
	public final int EmbedingDegree = 4;

	long t0, t1, elapsedTime;

	private FieldElement[] tmp = new FieldElement[4];

	// final BigInteger r = BigInteger.valueOf(2).pow(241).subtract(
	// BigInteger.valueOf(2).pow(121)).add(BigInteger.ONE);
	//	
	// final BigInteger fExp = BigInteger.valueOf(2).pow(241 *
	// 4).subtract(BigInteger.ONE).divide(r);

	FieldElement u;

	private int v1;

	private int v2;

	private FieldElement zero;

	/**
	 * @param field
	 * @param b
	 * 
	 */
	public EtaTPairing(final ExtendField field, final int b) {
		this.field = (GF2m4) field;
		this.m = this.field.getM();
		final int k = this.field.getK();
		zero = this.field.getZERO();
		one = this.field.getONE();

		if (k % 2 == 0) {
			if (1 + 2 * k - this.m < 0) {
				this.root = this.field.mul(zero.getClone().setBit(
						(1 + 4 * k - this.m) >>> 1).setBit(
						(this.m + 2 * k + 1) >>> 1).setBit((this.m + 1) >>> 1),
						zero.getClone().setBit(k >>> 1).setBit(0));
			} else {
				this.root = this.field.mul(zero.getClone().setBit(
						(1 + 2 * k - this.m)).setBit((this.m + 1) >>> 1), zero
						.getClone().setBit(k >>> 1).setBit(0));
			}
		} else {
			this.root = zero.getClone();
			this.root.toSetBit((this.m + 1) >>> 1);
			this.root.toSetBit((k + 1) >>> 1);
		}

		if ((this.m % 8 == 1) | (this.m % 8 == 7)) {
			if (b == 0) {
				this.pow = 1;
			} else {
				this.pow = 0;
			}
		} else if ((this.m % 8 == 3) | (this.m % 8 == 5)) {
			if (b == 1) {
				this.pow = 1;
			} else {
				this.pow = 0;
			}
		}

		if (b == 1) {
			if ((this.m % 8 == 1) | (this.m % 8 == 7)) {
				this.e = 1;
			} else {
				this.e = 0;
			}
		} else {
			if ((this.m % 8 == 3) | (this.m % 8 == 5)) {
				this.e = 1;
			} else {
				this.e = 0;
			}
		}

		this.v1 = ((this.m + 1) >>> 1) % 2;
		if ((this.m % 8 == 5) | (this.m % 8 == 7)) {
			this.v2 = 1;
		} else {
			this.v2 = 0;
		}

		this.lambda = v1 ^ 1;

		//

	}

	private FieldElement[] finalExpo(final FieldElement[] u) {
		FieldElement[] v = null;
		FieldElement[] w = null;
		if (this.pow == 0) {
			v = this.field.U22m1(u);
			w = this.field.U2m1(v);
			for (int i = (this.m - 1) >>> 1; i >= 0; i--) {
				v = this.field.extendSquaring(v);
			}
		} else {

		}
		return this.field.extendMul(v, w);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tyouiifan.cipher.risk.tsukuba.ac.jp.Pairing#hyperMul(tyouiifan.cipher
	 * .risk.tsukuba.ac.jp.ECPoint)
	 */
	@Override
	public ECPoint hyperMul(ECPoint P) {
		// TODO Auto-generated method stub
		System.out
				.println("error to use hyper muliplication in elliptic pairing");
		return null;
	}

	/**
	 * @param a
	 * @param b
	 * @return extend mul but save 0 multiplication for pairing
	 */
	public FieldElement[] mul(final FieldElement[] a, final FieldElement[] b) {
		final FieldElement[] returnArray = new FieldElement[4];
		// this method can't use for k!=4.
		this.tmp[0] = this.field.mul(a[0], b[0]);
		this.tmp[1] = this.field.mul(a[1], b[1]);
		final FieldElement A01 = a[0].xor(a[1]);
		final FieldElement B01 = b[0].xor(b[1]);
		final FieldElement a01 = this.field.mul(A01, B01);
		final FieldElement a13 = this.field.mul(a[3], b[1]);

		returnArray[0] = a[3].xor(this.tmp[0]).xor(this.tmp[1]);
		returnArray[1] = a[2].xor(a[3]).xor(a01).xor(this.tmp[0]);
		returnArray[2] = a[0].xor(a[2]).xor(this.field.mul(a[2], b[0]))
				.xor(a13);
		returnArray[3] = a[1].xor(a[3]).xor(this.field.mul(a[2], b[1]))
				.xor(a13).xor(this.field.mul(a[3], b[0]));
		return returnArray;
	}

	/**
	 * In this algorithm we calc Tate-pairing.
	 * 
	 * @param P
	 *            ECpoint for input.
	 * @param Q
	 *            ECpoint for input and it will distortion this point.
	 * @return Tate-pairing anser
	 */
	public FieldElement[] pairing(final ECPoint P, final ECPoint Q) {
		FieldElement xp = P.getAffineX();
		FieldElement yp = P.getAffineY();
		FieldElement xq = Q.getAffineX();
		FieldElement yq = Q.getAffineY();

		g[0] = this.zero.getClone();
		g[1] = this.zero.getClone();
		g[2] = this.one.getClone();
		g[3] = this.zero.getClone();
		f = g.clone();

		u = xp.coefXor(0, this.lambda);
		f[0] = yq.xor(this.field.mul(u, xp.xor(xq).coefXor(0, 1))).xor(yp)
				.coefXor(0, this.e);
		f[1] = xq.xor(u);

		t0 = System.nanoTime();
		// if (v1==1) {
		for (int i = (this.m - 1) >>> 1; i >= 0; i--) {
			u = xp.coefXor(0, 1);

			xp = this.field.squareRoot(xp, this.root);
			yp = this.field.squareRoot(yp, this.root);

			g[0] = this.field.mul(u, xp.xor(xq).coefXor(0, 1)).xor(yp).xor(yq)
					.coefXor(0, this.v2);
			g[1] = u.xor(xq);

			// f = field.extendMul(f, g);
			f = this.mul(f, g);

			xq = this.field.squaring(xq);
			yq = this.field.squaring(yq);
		}
		// } else {
		// for (int i = (this.m - 1) >>>1; i >= 0; i--) {
		// u = xp;
		//
		// xp = this.field.squareRoot(xp, this.root);
		// yp = this.field.squareRoot(yp, this.root);
		//
		// g[0] = this.field.mul(u, xp.xor(xq)).xor(yp).xor(yq).xor(xp)
		// .coefXor(0, this.v2);
		// g[1] = u.xor(xq);
		//
		// // f = field.extendMul(f, g);
		// f = this.mul(f, g);
		//
		// xq = this.field.squaring(xq);
		// yq = this.field.squaring(yq);
		// }
		// }

		// t1 = System.nanoTime();
		// elapsedTime = t1 - t0;
		// System.out.print(elapsedTime + ", ");
		//
		// t0 = System.nanoTime();
		f = this.finalExpo(f);
		// t1 = System.nanoTime();
		// elapsedTime = t1 - t0;
		// System.out.print(elapsedTime + ", ");

		return f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairing.cipher.risk.tsukuba.ac.jp.Pairing#print()
	 */
	@Override
	public void print() {
		// TODO Auto-generated method stub
		System.out.println(f[0].toBigInteger().toString());
		System.out.println(f[1].toBigInteger().toString());
		System.out.println(f[2].toBigInteger().toString());
		System.out.println(f[3].toBigInteger().toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tyouiifan.cipher.risk.tsukuba.ac.jp.Pairing#setup(tyouiifan.cipher.risk
	 * .tsukuba.ac.jp.extendFiled, int)
	 */
	@Override
	public void setup(ExtendField field, int b) {
		this.field = (GF2m4) field;
		this.m = this.field.getM();
		final int k = this.field.getK();
		zero = this.field.getZERO();
		one = this.field.getONE();

		if (k % 2 == 0) {
			if (1 + 2 * k - this.m < 0) {
				this.root = this.field.mul(zero.getClone().setBit(
						(1 + 4 * k - this.m) >>> 1).setBit(
						(this.m + 2 * k + 1) >>> 1).setBit((this.m + 1) >>> 1),
						zero.getClone().setBit(k >>> 1).setBit(0));
			} else {
				this.root = this.field.mul(zero.getClone().setBit(
						(1 + 2 * k - this.m)).setBit((this.m + 1) >>> 1), zero
						.getClone().setBit(k >>> 1).setBit(0));
			}
		} else {
			this.root = zero.getClone();
			this.root.toSetBit((this.m + 1) >>> 1);
			this.root.toSetBit((k + 1) >>> 1);
		}

		if ((this.m % 8 == 1) | (this.m % 8 == 7)) {
			if (b == 0) {
				this.pow = 1;
			} else {
				this.pow = 0;
			}
		} else if ((this.m % 8 == 3) | (this.m % 8 == 5)) {
			if (b == 1) {
				this.pow = 1;
			} else {
				this.pow = 0;
			}
		}

		if (b == 1) {
			if ((this.m % 8 == 1) | (this.m % 8 == 7)) {
				this.e = 1;
			} else {
				this.e = 0;
			}
		} else {
			if ((this.m % 8 == 3) | (this.m % 8 == 5)) {
				this.e = 1;
			} else {
				this.e = 0;
			}
		}

		this.v1 = ((this.m + 1) >>> 1) % 2;
		if ((this.m % 8 == 5) | (this.m % 8 == 7)) {
			this.v2 = 1;
		} else {
			this.v2 = 0;
		}

		this.lambda = v1 ^ 1;

		//

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tyouiifan.cipher.risk.tsukuba.ac.jp.Pairing#setup(tyouiifan.cipher.risk
	 * .tsukuba.ac.jp.GF2m4, int)
	 */
	public void setup(GF2m4 field, int b) {
		// TODO 自動生成されたメソッド・スタブ
		this.field = field;
		this.m = field.getM();
		final int k = field.getK();
		zero = field.getZERO();
		one = field.getONE();

		if (k % 2 == 0) {
			if (1 + 2 * k - this.m < 0) {
				this.root = field.mul(zero.getClone().setBit(
						(1 + 4 * k - this.m) >>> 1).setBit(
						(this.m + 2 * k + 1) >>> 1).setBit((this.m + 1) >>> 1),
						zero.getClone().setBit(k >>> 1).setBit(0));
			} else {
				this.root = field.mul(zero.getClone().setBit(
						(1 + 2 * k - this.m)).setBit((this.m + 1) >>> 1), zero
						.getClone().setBit(k >>> 1).setBit(0));
			}
		} else {
			this.root = zero.getClone();
			this.root.toSetBit((this.m + 1) >>> 1);
			this.root.toSetBit((k + 1) >>> 1);
		}

		if ((this.m % 8 == 1) | (this.m % 8 == 7)) {
			if (b == 0) {
				this.pow = 1;
			} else {
				this.pow = 0;
			}
		} else if ((this.m % 8 == 3) | (this.m % 8 == 5)) {
			if (b == 1) {
				this.pow = 1;
			} else {
				this.pow = 0;
			}
		}

		if (b == 1) {
			if ((this.m % 8 == 1) | (this.m % 8 == 7)) {
				this.e = 1;
			} else {
				this.e = 0;
			}
		} else {
			if ((this.m % 8 == 3) | (this.m % 8 == 5)) {
				this.e = 1;
			} else {
				this.e = 0;
			}
		}

		this.v1 = ((this.m + 1) >>> 1) % 2;
		if ((this.m % 8 == 5) | (this.m % 8 == 7)) {
			this.v2 = 1;
		} else {
			this.v2 = 0;
		}

		this.lambda = v1 ^ 1;

		//

	}
}