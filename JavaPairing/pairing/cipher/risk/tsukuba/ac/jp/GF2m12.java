package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;

/**
 * An Elliptic Curve Cryptography Library for Pairing Crypto
 * 
 * This is a class to calc finite field GF2^m ,which is shown in polynomial.<br>
 * We can calc GF2^m over module with irreducible polynomial.<br>
 * And also can do same calc in GF2^m's k times extension feild GF2^mk.<br>
 * <br>
 * define GF2^m <br>
 * element of GF2^m is shown in polynomial a(1)x^m-1 + a(2)x^m-2 + ... + a(m-1)x
 * + a(m) <br>
 * set a(1),a(2),a(3),...,a(m-1),a(m) into FieldElement coef_m <br>
 * <br>
 * define GF2^km <br>
 * element of GF2^km is shown in polynomial coef_m(1)T^p + coef_m(2)T^p-1 + ...
 * + coef_m(p-1)T + coef_m(p) <br>
 * set coef_m(1),coef_m(2),...,coef_m(p) into FieldElement[] coef_km <br>
 * 
 * @author TyouIifan at cipher.risk.tsukuba.ac.jp
 * @version 0.1
 * @since 0.1
 */
public class GF2m12 extends ExtendField {
	private final FieldElement[] tmp = new FieldElement[12];

	FieldElement[] tmpSq = new FieldElement[12];

	/**
	 * set irreducible polynomial
	 * 
	 * @param m
	 *            bitLength.
	 * @param ks
	 */
	public GF2m12(final int m, final int[] ks) {
		super(m, ks);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairing.cipher.risk.tsukuba.ac.jp.ExtendField#equals(pairing.cipher.risk
	 * .tsukuba.ac.jp.FieldElement[],
	 * pairing.cipher.risk.tsukuba.ac.jp.FieldElement[])
	 */
	@Override
	public boolean equals(FieldElement[] fields1, FieldElement[] fields2) {
		for (int j = 0; j < 12; j++) {
			for (int i = 0; i <= mInt; i++) {
				if (fields1[j].getCoef(i) != fields2[j].getCoef(i)) {
					return false;
				}
			}
		}
		return true;
	}

	public FieldElement[] exAdd(final FieldElement[] filedArrayA,
			final FieldElement[] fieldArrayB) {
		final FieldElement[] tmpAdd = new FieldElement[6];
		tmpAdd[0] = filedArrayA[0].xor(fieldArrayB[0]);
		tmpAdd[1] = filedArrayA[1].xor(fieldArrayB[1]);
		tmpAdd[2] = filedArrayA[2].xor(fieldArrayB[2]);
		tmpAdd[3] = filedArrayA[3].xor(fieldArrayB[3]);
		tmpAdd[4] = filedArrayA[4].xor(fieldArrayB[4]);
		tmpAdd[5] = filedArrayA[5].xor(fieldArrayB[5]);
		return tmpAdd;
	}

	public FieldElement[] exEuclidDiv(final FieldElement[] a,
			final FieldElement[] b) {
		FieldElement[] r = a.clone();
		FieldElement[] q = { zero.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone() };
		FieldElement[] s = { zero.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone() };
		while (r[4] != zero.getClone()) {
			s[0] = div(r[5], b[5]);
			q[0] = q[0].xor(s[0]);
			r = extendAdd(r, (extendMul(s, b)));
		}
		// a = bq + r
		FieldElement[] ret = { q[0], q[1], q[2], q[3], q[4], q[5], r[0], r[1],
				r[2], r[3], r[4], r[5] };
		return ret;
	}

