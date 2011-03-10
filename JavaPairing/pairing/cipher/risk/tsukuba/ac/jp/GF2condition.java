package pairing.cipher.risk.tsukuba.ac.jp;

public class GF2condition {

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

	/**
	 * 0 for the field element
	 */
	public final FieldElement zero;

	private final int[] sq = new int[65536];

	private FieldElement tmp;

	public GF2condition(final int m, final int[] k) {
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

}
