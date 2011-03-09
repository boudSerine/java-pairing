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
 * set a(1),a(2),a(3),...,a(m-1),a(m) into BigInteger coef_m <br>
 * <br>
 * define GF2^km <br>
 * element of GF2^km is shown in polynomial coef_m(1)T^p + coef_m(2)T^p-1 + ...
 * + coef_m(p-1)T + coef_m(p) <br>
 * set coef_m(1),coef_m(2),...,coef_m(p) into BigInteger[] coef_km <br>
 * 
 * @author TyouIifan at cipher.risk.tsukuba.ac.jp
 * @version 0.1
 * @since 0.1
 */
public class GF2m {

	private int coef[];

	private final int k;

	private int kBit;

	private int kInt;

	private int kUp, kLow, mUp, mLow, cont;

	private final int m;

	private final int mBit;

	public final int mInt;

	private final int mkInt;

	private final int mkShiftLow;

	private final int mkShiftUp;

	private int mMask;

	/**
	 * modulus to reduction field elements
	 */
	public final FieldElement modulus;

	private final int mShiftLow;

	private final int mShiftUp;

	/**
	 * 1 for the field element
	 */
	public final FieldElement one;

	private final int[] sq = new int[65536];

	private FieldElement tmp;

	/**
	 * 0 for the field element
	 */
	public final FieldElement zero;

	/**
	 * set irreducible polynomial
	 * 
	 * @param m
	 *            bitLength.
	 * @param k
	 */
	public GF2m(final int m, final int[] k) {
		this.m = m;
		this.mInt = m >>> 5;
		this.mBit = m % 32;
		this.k = k[0];
		this.kInt = this.k >>> 5;
		this.kBit = this.k % 32;
		this.mkShiftUp = (m - k[0]) % 32;
		this.mkShiftLow = 32 - mkShiftUp;
		this.mShiftUp = mBit;
		this.mShiftLow = 32 - mShiftUp;
		this.mkInt = (m - k[0]) >>> 5;
		this.mMask = 0;
		for (int i = 31; i >= mBit; i--) {
			mMask |= Mask.m[i];
		}

		this.modulus = new FieldElement(m).setBit(m).setBit(k[0]).setBit(0);
		this.zero = this.modulus.getZERO();
		this.one = zero.getONE();
		this.tmp = this.zero.getClone();

		for (int i = 0; i <= 65535; i++) {
			for (int j = 0; j < 16; j++) {
				if ((i & Mask.m[j]) != 0) {
					sq[i] |= Mask.m[2 * j];
				}
			}
		}
		this.coef = new int[2 * (mInt + 1)];
	}

	/**
	 * This method calc addition for two field in GF2m.
	 * 
	 * @param fieldA
	 *            finite field from polynomial.
	 * @param fieldB
	 *            finite field from polynomial.
	 * @return fieldA+fieldB mod(modulus).
	 */
	public FieldElement add(final FieldElement fieldA, final FieldElement fieldB) {
		return fieldA.xor(fieldB);
	}

	/**
	 * This method divibe field polynomial by a simple number.
	 * 
	 * @param field
	 *            finite field from polynomials.
	 * @param div
	 *            simple number.
	 * @return [field]/[div] mod(modulus).
	 */
	public FieldElement div(final FieldElement field, final FieldElement div) {
		return this.mul(field, this.inv(div));
	}

	/**
	 * @return k
	 */
	public int getK() {
		return this.k;
	}

	/**
	 * @return m
	 */
	public int getM() {
		return this.m;
	}

	/**
	 * @return 1 for the field element
	 */
	public FieldElement getONE() {
		return one.getClone();
	}

	/**
	 * @return 0 for the field element
	 */
	public FieldElement getZERO() {
		return zero.getClone();
	}

