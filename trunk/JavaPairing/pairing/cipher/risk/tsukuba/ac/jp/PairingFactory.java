/**
 *
 */
package pairing.cipher.risk.tsukuba.ac.jp;

/**
 * @author í£Å@àÍñ}
 *
 */
public abstract class PairingFactory {
	public static PairingFactory getFactory(boolean isElliptic, int m, int k) {
		PairingFactory tmp;
		if (isElliptic) {
			tmp = new EllipticPairingFactory();
		} else {
			tmp = new HyperellipticPairingFactory();
		}
		tmp.setField(m, k);
		return tmp;
	}

	public ExtendField field;

	private int m;

	private int k;

	abstract public EllipticCurveGF2m getCurve(int a, int b);

	abstract public ExtendField getField();
	abstract public ExtendField setField(int m, int k);
}
