/**
 * 
 */
package pairing.cipher.risk.tsukuba.ac.jp;

/**
 * @author í£Å@àÍñ}
 * 
 */
public interface Pairing {
	/**
	 * 
	 */
	public int getEmbedingDegree();

	public ExtendField getField();

	public int getPairingLength();

	public ECPoint hyperMul(ECPoint P);

	public FieldElement[] pairing(final ECPoint P, final ECPoint Q);

	public void print();

	public void setup(final ExtendField field, final int b);
}
