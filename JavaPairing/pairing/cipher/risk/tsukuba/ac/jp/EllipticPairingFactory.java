/**
 * 
 */
package pairing.cipher.risk.tsukuba.ac.jp;

/**
 * @author í£Å@àÍñ}
 * 
 */
public class EllipticPairingFactory extends PairingFactory {

	EllipticCurveGF2m curve;

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairing.cipher.risk.tsukuba.ac.jp.PairingFactory#getCurve(int, int)
	 */
	@Override
	public EllipticCurveGF2m getCurve(int a, int b) {
		curve = new EllipticCurveGF2m(field, a, b);
		return curve;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairing.cipher.risk.tsukuba.ac.jp.AbstractPairingFactory#getField()
	 */
	@Override
	public ExtendField getField() {
		return field;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * pairing.cipher.risk.tsukuba.ac.jp.AbstractPairingFactory#setField(int,
	 * int[])
	 */
	@Override
	public ExtendField setField(int m, int k) {
		int[] ks = { k };
		field = new GF2m4(m, ks);
		return field;
	}

}
