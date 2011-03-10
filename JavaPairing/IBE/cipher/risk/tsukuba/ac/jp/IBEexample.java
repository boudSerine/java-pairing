/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.math.BigInteger;
import java.util.Random;

import pairing.cipher.risk.tsukuba.ac.jp.EtaTPairing;

/**
 * @author ’£ ˆê–}
 * 
 */
public class IBEexample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int aE = 1;

		int bE = 1;

		boolean elliptic = true;

		IBE IBE964 = new IBE(241, 70, aE, bE, elliptic);

		IBE964.setup();

		IBE964.setPairing(new EtaTPairing(IBE964.getField(), bE));

		BigInteger ID = new BigInteger(IBE964.getM(), new Random());

		// IBE964.extract(ID);

	}

}
