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
public class ModifiedTatePairing implements Pairing {
	private FieldElement a;

	private FieldElement b;

	private EllipticCurveGF2m curve;

	private GF2m4 field;

	private int m;
	public final int EmbedingDegree = 4;

	private FieldElement one;

	private int pow;

	private final FieldElement[] tmp = new FieldElement[4];

	private FieldElement x;

	private FieldElement y;

	private FieldElement zero;

	private FieldElement[] f;

	/**
	 * @param field
	 * @param curve
	 * @param b
	 */
	public ModifiedTatePairing(final GF2m4 field,
			final EllipticCurveGF2m curve, final int b) {
		this.field = field;
		this.curve = curve;
		this.m = field.getM();
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
		this.one = field.getONE();
		this.zero = field.getZERO();
	}

	// private FieldElement[] finalExpo(final FieldElement[] u) {
	// FieldElement m0 = field.squaring(u[0]);
	// FieldElement m1 = field.squaring(u[1]);
	// FieldElement m2 = field.squaring(u[2]);
	// FieldElement m3 = field.squaring(u[3]);
	//
	// FieldElement[] T0 = { m0.xor(m1), m1 };
	// FieldElement[] T1 = { m2.xor(m3), m3 };
	// FieldElement[] T2 = { m3, m2 };
	// FieldElement[] T3 = field.exMul(new FieldElement[] { u[0], u[1] },
	// new FieldElement[] { u[2], u[3] });
	// FieldElement[] T4 = field.exAdd(T0, T2);
	// FieldElement[] D = field.exInv(field.exAdd(T3, T4));
	// FieldElement[] T5 = field.exMul(T1, D);
	// FieldElement[] T6 = field.exMul(T4, D);
	// FieldElement[] V0 = field.exAdd(T5, T6);
	// FieldElement[] V1 = T5.clone();
	// FieldElement[] W1 = T5.clone();
	// FieldElement[] W0;
	// if (this.pow == 1) {
	// W0 = T6;
	// } else {
	// W0 = V0;
	// }
	//
	// FieldElement[] V = { V0[0], V0[1], V1[0], V1[1] };
	// FieldElement[] W = { W0[0], W0[1], W1[0], W1[1] };
	// V = field.U2m1(V);
	//
	// for (int i = (this.m - 1) / 2; i >= 0; i--) {
	// V = this.field.extendSquaring(V);
	// }
	// return this.field.extendMul(V, W);
	// }

	public FieldElement[] extendSquaring(final FieldElement[] f) {
		final FieldElement[] tmpArray = new FieldElement[4];
		this.tmp[0] = field.squaring(f[0]);
		this.tmp[1] = field.squaring(f[1]);
		this.tmp[2] = field.squaring(f[2]);
		this.tmp[3] = field.squaring(f[3]);
		tmpArray[0] = this.tmp[0].xor(this.tmp[1]).xor(this.tmp[3]);
		tmpArray[1] = this.tmp[1].xor(this.tmp[2]);
		tmpArray[2] = this.tmp[2].xor(this.tmp[3]);
		tmpArray[3] = this.tmp[3];
		return tmpArray;
	}

	private FieldElement[] finalExpo(final FieldElement[] u) {
		FieldElement[] v = null;
		FieldElement[] w = null;
		if (this.pow == 0) {
			v = this.field.U22m1(u);
			w = this.field.U2m1(v);
			for (int i = (this.m - 1) / 2; i >= 0; i--) {
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
		// TODO Auto-generated method stub
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
	 * In this algorithm we calc Tate-pairing.
	 * 
	 * @param P
	 *            ECpoint for input.
	 * @param Q
	 *            ECpoint for input and it will distortion this point.
	 * @return Tate-pairing anser
	 */
	public FieldElement[] pairing(final ECPoint P, final ECPoint Q) {
		this.a = this.field.squaring(P.getAffineX()).coefXor(0, 1);
		this.b = this.field.squaring(P.getAffineY()).coefXor(0, 1);
		this.x = Q.getAffineX();
		this.y = Q.getAffineY();

		final FieldElement[] g = { this.zero.getClone(), this.zero.getClone(),
				this.one.getClone(), this.zero.getClone() };
		f = g.clone();

		FieldElement u = this.y.coefXor(0, 1).coefXor(0, curve.getB());
		FieldElement v = this.x.coefXor(0, 1);
		FieldElement th = this.field.mul(this.a, v);

		// long t0, t1, elapsedTime;
		// t0 = System.nanoTime();
		for (int i = (this.m - 3) / 2; i >= 0; i--) {
			g[0] = this.b.xor(th).xor(u);
			g[1] = this.a.xor(v).coefXor(0, 1);
			f = this.field.extendMul(this.field.extendSquaring(f), g);

			this.a = this.field.squaring(this.field.squaring(this.a));
			this.b = this.field.squaring(this.field.squaring(this.b));
			v = v.coefXor(0, 1);
			u = u.xor(v);
			th = this.field.mul(this.a, v);
		}

		g[0] = this.b.xor(th).xor(u);
		g[1] = this.a.xor(v).coefXor(0, 1);
		f = this.field.extendMul(this.field.extendSquaring(f), g);

		g[0] = g[0].xor(this.field.squaring(this.a)).xor(v).coefXor(0, 1);
		g[1] = g[1].coefXor(0, 1);

		f = this.field.extendMul(f, g);

		// t1 = System.nanoTime();
		// elapsedTime = t1 - t0;
		// System.out.print(elapsedTime + ", ");
		//
		// t0 = System.nanoTime();
		// f = f^(((q^k)-1)/r)
		f = this.finalExpo(f);
		// t1 = System.nanoTime();
		// elapsedTime = t1 - t0;
		// System.out.print(elapsedTime + ", ");

		return f;
		// return f from Fqk
	}

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
		this.curve = curve;
		this.m = this.field.getM();
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
		this.one = this.field.getONE();
		this.zero = this.field.getZERO();
	}
}