	public FieldElement[] exEuclidGCD(final FieldElement[] f,
			final FieldElement[] m) {
		FieldElement[] u = { one.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone() };
		FieldElement[] v = { zero.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone() };
		FieldElement[] s = m;
		FieldElement[] g = f;
		FieldElement[] tmp;
		FieldElement[] t = { zero.getClone(), zero.getClone(), zero.getClone(),
				zero.getClone(), zero.getClone(), zero.getClone() };
		FieldElement[] q = new FieldElement[6];
		FieldElement[] r = new FieldElement[6];
		while (s[0] != zero.getClone() & s[1] != zero.getClone()
				& s[2] != zero.getClone() & s[3] != zero.getClone()) {
			tmp = exEuclidDiv(g, s);
			q[0] = tmp[0];
			q[1] = tmp[1];
			q[2] = tmp[2];
			q[3] = tmp[3];
			q[4] = tmp[4];
			q[5] = tmp[5];
			r[0] = tmp[6];
			r[1] = tmp[7];
			r[2] = tmp[8];
			r[3] = tmp[9];
			r[4] = tmp[10];
			r[5] = tmp[11];
			t = extendAdd(u, extendMul(v, q));
			u = v;
			g = s;
			v = t;
			s = r;
		}
		// au + bv = D = gcd(a,b)
		return u;
	}

	public FieldElement[] exMul(final FieldElement[] a, final FieldElement[] b) {
		final FieldElement[] tmpMul = new FieldElement[6];
		final FieldElement mul00 = this.mul(a[0], b[0]);
		final FieldElement mul01 = this.mul(a[0].xor(a[1]), b[0].xor(b[1]));
		final FieldElement mul02 = this.mul(a[0].xor(a[2]), b[0].xor(b[2]));
		final FieldElement mul03 = this.mul(a[0].xor(a[3]), b[0].xor(b[3]));
		final FieldElement mul04 = this.mul(a[0].xor(a[4]), b[0].xor(b[4]));
		final FieldElement mul05 = this.mul(a[0].xor(a[5]), b[0].xor(b[5]));
		final FieldElement mul11 = this.mul(a[1], b[1]);
		final FieldElement mul12 = this.mul(a[1].xor(a[2]), b[1].xor(b[2]));
		final FieldElement mul13 = this.mul(a[1].xor(a[3]), b[1].xor(b[3]));
		final FieldElement mul14 = this.mul(a[1].xor(a[4]), b[1].xor(b[4]));
		final FieldElement mul15 = this.mul(a[1].xor(a[5]), b[1].xor(b[5]));
		final FieldElement mul22 = this.mul(a[2], b[2]);
		final FieldElement mul23 = this.mul(a[2].xor(a[3]), b[2].xor(b[3]));
		final FieldElement mul24 = this.mul(a[2].xor(a[4]), b[2].xor(b[4]));
		final FieldElement mul25 = this.mul(a[2].xor(a[5]), b[2].xor(b[5]));
		final FieldElement mul33 = this.mul(a[3], b[3]);
		final FieldElement mul34 = this.mul(a[3].xor(a[4]), b[3].xor(b[4]));
		final FieldElement mul35 = this.mul(a[3].xor(a[5]), b[3].xor(b[5]));
		final FieldElement mul44 = this.mul(a[4], b[4]);
		final FieldElement mul45 = this.mul(a[4].xor(a[5]), b[4].xor(b[5]));
		final FieldElement mul55 = this.mul(a[5], b[5]);

		tmpMul[0] = mul15.xor(mul24).xor(mul25).xor(mul34).xor(mul35)
				.xor(mul00).xor(mul11).xor(mul33).xor(mul44).xor(mul55);
		tmpMul[1] = mul01.xor(mul25).xor(mul34).xor(mul35).xor(mul45)
				.xor(mul55).xor(mul44).xor(mul22).xor(mul11).xor(mul00);
		tmpMul[2] = mul02.xor(mul15).xor(mul24).xor(mul25).xor(mul34)
				.xor(mul45).xor(mul44).xor(mul22).xor(mul00);
		tmpMul[3] = mul03.xor(mul12).xor(mul15).xor(mul24).xor(mul44)
				.xor(mul00);
		tmpMul[4] = mul04.xor(mul13).xor(mul25).xor(mul34).xor(mul55)
				.xor(mul11).xor(mul00);
		tmpMul[5] = mul05.xor(mul14).xor(mul15).xor(mul23).xor(mul24)
				.xor(mul25).xor(mul34).xor(mul55).xor(mul44).xor(mul33).xor(
						mul22).xor(mul00);
		return tmpMul;
	}

