package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;
import java.util.Random;

/**
 * this is the interface of FieldElment classes
 *
 * @author í£Å@àÍñ}
 */

public class FieldElement {
	public static FieldElement newONE(int m) {
		FieldElement tmp = new FieldElement(m);
		tmp.toSetCoef(0, 1);
		return tmp;
	}
	public static FieldElement newRANDOM(int m, Random rand) {
		FieldElement tmp = new FieldElement(m);
		for (int i = 0; i < tmp.getInts(); i++) {
			tmp.toSetCoef(i, rand.nextInt());
		}
		tmp.toSetCoef(tmp.getInts(), rand.nextInt(Mask.m[m % 32]));
		return tmp;
	}
	public static FieldElement newZERO(int m) {
		FieldElement tmp = new FieldElement(m);
		return tmp;
	}

	protected int[] coef;

	protected int ints;

	protected int m;

	/**
	 * constructor with BigInteger
	 *
	 * @param value
	 *            this = value
	 */
	public FieldElement(final BigInteger value, int m) {
		this.m =m;
		ints = m / 32;
		coef = new int[ints+1];
		for (int j = 0; j <= ints; j++) {
			for (int i = 0; i < 32; i++) {
				if (value.testBit(i + 32 * j)) {
					this.coef[j] |= Mask.m[i];
				}
			}
		}
	}

	/**
	 * constructor
	 */
	public FieldElement(int m) {
		ints = m / 32;
		this.m = m;
		coef = new int[ints + 1];
		for (int i = 0; i <= this.ints; i++) {
			coef[i] = 0;
		}
	}

	public FieldElement(int m, BigInteger bigInteger) {
		ints = m / 32;
		this.m = m;
		coef = new int[ints + 1];
		for (int i = 0; i <= this.ints; i++) {
			coef[i] = 0;
		}
		for (int j = 0; j <= ints; j++) {
			for (int i = 0; i < 32; i++) {
				if (bigInteger.testBit(i + 32 * j)) {
					this.coef[j] |= Mask.m[i];
				}
			}
		}
	}

