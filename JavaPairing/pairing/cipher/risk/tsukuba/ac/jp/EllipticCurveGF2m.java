package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;

/**
 * An Elliptic Curve Cryptography Library for Pairing Cryptography<br>
 * <br>
 * This is a class to compute elliptic curve point addition and multiplication.<br>
 * We can compute in GF2^m over module with irreducible polynomial.<br>
 * And also can do same compute in GF2^m's k times extension field GF2^mk.<br>
 * <br>
 * We use ECpoint  java.security.spec.ECPoint to define point in
 * Elliptic Curve.<br>
 * To computeulate addition and multipulication we use Finite Field class GF2m.<br>
 * <br>
 *
 * @author TyouIifan at cipher.risk.tsukuba.ac.jp
 * @version 0.1
 * @since 0.1
 */
public class EllipticCurveGF2m {
	/**
	 * a value to define y^2+y=x^3+ax+b
	 */
	private final int a;

	private final int b;

	/**
	 * define field to compute .
	 */
	private GF2m4 field;

	FieldElement output_x, output_y, lambda;

	// Field256[] tmp;

	private FieldElement[][] H;

	/**
	 * @param field
	 * @param a
	 * @param b
	 */
	public EllipticCurveGF2m(final ExtendField field, final int a, final int b) {
		this.a = a;
		this.b = b;
		this.field = (GF2m4) field;
	}

	/**
	 * @param pointA
	 * @param pointB
	 * @return new ECpint for addition of two point.
	 */
	public ECPoint add(final ECPoint pointA, final ECPoint pointB) {
		final FieldElement ax = pointA.getAffineX();
		final FieldElement ay = pointA.getAffineY();
		final FieldElement bx = pointB.getAffineX();
		final FieldElement by = pointB.getAffineY();

		if (!ax.equals(bx)) {
			// ax!=bx
			this.lambda = this.field.div(by.xor(ay), bx.xor(ax));
			// lamda=(by+ay)/(bx+ax)
			this.output_x = this.field.squaring(this.lambda).xor(ax.xor(bx));
			// x3=lamuda^2+ax+bx
			this.output_y = this.field.mul(ax.xor(this.output_x), this.lambda)
					.xor(ay.xor(this.field.modulus.getONE()));
			// y3=lamda(ax+output_x)+ay+1
		} else if (!ay.equals(by)) {
			// x1=x2,y1!=y2 -> P1+P2=infinite. return 0;
			this.output_x = this.field.modulus.getZERO();
			this.output_y = this.field.modulus.getZERO();
		} else if (!ay.equals(this.field.modulus.getZERO())) {
			// P1=P2,y1!=0
			this.lambda = this.field.squaring(ax).xor(
					this.field.modulus.getONE());
			// lamda=x1^2+A
			this.output_x = this.field.squaring(this.lambda);
			// x3=lamda^2
			this.output_y = this.field.mul(ax.xor(this.output_x), this.lambda)
					.xor(ay).xor(this.field.modulus.getONE());
			// y3=lamda(ax+output_x)+ay+1
		} else {
			// P1=P2,y1=0 -> P1+P2=infinite. return 0;
			this.output_x = this.field.modulus.getZERO();
			this.output_y = this.field.modulus.getZERO();
		}
		return new ECPoint(this.output_x, this.output_y);
	}

	/**
	 * @param pointA
	 * @return Double point of input point
	 */
	public ECPoint doublePoint(final ECPoint pointA) {
		final FieldElement ax = pointA.getAffineX();
		final FieldElement ay = pointA.getAffineY();

		// P1=P2,y1!=0
		this.lambda = this.field.squaring(ax).coefXor(0, a);
		// lamda=x1^2+A
		this.output_x = this.field.squaring(this.lambda);
		// x3=lamda^2
		this.output_y = this.field.mul(ax.xor(this.output_x), this.lambda).xor(
				ay.xor(this.field.modulus.getONE()));
		// y3=lamda(ax+output_x)+ay+1
		return new ECPoint(this.output_x, this.output_y);
	}

	/**
	 * @return this a
	 */
	public int getA() {
		return a;
	}

	/**
	 * @return this b
	 */
	public int getB() {
		return b;
	}

	public GF2m4 getField() {
		return field;
	}

	public FieldElement[][] getH() {
		return H;
	}

	/*
	 * a value to define y^2+y=x^3+ax+b
	 */

	public FieldElement getLambda() {
		return lambda;
	}

	/**
	 * @return get new random point
	 */
	public ECPoint getNewPoint() {
		FieldElement x = ECPoint.newX(this);
		return new ECPoint(x, getY(x));
	}

	/**
	 * @param x
	 * @return get new point (x,y)
	 */
	public ECPoint getNewPoint(FieldElement x) {
		return new ECPoint(x, getY(x));
	}

	public FieldElement getOutput_x() {
		return output_x;
	}

	public FieldElement getOutput_y() {
		return output_y;
	}