	public FieldElement[] exSquare(final FieldElement[] a) {
		final FieldElement[] tmp2 = new FieldElement[6];
		final FieldElement sq0 = this.squaring(a[0]);
		final FieldElement sq1 = this.squaring(a[1]);
		final FieldElement sq2 = this.squaring(a[2]);
		final FieldElement sq3 = this.squaring(a[3]);
		final FieldElement sq4 = this.squaring(a[4]);
		final FieldElement sq5 = this.squaring(a[5]);

		tmp2[0] = sq0.xor(sq3).xor(sq4);
		tmp2[1] = sq4;
		tmp2[2] = sq1.xor(sq3).xor(sq5);
		tmp2[3] = sq3.xor(sq5);
		tmp2[4] = sq2;
		tmp2[5] = sq3;
		return tmp2;
	}

	/**
	 * This method calc addition for two k-extend field.
	 * 
	 * @param filedArrayA
	 *            k-extend field from polynomials.
	 * @param fieldArrayB
	 *            k-extend field from polynomials.
	 * @return [fieldArrayA]+[fieldArrayB] mod(modulus).
	 */
	public FieldElement[] extendAdd(final FieldElement[] filedArrayA,
			final FieldElement[] fieldArrayB) {
		this.tmp[0] = filedArrayA[0].xor(fieldArrayB[0]);
		this.tmp[1] = filedArrayA[1].xor(fieldArrayB[1]);
		this.tmp[2] = filedArrayA[2].xor(fieldArrayB[2]);
		this.tmp[3] = filedArrayA[3].xor(fieldArrayB[3]);
		this.tmp[4] = filedArrayA[4].xor(fieldArrayB[4]);
		this.tmp[5] = filedArrayA[5].xor(fieldArrayB[5]);
		this.tmp[6] = filedArrayA[6].xor(fieldArrayB[6]);
		this.tmp[7] = filedArrayA[7].xor(fieldArrayB[7]);
		this.tmp[8] = filedArrayA[8].xor(fieldArrayB[8]);
		this.tmp[9] = filedArrayA[9].xor(fieldArrayB[9]);
		this.tmp[10] = filedArrayA[10].xor(fieldArrayB[10]);
		this.tmp[11] = filedArrayA[11].xor(fieldArrayB[11]);
		return this.tmp;
	}

	/**
	 * This is a k-extend field multiplication method in F2(4m).
	 * 
	 * @param fieldArrayA
	 *            k-extend field from polynomials.
	 * @param fieldArrayB
	 *            k-extend field from polynomials.
	 * @return [fieldArrayA]*[fieldArrayB] mod(modulus).
	 */
	@Override
	public FieldElement[] extendMul(final FieldElement[] a,
			final FieldElement[] b) {
		final FieldElement[] a0 = { a[0], a[1], a[2], a[3], a[4], a[5] };
		final FieldElement[] a1 = { a[6], a[7], a[8], a[9], a[10], a[11] };
		final FieldElement[] b0 = { b[0], b[1], b[2], b[3], b[4], b[5] };
		final FieldElement[] b1 = { b[6], b[7], b[8], b[9], b[10], b[11] };
		final FieldElement[] ab0 = this.exMul(a0, b0);
		final FieldElement[] ab1 = this.exMul(a1, b1);
		final FieldElement[] ab01 = this.exAdd(this.exMul(this.exAdd(a0, a1),
				this.exAdd(b0, b1)), this.exAdd(ab0, ab1));

		this.tmp[6] = ab1[0].xor(ab01[0]);
		this.tmp[7] = ab1[1].xor(ab01[1]);
		this.tmp[8] = ab1[2].xor(ab01[2]);
		this.tmp[9] = ab1[3].xor(ab01[3]);
		this.tmp[10] = ab1[4].xor(ab01[4]);
		this.tmp[11] = ab1[5].xor(ab01[5]);

		this.tmp[0] = ab0[0].xor(ab1[1]).xor(ab1[2]).xor(ab1[4]).xor(ab1[5]);
		this.tmp[1] = ab0[1].xor(ab1[2]).xor(ab1[3]).xor(ab1[5]);
		this.tmp[2] = ab0[2].xor(ab1[1]).xor(ab1[2]).xor(ab1[3]).xor(ab1[5]);
		this.tmp[3] = ab0[3].xor(ab1[0]).xor(ab1[1]).xor(ab1[3]).xor(ab1[5]);
		this.tmp[4] = ab0[4].xor(ab1[1]).xor(ab1[2]).xor(ab1[4]);
		this.tmp[5] = ab0[5].xor(ab1[0]).xor(ab1[1]).xor(ab1[3]).xor(ab1[4]);

		return this.tmp;
	}