	/**
	 * This method calc the inversion polynomial for input field polynomial. It
	 * use for division.
	 * 
	 * @param field
	 *            finite field from polynomial.
	 * @return [field]^-1 mod(modulus).
	 */
	public FieldElement inv(final FieldElement field) {
		/**
		 * extend Euclid algorithm
		 */
		FieldElement b = this.one.getClone();
		FieldElement c = this.zero.getClone();
		FieldElement u = field.getClone();
		FieldElement v = this.modulus;
		int j;
		while (u.bitLength() > 1) {
			j = u.bitLength() - v.bitLength();
			if (j < 0) {
				tmp = u;
				u = v;
				v = tmp;
				tmp = c;
				c = b;
				b = tmp;
				j = -j;
			}
			u = u.xor(v.shiftLeft(j));
			// u.toXor(v.shiftLeft(j));
			b.toXor(c.shiftLeft(j));
		}
		return b;
	}

	/**
	 * This method calc the mod, use modulus which has been set.
	 * 
	 * @param field
	 *            finite field from polynomial.
	 * @return field mod(modulus).
	 */
	public FieldElement mod(final FieldElement field) {
		if (field.compareTo(this.modulus) < 0) {
			return field;
		} else {
			int tmp = field.getCoef(mInt);
			FieldElement f = field;
			for (int i = 4; i >= 0; i--) {
				if ((tmp ^ Mask.m[mBit + i]) != 0) {
					// f.toXor(mod[i]);
				}
			}
			return f;
		}
	}

	/**
	 * this method use the multiplication of polynomials in F2[X]
	 * 
	 * @param a
	 * @param b
	 * @return fieldV*fieldU mod(modulus).
	 */
	public FieldElement mul(final FieldElement a, FieldElement b) {
		int[] B = new int[mInt + 2];
		for (int i = 0; i <= mInt; i++) {
			B[i] = b.getCoef(i);
			coef[i] = 0;
		}
		B[mInt + 1] = 0;
		for (int i = mInt + 1; i <= 2 * mInt + 1; i++) {
			coef[i] = 0;
		}

		for (int k = 0; k <= 30; k++) {
			for (int j = 0; j <= mInt; j++) {
				if ((a.getCoef(j) & Mask.m[k]) != 0) {
					for (int cj = 0; cj <= mInt + 1; cj++) {
						coef[cj + j] ^= B[cj];
					}
				}
			}
			// B shift left
			for (int i = mInt + 1; i > 0; i--) {
				B[i] <<= 1;
				if (B[i - 1] < 0) {
					B[i] |= Mask.m[0];
				}
			}
			B[0] <<= 1;
		}
		for (int j = 0; j <= mInt; j++) {
			if ((a.getCoef(j) & Mask.m[31]) != 0) {
				for (int cj = 0; cj <= mInt + 1; cj++) {
					coef[cj + j] ^= B[cj];
				}
			}
		}

		// bShift[0] = b.getClone();
		// bShift[1] = bShift[0].shiftLeft().getClone();
		// bShift[2] = bShift[1].shiftLeft().getClone();
		// bShift[3] = bShift[2].shiftLeft().getClone();
		//
		// int[] A = new int[mInt +1];
		// for (int i = 0; i <= mInt; i++) {
		// A[i] = a.getCoef(i);
		// }
		//		
		// int[][] B = new int[16][mInt + 1];
		// for (int i = 0; i < 16; i++) {
		// for (int j = 0; j <= mInt; j++) {
		// B[i][j] = 0;
		// }
		// }
		// for (int i = 0; i < 16; i++) {
		// for (int j = 0; j <= mInt; j++) {
		// for (int k = 0; k < 4; k++) {
		// if ((i & Mask.m[k]) != 0) {
		// B[i][j] ^= bShift[k].getCoef(j);
		// }
		// }
		// }
		// }
		// for (int j = 0; j <= 2 * mInt + 1; j++) {
		// coef[j] = 0;
		// }
		//
		// for (int k = 0; k <= 7; k++) {
		// for (int j = 0; j <= mInt; j++) {
		// for (int cj = 0; cj <= mInt + 1; cj++) {
		// mUp = (A[j] >>> 4*j) & mask4;
		// coef[cj + j] ^= B[mUp][cj];
		// }
		// }
		// }

		for (int i = 2 * mInt + 1; i > mInt; i--) {
			kUp = coef[i] >>> mkShiftUp;
			kLow = coef[i] << mkShiftLow;
			mUp = coef[i] >>> mShiftUp;
			mLow = coef[i] << mShiftLow;
			cont = i - mkInt;
			coef[cont] ^= kUp;
			coef[--cont] ^= kLow;
			cont = i - mInt;
			coef[cont] ^= mUp;
			coef[--cont] ^= mLow;
		}
		mUp = coef[mInt] & mMask;
		coef[mInt] ^= mUp;
		coef[0] ^= mUp >>> mBit;
		if (mBit >= kBit) {
			coef[kInt] ^= mUp >>> (mBit - kBit);
		} else {
			coef[kInt] ^= mUp << (kBit - mBit);
			coef[kInt + 1] ^= mUp >>> 32 - (kBit - mBit);
		}

		FieldElement ret = zero.getClone();
		for (int i = 0; i <= mInt; i++) {
			ret.toCoefXor(i, coef[i]);
		}
		return ret;
	}

