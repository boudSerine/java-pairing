/**
 * 
 */
package pairing.cipher.risk.tsukuba.ac.jp;

import java.security.InvalidKeyException;
import java.security.spec.KeySpec;

import javax.crypto.spec.DESKeySpec;

/**
 * @author í£Å@àÍñ}
 * 
 */
public class PairingKeySpec implements KeySpec {

	FieldElement[] pairingOut;

	public PairingKeySpec(FieldElement[] elements) {
		for (int i = 0; i < elements.length; i++) {
			pairingOut[i] = elements[i];
		}
	}

	public boolean equals(PairingKeySpec key) {
		for (int i = 0; i < pairingOut.length; i++) {
			if (!key.getPairingOut(i).equals(this.pairingOut[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @return DESKye from pairing output;
	 */
	public DESKeySpec getDESKey() {
		try {
			return new DESKeySpec(pairingToBytes());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	public FieldElement[] getPairingOut() {
		return pairingOut;
	}

	public FieldElement getPairingOut(int count) {
		return pairingOut[count];
	}

	public byte[] pairingToBytes() {
		byte[] pairingValue = new byte[pairingOut[0].getM() * pairingOut.length
				/ 8];
		for (int i = 0; i < pairingOut.length; i++) {
			for (int j = 0; j < pairingOut[i].getInts(); j++) {
				pairingValue[i + 4 * j] = (byte) (pairingOut[i].getCoef(j));
				pairingValue[i + 4 * j + 1] = (byte) ((pairingOut[i].getCoef(j) >>> 18));
				pairingValue[i + 4 * j + 2] = (byte) ((pairingOut[i].getCoef(j) >>> 16));
				pairingValue[i + 4 * j + 3] = (byte) ((pairingOut[i].getCoef(j) >>> 24));
			}
		}
		return pairingValue;
	}

	public void print() {
		for (int i = 0; i < pairingOut.length; i++) {
			System.out.println(pairingOut[i]);
		}
	}

	public void setPairingOut(FieldElement element, int count) {
		this.pairingOut[count] = element;
	}

	public void setPairingOut(FieldElement[] pairingOut) {
		this.pairingOut = pairingOut;
	}

}
