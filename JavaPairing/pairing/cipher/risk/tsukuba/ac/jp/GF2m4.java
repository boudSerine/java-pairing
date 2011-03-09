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
public class GF2m4 extends ExtendField {
	private final FieldElement[] tmp = new FieldElement[4];

	private final FieldElement[] tmp2 = { this.modulus.getZERO(),
			this.modulus.getZERO() };

	/**
	 * set irreducible polynomial
	 * 
	 * @param m
	 *            bitLength.
	 * @param ks
	 */
	public GF2m4(final int m, final int[] ks) {
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
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i <= mInt; i++) {
				if (fields1[j].getCoef(i) != fields2[j].getCoef(i)) {
					return false;
				}
			}
		}
		return true;
	}

	public FieldElement[] exAdd(final FieldElement[] u, final FieldElement[] v) {
		final FieldElement[] tmp = new FieldElement[2];
		tmp[0] = u[0].xor(v[0]);
		tmp[1] = u[1].xor(v[1]);
		return tmp;
	}

	public FieldElement[] exInv(final FieldElement[] u) {
		FieldElement tmp2[] = { modulus.getZERO(), modulus.getZERO() };
		final FieldElement a0 = u[0].xor(u[1]);
		final FieldElement m0 = this.squaring(u[0]);
		final FieldElement m1 = this.mul(a0, u[1]);
		final FieldElement a1 = m0.xor(m1);
		final FieldElement i0 = this.inv(a1);
		tmp2[0] = this.mul(a0, i0);
		tmp2[1] = this.mul(u[1], i0);
		return tmp2;
	}

	public FieldElement[] exMul(final FieldElement[] u, final FieldElement[] v) {
		final FieldElement[] tmp = new FieldElement[2];
		final FieldElement uv0 = this.mul(u[0], v[0]);
		final FieldElement uv1 = this.mul(u[1], v[1]);
		final FieldElement uv01 = this.mul(u[0].xor(u[1]), v[0].xor(v[1]));
		tmp[0] = uv0.xor(uv1);
		tmp[1] = uv01.xor(uv0);
		return tmp;
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
		return this.tmp;
	}

	/**
	 * @param fieldArrayA
	 * @param fieldArrayB
	 * @return fieldA/fieldB
	 */
	public FieldElement[] extendDiv(final FieldElement[] fieldArrayA,
			final FieldElement[] fieldArrayB) {
		return this.extendMul(fieldArrayA, this.extendInv(fieldArrayB));
	}

	/**
	 * This is a k-extend field multiplication method in F2(4m).
	 * 
	 * @param fieldArray
	 *            k-extend field from polynomials.
	 * @return [fieldArrayA]*[fieldArrayB] mod(modulus).
	 */
	public FieldElement[] extendInv(final FieldElement[] fieldArray) {
		// this method can't use for k!=4.

		FieldElement B1;
		FieldElement BS;
		FieldElement BT;
		FieldElement BST;

		final FieldElement[] s = new FieldElement[4];
		s[0] = this.squaring(fieldArray[0]);
		s[1] = this.squaring(fieldArray[1]);
		s[2] = this.squaring(fieldArray[2]);
		s[3] = this.squaring(fieldArray[3]);
		final FieldElement a01 = this.mul(fieldArray[0], fieldArray[1]);
		final FieldElement a02 = this.mul(fieldArray[0], fieldArray[2]);
		final FieldElement a03 = this.mul(fieldArray[0], fieldArray[3]);
		final FieldElement a12 = this.mul(fieldArray[1], fieldArray[2]);
		final FieldElement a13 = this.mul(fieldArray[1], fieldArray[3]);
		final FieldElement a23 = this.mul(fieldArray[2], fieldArray[3]);

		FieldElement inv = this.squaring(s[0]).xor(this.squaring(s[1])).xor(
				this.squaring(s[2])).xor(this.squaring(s[3]));
		inv = inv.xor(this.mul(s[0], s[1])).xor(this.mul(a12, s[0]));
		inv = inv.xor(this.mul(a02, s[1])).xor(this.mul(s[1], a12));
		inv = inv.xor(this.mul(a01, s[2])).xor(this.mul(a02, s[2]));
		inv = inv.xor(this.mul(a03, s[0])).xor(this.mul(a13, s[0]));
		inv = inv.xor(this.mul(a03, s[1])).xor(this.mul(a23, s[0]));
		inv = inv.xor(this.mul(a01, a23)).xor(this.mul(a23, s[1]));
		inv = inv.xor(this.mul(a13, s[2])).xor(this.mul(s[0], s[3]));
		inv = inv.xor(this.mul(a01, s[3])).xor(this.mul(a12, s[3]));
		inv = inv.xor(this.mul(s[2], s[3])).xor(this.mul(a03, s[3]));
		inv = inv.xor(this.mul(a13, s[3]));
		inv = this.inv(inv);

		B1 = this.mul(fieldArray[0], s[0]);
		B1 = B1.xor(this.mul(fieldArray[1], s[1]));
		B1 = B1.xor(this.mul(fieldArray[0], a12));
		B1 = B1.xor(this.mul(fieldArray[2], s[1]));
		B1 = B1.xor(this.mul(fieldArray[2], s[2]));
		B1 = B1.xor(this.mul(fieldArray[3], s[0]));
		B1 = B1.xor(this.mul(fieldArray[0], a13));
		B1 = B1.xor(this.mul(fieldArray[0], a23));
		B1 = B1.xor(this.mul(fieldArray[1], a23));
		B1 = B1.xor(this.mul(fieldArray[3], s[2]));
		B1 = B1.xor(this.mul(fieldArray[1], s[3]));
		B1 = B1.xor(this.mul(fieldArray[2], s[3]));
		B1 = this.mul(B1, inv);

		BS = this.mul(fieldArray[1], s[0]);
		BS = BS.xor(this.mul(fieldArray[0], s[1]));
		BS = BS.xor(this.mul(fieldArray[1], s[1]));
		BS = BS.xor(this.mul(fieldArray[2], s[1]));
		BS = BS.xor(this.mul(fieldArray[0], s[2]));
		BS = BS.xor(this.mul(fieldArray[1], s[2]));
		BS = BS.xor(this.mul(fieldArray[2], s[2]));
		BS = BS.xor(this.mul(fieldArray[0], a13));
		BS = BS.xor(this.mul(fieldArray[1], a23));
		BS = BS.xor(this.mul(fieldArray[3], s[3]));
		BS = this.mul(BS, inv);

		BT = this.mul(fieldArray[2], s[0]);
		BT = BT.xor(this.mul(fieldArray[0], s[2]));
		BT = BT.xor(this.mul(fieldArray[1], s[2]));
		BT = BT.xor(this.mul(fieldArray[2], s[2]));
		BT = BT.xor(this.mul(fieldArray[3], s[1]));
		BT = BT.xor(this.mul(fieldArray[0], a23));
		BT = BT.xor(this.mul(fieldArray[1], a23));
		BT = BT.xor(this.mul(fieldArray[3], s[2]));
		BT = BT.xor(this.mul(fieldArray[0], s[3]));
		BT = BT.xor(this.mul(fieldArray[1], s[3]));
		BT = BT.xor(this.mul(fieldArray[2], s[3]));
		BT = this.mul(BT, inv);

		BST = this.mul(fieldArray[2], s[1]);
		BST = BST.xor(this.mul(fieldArray[1], s[2]));
		BST = BST.xor(this.mul(fieldArray[2], s[2]));
		BST = BST.xor(this.mul(fieldArray[3], s[0]));
		BST = BST.xor(this.mul(fieldArray[3], s[1]));
		BST = BST.xor(this.mul(fieldArray[1], a23));
		BST = BST.xor(this.mul(fieldArray[1], s[3]));
		BST = BST.xor(this.mul(fieldArray[3], s[3]));
		BST = this.mul(BST, inv);

		final FieldElement[] returnArray = new FieldElement[] { B1, BS, BT, BST };

		return returnArray;
	}

	/**
	 * @param inputField
	 * @param pow
	 * @return pow for inversion
	 */
	public FieldElement[] extendInvPow(final FieldElement[] inputField,
			final BigInteger pow) {
		return this.extendPow(this.extendInv(inputField), pow);
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
	public FieldElement[] extendMul(final FieldElement[] fieldArrayA,
			final FieldElement[] fieldArrayB) {
		final FieldElement[] returnArray = new FieldElement[4];
		// this method can't use for k!=4.
		this.tmp[0] = this.mul(fieldArrayA[0], fieldArrayB[0]);
		this.tmp[1] = this.mul(fieldArrayA[1], fieldArrayB[1]);
		this.tmp[2] = this.mul(fieldArrayA[2], fieldArrayB[2]);
		this.tmp[3] = this.mul(fieldArrayA[3], fieldArrayB[3]);
		final FieldElement A01 = fieldArrayA[0].xor(fieldArrayA[1]);
		final FieldElement A23 = fieldArrayA[2].xor(fieldArrayA[3]);
		final FieldElement B01 = fieldArrayB[0].xor(fieldArrayB[1]);
		final FieldElement B23 = fieldArrayB[2].xor(fieldArrayB[3]);
		final FieldElement a23 = this.mul(A23, B23);
		final FieldElement a01 = this.mul(A01, B01);
		final FieldElement a02 = this.mul(fieldArrayA[0].xor(fieldArrayA[2]),
				fieldArrayB[0].xor(fieldArrayB[2]));
		final FieldElement a13 = this.mul(fieldArrayA[1].xor(fieldArrayA[3]),
				fieldArrayB[1].xor(fieldArrayB[3]));
		final FieldElement aall = this.mul(A01.xor(A23), B01.xor(B23));
		final FieldElement s1 = this.tmp[0].xor(a23);
		final FieldElement tst = this.tmp[0].xor(a02);

		returnArray[0] = s1.xor(this.tmp[1]).xor(this.tmp[2]);
		returnArray[1] = s1.xor(this.tmp[3]).xor(a01);
		returnArray[2] = tst.xor(this.tmp[1]).xor(a13);
		returnArray[3] = tst.xor(aall).xor(a01);
		return returnArray;
	}

	/**
	 * This is a k-extend field power method in F2(4m).
	 * 
	 * @param inputField
	 *            k-extend field from polynomials.
	 * @param pow
	 *            FieldElement value to power fieldArray.
	 * @return [fieldArrayA[]]^[pow] mod(modulus).
	 */
	@Override
	public FieldElement[] extendPow(final FieldElement[] inputField,
			final BigInteger pow) {
		FieldElement[] returnArray = { this.modulus.getONE(),
				this.modulus.getZERO(), this.modulus.getZERO(),
				this.modulus.getZERO() };
		FieldElement[] field = inputField.clone();
		for (int i = 0; i < pow.bitLength(); i++) {
			if (pow.testBit(i)) {
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
		final FieldElement[] tmp = fieldArray.clone();
		for (int i = 0; i < this.getK(); i++) {
			tmp[i] = this.div(fieldArray[i], div);
		}
		return tmp;
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
		return this.tmp;
	}

	/**
	 * @param field
	 * @return squaring for extend field
	 */
	public FieldElement[] extendSquaring(final FieldElement[] field) {
		final FieldElement[] tmpArray = new FieldElement[4];
		this.tmp[0] = this.squaring(field[0]);
		this.tmp[1] = this.squaring(field[1]);
		this.tmp[2] = this.squaring(field[2]);
		this.tmp[3] = this.squaring(field[3]);
		tmpArray[0] = this.tmp[0].xor(this.tmp[1]).xor(this.tmp[3]);
		tmpArray[1] = this.tmp[1].xor(this.tmp[2]);
		tmpArray[2] = this.tmp[2].xor(this.tmp[3]);
		tmpArray[3] = this.tmp[3];
		return tmpArray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairing.cipher.risk.tsukuba.ac.jp.ExtendField#print()
	 */
	@Override
	public void print(FieldElement[] element) {
		System.out.println(element[0].toBigInteger());
		System.out.println(element[1].toBigInteger());
		System.out.println(element[2].toBigInteger());
		System.out.println(element[3].toBigInteger());
	}

	public FieldElement[] U22m1(final FieldElement[] u) {
		final FieldElement v0 = this.squaring(u[0]);
		final FieldElement v1 = this.squaring(u[1]);
		final FieldElement v2 = this.squaring(u[2]);
		final FieldElement v3 = this.squaring(u[3]);
		final FieldElement[] w0 = { v0.xor(v1), v1 };
		final FieldElement[] w1 = { v2.xor(v3), v3 };
		final FieldElement[] w2 = { v3, v2 };
		FieldElement[] tmp1 = { u[0], u[1] };
		FieldElement[] tmp2 = { u[2], u[3] };
		final FieldElement[] w3 = this.exMul(tmp1, tmp2);
		FieldElement[] w4 = this.exAdd(w0, w2);
		FieldElement[] D = this.exAdd(w3, w4);
		D = this.exInv(D);
		w4 = this.exAdd(w1, w4);
		tmp1 = this.exMul(D, w4);
		tmp2 = this.exMul(D, w1);
		final FieldElement[] tmp = new FieldElement[4];
		tmp[0] = tmp1[0];
		tmp[1] = tmp1[1];
		tmp[2] = tmp2[0];
		tmp[3] = tmp2[1];
		return tmp;
	}

	public FieldElement[] U2m1(final FieldElement[] u) {
		FieldElement v2;
		FieldElement v1;
		FieldElement v0;

		final FieldElement a0 = u[0].xor(u[1]);
		final FieldElement a1 = u[2].xor(u[3]);
		final FieldElement m0 = this.mul(a0, a1);
		final FieldElement m1 = this.mul(u[0], u[2]);
		final FieldElement m2 = this.mul(u[1], u[3]);
		final FieldElement m3 = this.mul(u[1], u[2]);
		final FieldElement m4 = this.mul(u[0], u[1]);
		final FieldElement m5 = this.mul(u[3], u[2]);
		final FieldElement s0 = this.squaring(a0);
		final FieldElement s1 = this.squaring(a1);
		final FieldElement a2 = m0.xor(m1).xor(m2);
		final FieldElement v3 = m5.xor(s1);
		if ((getM() % 8 == 1) | (getM() % 8 == 5)) {
			v2 = v3.xor(a2);
			v1 = v3.xor(m1).xor(m2).xor(m3);
			v0 = m4.xor(a2).xor(s0);
		} else {
			v2 = a2;
			v1 = v3.xor(m0).xor(m3);
			v0 = m1.xor(m2).xor(m3).xor(m4).xor(s0);
		}
		final FieldElement[] tmp = { v0, v1, v2, v3 };
		return tmp;
	}
}