	public FieldElement(int m, int ints) {
		this.ints = ints;
		this.m = m;
		coef = new int[ints + 1];
		for (int i = 0; i <= this.ints; i++) {
			coef[i] = 0;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pairing.cipher.risk.tsukuba.ac.jp.FieldElement#bitLength()
	 */
	public int bitLength() {
		int leng = 0;
		for (int i = this.ints; i >= 0; i--) {
			if (this.coef[i] != 0) {
				for (int j = 31; j >= 0; j--) {
					if ((this.coef[i] & Mask.m[j]) != 0) {
						leng = j + 1 + 32 * i;
						break;
					}
				}
				break;
			}
		}
		return leng;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#coefXor(int, int)
	 */

	public void changeToBigInteger(final BigInteger value) {
		for (int j = 0; j <= this.ints; j++) {
			coef[j] = 0;
			for (int i = 0; i < 32; i++) {
				if (value.testBit(i + 32 * j)) {
					this.coef[j] |= Mask.m[i];
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#compareTo(tyouiifan.
	 * cipher.risk.tsukuba.ac.jp.FieldElement)
	 */

	/**
	 * constructor with FieldElement itself
	 *
	 * @param tmp
	 *            this = tmp
	 */
	public FieldElement clone(final FieldElement tmp) {
		for (int i = 0; i <= this.ints; i++) {
			this.coef[i] = tmp.coef[i];
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#equals(tyouiifan.cipher
	 * .risk.tsukuba.ac.jp.FieldElement)
	 */

	public FieldElement coefXor(final int i, final int xor) {
		FieldElement tmp = this.getClone();
		tmp.toCoefXor(i, xor);
		return tmp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#getClone()
	 */

	public int compareTo(final FieldElement y) {
		for (int j = this.ints; j >= 0; j--) {
			if (this.coef[j] > y.getCoef(j)) {
				return 1;
			}
			if (this.coef[j] < y.getCoef(j)) {
				return -1;
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#getCoef(int)
	 */

	public boolean equals(final FieldElement field) {
		for (int i = 0; i <= this.ints; i++) {
			if (this.coef[i] != field.getCoef(i)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see pairing.cipher.risk.tsukuba.ac.jp.FieldElement#getInts()
	 */

	public FieldElement getClone() {
		FieldElement tmp = new FieldElement(this.m, this.ints);
		for (int i = 0; i <= this.ints; i++) {
			tmp.coef[i] = this.coef[i];
		}
		return tmp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#getONE()
	 */

	public int getCoef(final int i) {
		return this.coef[i];
	}

	public int getInts() {
		// TODO Auto-generated method stub
		return ints;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#getZERO()
	 */

	public int getM() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#setBit(int)
	 */

	public FieldElement getONE() {
		return new FieldElement(m).setCoef(0, 1);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#setCoef(int, int)
	 */

	/*
	 * (non-Javadoc)
	 *
	 * @see pairing.cipher.risk.tsukuba.ac.jp.FieldElement#RANDOM(int)
	 */
	public FieldElement getRANDOM(Random rand) {
		FieldElement tmp = new FieldElement(this.getM());
		for (int i = 0; i < tmp.getInts(); i++) {
			tmp.toSetCoef(i, rand.nextInt());
		}
		tmp.toSetCoef(tmp.getInts(), rand.nextInt(Mask.m[this.getM() % 32]));
		return tmp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#shiftLeft()
	 */

	public FieldElement getZERO() {
		return new FieldElement(m);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#shiftLeft(int)
	 */

	/**
	 * @return the count of bits sat to 1
	 */
	public int hammingCount() {
		int count = 0;
		for (int i = 0; i <= this.ints; i++) {
			count += Integer.bitCount(coef[i]);
		}
		return count;
	}

	public FieldElement setBit(final int i) {
		FieldElement tmp = this.getClone();
		tmp.toSetBit(i);
		return tmp;
	}

	public FieldElement setCoef(final int i, final int value) {
		FieldElement tmp = this.getClone();
		tmp.toSetCoef(i, value);
		return tmp;
	}

	public FieldElement shiftLeft() {
		final FieldElement tmp = this.getClone();
		tmp.toShiftLeft();
		return tmp;
	}

	public FieldElement shiftLeft(final int j) {
		final FieldElement tmp = this.getClone();
		tmp.toShiftLeft(j);
		return tmp;
	}

	/**
	 * @return
	 */
	public FieldElement shiftRight() {
		final FieldElement tmp = this.getClone();
		tmp.toShiftRight();
		return tmp;
	}

	/**
	 * @param j
	 * @return
	 */
	public FieldElement shiftRight(final int j) {
		final FieldElement tmp = this.getClone();
		tmp.toShiftRight(j);
		return tmp;
	}

	public boolean testBit(final int i) {
		return (this.coef[i >>> 5] & Mask.m[i & Mask.mod32]) != 0;
	}

	public int testBitInt(final int i) {
		return (this.coef[i >>> 5] >> (i & Mask.mod32)) & 1;
	}

	public BigInteger toBigInteger() {
		BigInteger value = BigInteger.ZERO;
		// for(int i = 7; i>=0;i--){
		// value = value.shiftLeft(32).xor(new
		// BigInteger(String.valueOf(coef[i])));
		// }
		for (int i = 0; i < 256; i++) {
			if (this.testBit(i)) {
				value = value.setBit(i);
			}
		}
		return value;
	}

	public void toCoefXor(final int i, final int j) {
		this.coef[i] ^= j;
	}

	public void toSetBit(final int i) {
		this.coef[i >>> 5] |= Mask.m[i & Mask.mod32];
	}

	public void toSetCoef(final int i, final int value) {
		this.coef[i] = value;
	}

	public void toShiftLeft() {
		this.coef[7] <<= 1;
		if (this.coef[6] < 0) {
			this.coef[7] |= Mask.m[0];
		}
		this.coef[6] <<= 1;
		if (this.coef[5] < 0) {
			this.coef[6] |= Mask.m[0];
		}
		this.coef[5] <<= 1;
		if (this.coef[4] < 0) {
			this.coef[5] |= Mask.m[0];
		}
		this.coef[4] <<= 1;
		if (this.coef[3] < 0) {
			this.coef[4] |= Mask.m[0];
		}
		this.coef[3] <<= 1;
		if (this.coef[2] < 0) {
			this.coef[3] |= Mask.m[0];
		}
		this.coef[2] <<= 1;
		if (this.coef[1] < 0) {
			this.coef[2] |= Mask.m[0];
		}
		this.coef[1] <<= 1;
		if (this.coef[0] < 0) {
			this.coef[1] |= Mask.m[0];
		}
		this.coef[0] <<= 1;
	}

	public void toShiftLeft(final int j) {
		for (int k = j - 1; k >= 0; k--) {
			this.coef[7] <<= 1;
			if (this.coef[6] < 0) {
				this.coef[7] |= Mask.m[0];
			}
			this.coef[6] <<= 1;
			if (this.coef[5] < 0) {
				this.coef[6] |= Mask.m[0];
			}
			this.coef[5] <<= 1;
			if (this.coef[4] < 0) {
				this.coef[5] |= Mask.m[0];
			}
			this.coef[4] <<= 1;
			if (this.coef[3] < 0) {
				this.coef[4] |= Mask.m[0];
			}
			this.coef[3] <<= 1;
			if (this.coef[2] < 0) {
				this.coef[3] |= Mask.m[0];
			}
			this.coef[2] <<= 1;
			if (this.coef[1] < 0) {
				this.coef[2] |= Mask.m[0];
			}
			this.coef[1] <<= 1;
			if (this.coef[0] < 0) {
				this.coef[1] |= Mask.m[0];
			}
			this.coef[0] <<= 1;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#toXorBit(int)
	 */

	public void toShiftRight() {
		for (int i = 0; i < 7; i++) {
			this.coef[i] >>>= 1;
			if ((this.coef[i + 1] & Mask.m[0]) != 0) {
				this.coef[i] |= Mask.m[31];
			}
		}
		this.coef[7] >>= 1;
	}

	public void toShiftRight(final int j) {
		for (int k = j - 1; k >= 0; k--) {
			for (int i = 0; i < 7; i++) {
				this.coef[i] >>>= 1;
				if ((this.coef[i + 1] & Mask.m[0]) != 0) {
					this.coef[i] |= Mask.m[31];
				}
			}
			this.coef[7] >>= 1;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see tyouiifan.cipher.risk.tsukuba.ac.jp.FieldElement#xorBit(int)
	 */

	@Override
	public String toString() {
		return this.toBigInteger().toString();
	}

	public void toXor(final FieldElement y) {
		this.coef[0] ^= y.getCoef(0);
		this.coef[1] ^= y.getCoef(1);
		this.coef[2] ^= y.getCoef(2);
		this.coef[3] ^= y.getCoef(3);
		this.coef[4] ^= y.getCoef(4);
		this.coef[5] ^= y.getCoef(5);
		this.coef[6] ^= y.getCoef(6);
		this.coef[7] ^= y.getCoef(7);
	}

	public void toXorBit(int i) {
		// TODO Auto-generated method stub
		this.coef[i >>> 5] ^= Mask.m[i & Mask.mod32];
	}

	public FieldElement xor(final FieldElement y) {
		final FieldElement tmp = this.getClone();
		tmp.toXor(y);
		return tmp;
	}

	public FieldElement xorBit(int i) {
		final FieldElement tmp = this.getClone();
		tmp.toXorBit(i);
		return tmp;
	}
}