	/**
	 * This is a k-extend field power method in F2(4m).
	 * 
	 * @param inputField
	 *            k-extend field from polynomials.
	 * @param integer
	 *            FieldElement value to power fieldArray.
	 * @return [fieldArrayA[]]^[pow] mod(modulus).
	 */
	@Override
	public FieldElement[] extendPow(final FieldElement[] inputField,
			final BigInteger integer) {
		FieldElement[] returnArray = { this.one, this.zero, this.zero,
				this.zero, this.zero, this.zero, this.zero, this.zero,
				this.zero, this.zero, this.zero, this.zero, };
		FieldElement[] field = inputField.clone();
		for (int i = 0; i < integer.bitLength(); i++) {
			if (integer.testBit(i)) {
				returnArray = this.extendMul(returnArray, field);
			}
			field = this.extendSquaring(field);
		}
		return returnArray;
	}

	/**
	 * This is a k-extend field power method in F2(4m).
	 * 
	 * @param inputField
	 *            k-extend field from polynomials.
	 * @param integer
	 *            FieldElement value to power fieldArray.
	 * @return [fieldArrayA[]]^[pow] mod(modulus).
	 */
	public FieldElement[] extendPow(final FieldElement[] inputField,
			final FieldElement integer) {
		FieldElement[] returnArray = { this.one, this.zero, this.zero,
				this.zero, this.zero, this.zero, this.zero, this.zero,
				this.zero, this.zero, this.zero, this.zero, };
		FieldElement[] field = inputField.clone();
		for (int i = 0; i < integer.bitLength(); i++) {
			if (integer.testBit(i)) {
				returnArray = this.extendMul(returnArray, field);
			}
			field = this.extendSquaring(field);
		}
		return returnArray;
	}

	/**
	 * @param extendFieldA
	 * @param extendFieldB
	 * @return add extend field
	 */
	public FieldElement[] extendSCAdd(final FieldElement[] extendFieldA,
			final FieldElement extendFieldB) {
		this.tmp[0] = extendFieldA[0].xor(extendFieldB);
		this.tmp[1] = extendFieldA[1].xor(extendFieldB);
		this.tmp[2] = extendFieldA[2].xor(extendFieldB);
		this.tmp[3] = extendFieldA[3].xor(extendFieldB);
		this.tmp[4] = extendFieldA[4].xor(extendFieldB);
		this.tmp[5] = extendFieldA[5].xor(extendFieldB);
		this.tmp[6] = extendFieldA[6].xor(extendFieldB);
		this.tmp[7] = extendFieldA[7].xor(extendFieldB);
		this.tmp[8] = extendFieldA[8].xor(extendFieldB);
		this.tmp[9] = extendFieldA[9].xor(extendFieldB);
		this.tmp[10] = extendFieldA[10].xor(extendFieldB);
		this.tmp[11] = extendFieldA[11].xor(extendFieldB);
		return this.tmp;
	}

