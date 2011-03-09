/**
 * 
 */
package pairing.cipher.risk.tsukuba.ac.jp;

/**
 * @author í£Å@àÍñ}
 * 
 */
public class HyperellipticPairingFactory extends PairingFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see pairing.cipher.risk.tsukuba.ac.jp.PairingFactory#getCurve(int, int)
	 */
	@Override
	public EllipticCurveGF2m getCurve(int a, int b) {
		// TODO Auto-generated method stub
		return null;
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
	 * int)
	 */
	@Override
	public ExtendField setField(int m, int k) {
		int[] ks = { k };
		field = new GF2m12(m, ks);
		return field;
	}

}