	/**
	 * this method use to calc field^pow
	 * 
	 * @param field
	 * @param pow
	 * @return field^pow
	 */
	public FieldElement pow(final FieldElement field, final BigInteger pow) {
		FieldElement returnField = this.one.getClone();
		FieldElement tmp = field;
		for (int i = 0; i < pow.bitLength(); i++) {
			if (pow.testBit(i)) {
				returnField = this.mul(returnField, tmp);
			}
			tmp = this.squaring(tmp);
		}
		return returnField;
	}

	/**
	 * @param field
	 * @return square root of input
	 */
	public FieldElement squareRoot(final FieldElement field) {
		FieldElement root;
		if (this.k % 2 == 0) {
			if (1 + 2 * this.k - this.m < 0) {
				root = this.mul(this.zero.getClone().setBit(
						(1 + 4 * this.k - this.m) >>> 1).setBit(
						(this.m + 2 * this.k + 1) >>> 1).setBit(
						(this.m + 1) >>> 1), this.zero.getClone().setBit(
						this.k >>> 1).setBit(0));
			} else {
				root = this.mul(this.zero.getClone().setBit(
						(1 + 2 * this.k - this.m)).setBit((this.m + 1) >>> 1),
						this.zero.getClone().setBit(this.k >>> 1).setBit(0));
			}
		} else {
			root = this.zero.getClone().setBit((this.m + 1) >>> 1).setBit(
					(this.k + 1) >>> 1);
		}
		this.tmp = this.zero.getClone();
		FieldElement odd = this.zero.getClone();

		for (int i = 0; i <= mInt; i++) {
			coef[i] = field.getCoef(i);
		}
		for (int i = 0; i <= mInt; i++) {
			for (int j = 0; j < 32; j++) {
				if ((this.coef[i] & Mask.m[j]) != 0) {
					this.tmp.toSetBit((i * 32 + j) >>> 1);
				}
				j++;
				if ((this.coef[i] & Mask.m[j]) != 0) {
					odd.toSetBit((i * 32 + j) >>> 1);
				}
			}
		}
		return this.tmp.xor(this.mul(odd, root));
	}

	/**
	 * @param field
	 * @param root
	 * @return square root of input
	 */
	public FieldElement squareRoot(final FieldElement field,
			final FieldElement root) {
		this.tmp = this.zero.getClone();
		FieldElement odd = this.zero.getClone();

		for (int i = 0; i <= mInt; i++) {
			coef[i] = field.getCoef(i);
		}
		for (int i = 0; i <= mInt; i++) {
			for (int j = 0; j < 32; j++) {
				if ((this.coef[i] & Mask.m[j]) != 0) {
					this.tmp.toSetBit((i * 32 + j) >>> 1);
				}
				j++;
				if ((this.coef[i] & Mask.m[j]) != 0) {
					odd.toSetBit((i * 32 + j) >>> 1);
				}
			}
		}
		return this.tmp.xor(this.mul(odd, root));
	}