	public FieldElement getY(final FieldElement x) {
		FieldElement xClone = field.mul(field.squaring(x), x);
		if (a == 1) {
			field.toAdd(xClone, x);
		}
		if (b == 1) {
			xClone.toXorBit(0);
		}
		FieldElement s = xClone.getClone();
		FieldElement t = xClone.getClone();

		for (int i = 0; i < (field.getM() - 1) / 2; i++) {
			t = field.squaring(field.squaring(t));
			field.toAdd(s, t);
		}
		return s;
	}

	/**
	 * @param ID
	 * @return get new point which x from ID
	 */
	public ECPoint hashPoint(String ID) {
		FieldElement x = ECPoint.hashID(ID, this);
		return new ECPoint(x, getY(x));
	}

	/**
	 * This is a method to multiplicate point on elliptic curve.
	 *
	 * @param mul
	 *            times to multiplicate.
	 * @param p1
	 *            a Elliptic Point on the defined curve.
	 * @return a Elliptic Point P*mul.
	 */
	public ECPoint mul(final BigInteger mul, final ECPoint p1) {
		/**
		 * nomal mul
		 */
		ECPoint mulP = new ECPoint(p1.x, p1.y);
		ECPoint tmp = new ECPoint(p1.x, p1.y);
		int i = 241;
		for (int j = 0; j < mul.bitLength(); j++) {
			if ((j == 0) && mul.testBit(0)) {
				mulP = p1;
				i = j;
			} else if ((j < i) && mul.testBit(j)) {
				mulP = tmp;
				i = j;
			} else if (mul.testBit(j)) {
				mulP = this.add(tmp, mulP);
			}
			tmp = this.doublePoint(tmp);
		}
		return mulP;
		/**
		 * montgomery EC mul ----error ;(
		 */
		// ECPoint mulP;
		// final Field256 x = P.getAffineX();
		// final Field256 y = P.getAffineY();
		// Field256 x1 = x;
		// Field256 z1 = one;
		// Field256 x2 = field.pow(x,
		// Field256.valueOf(4)).xor(one);
		// Field256 z2 = field.mul(x,x);
		// Field256 x3;
		// Field256 y3;
		// Field256 t;
		// for(int i = mul.bitLength()-2 ; i>=0;i--){
		// if(mul.testBit(i)){
		// t=z1;
		// z1=field.pow(field.add(field.mul(x1, z2), field.mul(x2, z1)),
		// Field256.valueOf(2));
		// x1=field.add(field.mul(x, z1), field.mul(field.mul(x1, x2),
		// field.mul(z2, t)));
		// t=x2;
		// x2=field.add(field.pow(x2,
		// Field256.valueOf(4)),field.pow(z2,Field256.valueOf(4)));
		// z2=field.mul(field.mul(t, t), field.mul(z2, z2));
		// }else{
		// t=z2;
		// z2=field.pow(field.add(field.mul(x1, z2), field.mul(x2, z1)),
		// Field256.valueOf(2));
		// x2=field.add(field.mul(x, z2), field.mul(field.mul(x1, x2),
		// field.mul(z1, t)));
		// t=x1;
		// x1=field.add(field.pow(x1,
		// Field256.valueOf(4)),field.pow(z1,Field256.valueOf(4)));
		// z1=field.mul(field.mul(t, t), field.mul(z1, z1));
		// }
		// }
		// x3=field.div(x1, z1);
		// y3=field.add(x, x3);
		// y3=field.mul(y3, field.add(field.mul(field.add(x1, field.mul(x, z1)),
		// field.add(x2, field.mul(x, z2))), field.mul(field.add(field.mul(x,
		// x), y), field.mul(z1, z2))));
		// y3= field.mul(y3, field.inv(field.mul(field.mul(z1, z2), x)));
		// y3= field.add(y3, y);
		// mulP = new ECPoint(x3,y3);
		// return mulP;
	}

	/**
	 * @param field
	 */
	public void setField(final GF2m4 field) {
		this.field = field;
	}

	public void setH(FieldElement[][] h) {
		H = h;
	}

	public void setLambda(FieldElement lambda) {
		this.lambda = lambda;
	}

	public void setOutput_x(FieldElement output_x) {
		this.output_x = output_x;
	}

	public void setOutput_y(FieldElement output_y) {
		this.output_y = output_y;
	}

	private FieldElement trace(FieldElement x) {
		FieldElement c = field.mul(field.squaring(x), x);
		if (a == 1) {
			field.toAdd(c, x);
		}
		if (b == 1) {
			c.toXorBit(0);
		}
		FieldElement tr = field.add(field.getZERO(), c);
		for (int i = 0; i < field.getM() - 1; i++) {
			c = field.squaring(c);
			field.toAdd(tr, c);
		}
		return tr;
	}

	public boolean traceTest(FieldElement x) {
		FieldElement c = field.mul(field.squaring(x), x);
		if (a == 1) {
			field.toAdd(c, x);
		}
		if (b == 1) {
			c.toXorBit(0);
		}
		FieldElement tr = field.add(field.getZERO(), c);
		for (int i = 0; i < field.getM() - 1; i++) {
			c = field.squaring(c);
			field.toAdd(tr, c);
		}
		return !tr.testBit(0);
	}
}
