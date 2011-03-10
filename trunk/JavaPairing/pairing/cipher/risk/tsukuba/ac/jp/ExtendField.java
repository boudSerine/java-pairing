/**
 * 
 */
package pairing.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;

/**
 * @author í£Å@àÍñ}
 *
 */
/**
 * @author í£Å@àÍñ}
 * 
 */
public abstract class ExtendField extends GF2m {

	/**
	 * @param m
	 * @param k
	 */
	public ExtendField(int m, int[] k) {
		super(m, k);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param fields1
	 * @param fields2
	 * @return equal or not
	 */
	abstract public boolean equals(FieldElement[] fields1,
			FieldElement[] fields2);

	/**
	 * @param out2
	 * @param out3
	 * @return extend mul
	 */
	abstract public FieldElement[] extendMul(FieldElement[] out2,
			FieldElement[] out3);

	/**
	 * @param inputField
	 * @param pow
	 * @return extend power
	 */
	abstract public FieldElement[] extendPow(final FieldElement[] inputField,
			final BigInteger pow);

	/**
	 * @param element
	 */
	abstract public void print(FieldElement[] element);
}