	/**
	 * This method divibe k-extend filed polynomial by a simple number.
	 * 
	 * @param fieldArray
	 *            k-extend field from polynomials.
	 * @param div
	 *            simple number.
	 * @return [fieldArray[]]/[div] mod(modulus).
	 */
	public FieldElement[] extendSCDiv(final FieldElement[] fieldArray,
			final FieldElement div) {
		this.tmp[0] = this.div(fieldArray[0], div);
		this.tmp[1] = this.div(fieldArray[1], div);
		this.tmp[2] = this.div(fieldArray[2], div);
		this.tmp[3] = this.div(fieldArray[3], div);
		this.tmp[4] = this.div(fieldArray[4], div);
		this.tmp[5] = this.div(fieldArray[5], div);
		this.tmp[6] = this.div(fieldArray[6], div);
		this.tmp[7] = this.div(fieldArray[7], div);
		this.tmp[8] = this.div(fieldArray[8], div);
		this.tmp[9] = this.div(fieldArray[9], div);
		this.tmp[10] = this.div(fieldArray[10], div);
		this.tmp[11] = this.div(fieldArray[11], div);
		return this.tmp;
	}

	/**
	 * @param fieldArray
	 * @param mul
	 * @return sucler multiplication
	 */
	public FieldElement[] extendSCMul(final FieldElement[] fieldArray,
			final FieldElement mul) {
		this.tmp[0] = this.mul(fieldArray[0], mul);
		this.tmp[1] = this.mul(fieldArray[1], mul);
		this.tmp[2] = this.mul(fieldArray[2], mul);
		this.tmp[3] = this.mul(fieldArray[3], mul);
		this.tmp[4] = this.mul(fieldArray[4], mul);
		this.tmp[5] = this.mul(fieldArray[5], mul);
		this.tmp[6] = this.mul(fieldArray[6], mul);
		this.tmp[7] = this.mul(fieldArray[7], mul);
		this.tmp[8] = this.mul(fieldArray[8], mul);
		this.tmp[9] = this.mul(fieldArray[9], mul);
		this.tmp[10] = this.mul(fieldArray[10], mul);
		this.tmp[11] = this.mul(fieldArray[11], mul);
		return this.tmp;
	}

	/**
	 * @param field
	 * @return squaring for extend field
	 */
	public FieldElement[] extendSquaring(final FieldElement[] field) {
		this.tmpSq[0] = this.squaring(field[0]);
		this.tmpSq[1] = this.squaring(field[1]);
		this.tmpSq[2] = this.squaring(field[2]);
		this.tmpSq[3] = this.squaring(field[3]);
		this.tmpSq[4] = this.squaring(field[4]);
		this.tmpSq[5] = this.squaring(field[5]);
		this.tmpSq[6] = this.squaring(field[6]);
		this.tmpSq[7] = this.squaring(field[7]);
		this.tmpSq[8] = this.squaring(field[8]);
		this.tmpSq[9] = this.squaring(field[9]);
		this.tmpSq[10] = this.squaring(field[10]);
		this.tmpSq[11] = this.squaring(field[11]);

		final FieldElement[] retunArray = new FieldElement[12];
		retunArray[0] = this.tmpSq[0].xor(this.tmpSq[10]).xor(this.tmpSq[11])
				.xor(this.tmpSq[3]).xor(this.tmpSq[4]).xor(this.tmpSq[7]).xor(
						this.tmpSq[8]);
		retunArray[1] = this.tmpSq[4].xor(this.tmpSq[7]).xor(this.tmpSq[9]);
		retunArray[2] = this.tmpSq[1].xor(this.tmpSq[10]).xor(this.tmpSq[3])
				.xor(this.tmpSq[5]).xor(this.tmpSq[7]).xor(this.tmpSq[9]);
		retunArray[3] = this.tmpSq[11].xor(this.tmpSq[3]).xor(this.tmpSq[5])
				.xor(this.tmpSq[6]).xor(this.tmpSq[9]);
		retunArray[4] = this.tmpSq[10].xor(this.tmpSq[11]).xor(this.tmpSq[2])
				.xor(this.tmpSq[7]).xor(this.tmpSq[8]).xor(this.tmpSq[9]);
		retunArray[5] = this.tmpSq[11].xor(this.tmpSq[3]).xor(this.tmpSq[6])
				.xor(this.tmpSq[8]);
		retunArray[6] = this.tmpSq[10].xor(this.tmpSq[6]).xor(this.tmpSq[9]);
		retunArray[7] = this.tmpSq[10];
		retunArray[8] = this.tmpSq[11].xor(this.tmpSq[7]).xor(this.tmpSq[9]);
		retunArray[9] = this.tmpSq[11].xor(this.tmpSq[9]);
		retunArray[10] = this.tmpSq[8];
		retunArray[11] = this.tmpSq[9];
		return retunArray;
	}