	/**
	 * @param field
	 * @return binary squaring
	 */
	public FieldElement squaring(final FieldElement field) {
		// int coef[] = new int[2 * (mInt + 1)];
		for (int i = 0; i <= mInt; i++) {
			coef[2 * i] = sq[field.getCoef(i) & 65535];
			coef[(2 * i) + 1] = sq[field.getCoef(i) >>> 16];
		}

		for (int i = 2 * mInt + 1; i > mInt; i--) {
			kUp = coef[i] >>> mkShiftUp;
			kLow = coef[i] << mkShiftLow;
			mUp = coef[i] >>> mShiftUp;
			mLow = coef[i] << mShiftLow;
			cont = i - mkInt;
			coef[cont] ^= kUp;
			coef[--cont] ^= kLow;
			cont = i - mInt;
			coef[cont] ^= mUp;
			coef[--cont] ^= mLow;
		}
		mUp = coef[mInt] & mMask;
		coef[mInt] ^= mUp;
		coef[0] ^= mUp >>> mBit;
		if (mBit >= kBit) {
			coef[kInt] ^= mUp >>> (mBit - kBit);
		} else {
			coef[kInt] ^= mUp << (kBit - mBit);
			coef[kInt + 1] ^= mUp >>> 32 - (kBit - mBit);
		}

		FieldElement ret = zero.getClone();
		for (int i = 0; i <= mInt; i++) {
			ret.toCoefXor(i, coef[i]);
		}
		return ret;
	}

	/**
	 * This method calc addition for two field in GF2m.
	 * 
	 * @param fieldA
	 *            finite field from polynomial.
	 * @param fieldB
	 *            finite field from polynomial.
	 */
	public void toAdd(final FieldElement fieldA, final FieldElement fieldB) {
		fieldA.toXor(fieldB);
	}

	/**
	 * This method divibe field polynomial by a simple number.
	 * 
	 * @param field
	 *            finite field from polynomials.
	 * @param div
	 *            simple number.
	 */
	public void toDiv(final FieldElement field, final FieldElement div) {
		this.toMul(field, this.inv(div));
	}

	/**
	 * this method use the multiplication of polynomials in F2[X]
	 * 
	 * @param fieldU
	 *            finite field from polynomial.
	 * @param fieldV
	 *            finite field from polynomial.
	 * @return fieldV*fieldU mod(modulus).
	 */
	public void toMul(FieldElement a, FieldElement b) {
		int[] B = new int[mInt + 2];
		for (int i = 0; i <= mInt; i++) {
			B[i] = b.getCoef(i);
			coef[i] = 0;
		}
		B[mInt + 1] = 0;
		for (int i = mInt + 1; i <= 2 * mInt + 1; i++) {
			coef[i] = 0;
		}

		for (int k = 0; k <= 30; k++) {
			for (int j = 0; j <= mInt; j++) {
				if ((a.getCoef(j) & Mask.m[k]) != 0) {
					for (int cj = 0; cj <= mInt + 1; cj++) {
						coef[cj + j] ^= B[cj];
					}
				}
			}
			// B shift left
			for (int i = mInt + 1; i > 0; i--) {
				B[i] <<= 1;
				if (B[i - 1] < 0) {
					B[i] |= Mask.m[0];
				}
			}
			B[0] <<= 1;
		}
		for (int j = 0; j <= mInt; j++) {
			if ((a.getCoef(j) & Mask.m[31]) != 0) {
				for (int cj = 0; cj <= mInt + 1; cj++) {
					coef[cj + j] ^= B[cj];
				}
			}
		}

		for (int i = 2 * mInt + 1; i > mInt; i--) {
			kUp = coef[i] >>> mkShiftUp;
			kLow = coef[i] << mkShiftLow;
			mUp = coef[i] >>> mShiftUp;
			mLow = coef[i] << mShiftLow;
			cont = i - mkInt;
			coef[cont] ^= kUp;
			coef[--cont] ^= kLow;
			cont = i - mInt;
			coef[cont] ^= mUp;
			coef[--cont] ^= mLow;
		}
		mUp = coef[mInt] & mMask;
		coef[mInt] ^= mUp;
		coef[0] ^= mUp >>> mBit;
		if (mBit > kBit) {
			coef[kInt] ^= mUp >>> (mBit - kBit);
		} else {
			coef[kInt] ^= mUp >>> (mBit - kBit);
		}

		a = zero.getClone();
		for (int i = 0; i <= mInt; i++) {
			a.toCoefXor(i, coef[i]);
		}
	}
}
