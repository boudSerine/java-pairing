package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;

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
public class TatePairing implements Pairing {
	private EllipticCurveGF2m curve;

	private GF2m4 field;

	private BigInteger finalPow;

	public final int EmbedingDegree = 4;
	private BigInteger invPow;

	private FieldElement one;

	/**
	 * order of Elliptic curve on extention Feild.
	 */
	private BigInteger r;

	private FieldElement xp;

	private FieldElement xq;

	private FieldElement yp;

	private FieldElement yq;

	private FieldElement zero;

	private FieldElement[] f;

	/**
	 * @param r
	 * @param field
	 * @param curve
	 * @param finalPow
	 * @param invPow
	 * 
	 */
	public TatePairing(final BigInteger r, final GF2m4 field,
			final EllipticCurveGF2m curve, final BigInteger finalPow,
			final BigInteger invPow) {
		this.r = r;
		this.field = field;
		this.curve = curve;
		this.finalPow = finalPow;
		this.invPow = invPow;
		this.zero = field.zero.getClone();
		this.one = field.one.getClone();
	}

	// the value to set an elictip curve y^3=x^2+Ax+B

	// the input point on the elictip curve
	// ECPoint Q = Elictip_Adder.ECpointAdder(A, B, P1, P2); //help of
	// PointAdder

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
		return EmbedingDegree * field.getM();
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
		this.xp = P.getAffineX();
		this.yp = P.getAffineY();
		this.xq = Q.getAffineX();
		this.yq = Q.getAffineY();

		f = new FieldElement[4];

		// P is from E(Fq)[r], Q is from E(Fqk)
		// f=1
		// V=P
		f[0] = one.getClone();
		f[1] = zero.getClone();
		f[2] = zero.getClone();
		f[3] = zero.getClone();
		ECPoint V = P;
		final int t = this.r.bitLength() - 1;
		// r is shwon (10010101...1)2=(t-1 to 0) in binnary. t-1 bit Integer.
		// for (t-1 down to 0)
		/**
		 * test time for a loop for Miller
		 */
		long t0, t1, elapsedTime;
		t0 = System.nanoTime();
		for (int i = t; i >= 0; i--) {
			// f = (f^2)*l(V,V)(Q) //l(P,Q) is a line cross to P and Q
			// l(v,v) is the tangent line to Eliptic curve y^2+y=x^3+Ax+B
			// l(v,v)(Q) mean input the point Q (X,Y) to the tangent line
			// y=ax+b
			f = this.field.extendMul(this.field.extendSquaring(f), this
					.tangentLine(V));
			// V = 2V
			V = this.curve.doublePoint(V);
			// if (ri==1){
			if (this.r.testBit(i)) {
				// f = f*l(V,P)(Q)
				f = this.field.extendMul(f, this.pointLine(V));
				// V = V + Plong t0, t1, elapsedTime;
				V = this.curve.add(V, P);
				// }
			}
		}
		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.print(elapsedTime + ",");
		// System.out.println(field.count);
		// field.count=0;
		t0 = System.nanoTime();
		// f = f^(((q^k)-1)/r)

		f = this.field.extendMul(this.field.extendPow(f, this.finalPow),
				this.field.extendInvPow(f, this.invPow));

		t1 = System.nanoTime();
		elapsedTime = t1 - t0;
		System.out.print(elapsedTime + ",");
		// System.out.println(field.count);
		// field.count=0;

		return f;
		// return f from Fqk
	}

	/**
	 * a method to calc Lv,p(dis(Q)).
	 * 
	 * @param V
	 *            input V for v,p
	 * @return lv,p(Q)
	 */
	public FieldElement[] pointLine(final ECPoint V) {
		// ax+by+c=0 for V,P.
		// input Q(xq,yq) for above polynomial.
		FieldElement a;
		FieldElement b;
		FieldElement c;
		final FieldElement vx = V.getAffineX();
		final FieldElement vy = V.getAffineY();
		// (px+vx)y+(py+vy)x+c=0 use for ax+by+c=0

		// a = py+vy
		a = this.yp.xor(vy);
		// b = px+vx
		b = vx.xor(this.xp);
		// c = (a*px+b*py) = (a*vx+b*vy)
		c = this.field.mul(a, this.xp).xor(this.field.mul(b, this.yp));

		FieldElement[] tmp = { this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone() };
		tmp[0] = this.field.mul(a, this.xq.xor(this.one)).xor(
				this.field.mul(b, this.yq)).xor(c);
		tmp[1] = a.xor(this.field.mul(b, this.xq));
		tmp[2] = b;

		return tmp;
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
		this.r = r;
		this.field = (GF2m4) field;
		this.curve = curve;
		this.finalPow = finalPow;
		this.invPow = invPow;
		this.zero = this.field.zero.getClone();
		this.one = this.field.one.getClone();

	}

	/**
	 * a method to calc Lv,v(dis(Q))
	 * 
	 * @param V
	 *            input V for v,v
	 * @return lv,v(Q)
	 */
	public FieldElement[] tangentLine(final ECPoint V) {
		// ax+by+c=0 for V.
		// input Q(xq,yq) for above polynomial.
		FieldElement a;
		FieldElement c;
		final FieldElement vx = V.getAffineX();
		final FieldElement vy = V.getAffineY();
		// slope:y'=(3*vx^2+A)
		// (3*vx^2+A)x+y+c=0

		// a = (3*vx^2+A) = (vx^2+A)
		a = this.field.squaring(vx).coefXor(0, 1);
		// b = 1
		// c = -(a*vx+b*vy)
		c = this.field.mul(a, vx).xor(vy);

		FieldElement[] tmp = { this.zero.getClone(), this.zero.getClone(),
				this.zero.getClone(), this.zero.getClone() };

		tmp[0] = this.field.mul(a, this.xq.coefXor(0, 1)).xor(this.yq).xor(c);
		tmp[1] = a.xor(this.xq);
		tmp[2] = this.one;
		return tmp;
	}
}