	public FieldElement[] finalExp(final FieldElement[] f) {
		// a = f^(2^(6m)-1)
		// FieldElement[] f0 = {f[0],f[1],f[2],f[3],f[4],f[5]};
		// FieldElement[] f1 = {f[6],f[7],f[8],f[9],f[10],f[11]};
		// FieldElement[] f01 = exMul(f0, f1);
		// f0 = exSquare(f0);
		// f1 = exSquare(f1);
		// FieldElement[] fw3 = w3(f1);
		// FieldElement[] fw5 = w5(f1);
		FieldElement[] a = extendPow(f, BigInteger.ZERO.setBit(6 * getM())
				.subtract(BigInteger.ONE));
		// System.out.println(BigInteger.ZERO.setBit(6*m).subtract(BigInteger.ONE));
		// FieldElement[] a = f;

		// a^
		a = frobenius(frobenius(frobenius(a)));
		FieldElement[] tmp = frobenius(frobenius(frobenius(a)));
		a = extendMul(a, tmp);
		tmp = frobenius(frobenius(frobenius(frobenius(tmp))));
		for (int i = 0; i < (getM() + 1) / 2; i++) {
			tmp = extendSquaring(tmp);
		}
		a = extendMul(a, tmp);

		return a;
	}

	public FieldElement[] frobenius(final FieldElement[] f) {
		// m%64=15
		// s -> 1+s+w^3+w^5
		FieldElement[] w = { add(f[1], f[5]), f[2],
				add(add(f[0], f[1]), add(f[3], f[5])),
				add(add(f[2], f[4]), f[5]), add(add(f[0], f[3]), f[5]),
				add(add(f[0], f[4]), f[5]) };

		FieldElement[] s = { add(f[7], f[11]), f[8],
				add(add(f[6], f[7]), add(f[9], f[11])),
				add(add(f[8], f[10]), f[11]), add(add(f[6], f[9]), f[11]),
				add(add(f[6], f[10]), f[11]) };

		w = exAdd(exAdd(w, s), exAdd(w3(s), w5(s)));

		FieldElement[] tmp = { w[0], w[1], w[2], w[3], w[4], w[5], s[0], s[1],
				s[2], s[3], s[4], s[5] };

		return tmp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairing.cipher.risk.tsukuba.ac.jp.ExtendField#print(pairing.cipher.risk
	 * .tsukuba.ac.jp.FieldElement[])
	 */
	@Override
	public void print(final FieldElement[] element) {
		System.out.println(element[0].toBigInteger());
		System.out.println(element[1].toBigInteger());
		System.out.println(element[2].toBigInteger());
		System.out.println(element[3].toBigInteger());
		System.out.println(element[4].toBigInteger());
		System.out.println(element[5].toBigInteger());
		System.out.println(element[6].toBigInteger());
		System.out.println(element[7].toBigInteger());
		System.out.println(element[8].toBigInteger());
		System.out.println(element[9].toBigInteger());
		System.out.println(element[10].toBigInteger());
		System.out.println(element[11].toBigInteger());
	}

	public FieldElement[] w3(final FieldElement[] f) {
		// a = f^(2^(6m)-1)
		FieldElement[] tmp = { add(add(f[4], f[3]), f[2]), add(f[4], f[1]),
				add(f[3], f[0]), add(f[4], f[3]), add(f[5], f[4]),
				add(add(f[5], f[4]), f[3]) };
		return tmp;
	}

	public FieldElement[] w5(final FieldElement[] f) {
		FieldElement[] tmp = { add(add(f[2], f[1]), f[0]), f[2],
				add(f[5], f[1]), add(add(f[5], f[4]), add(f[2], f[1])),
				add(add(f[2], f[3]), f[2]), add(add(f[3], f[2]), f[1]) };
		return tmp;
	}
}
