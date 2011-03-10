/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JProgressBar;

import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;

/**
 * @author ���@��}
 * 
 */
public class dataCtrl extends Thread {
	public static IBE ibe;
	public int length;
	private int all = 0;
	private int size = 0;
	public JProgressBar pb;

	public dataCtrl(IBE ibe, int length) {
		this.ibe = ibe;
		this.length = length;
	}

	public byte[] pairingToBytes(FieldElement[] pairingOut) {
		byte[] pairingValue = new byte[length];
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

	public byte[] hashPairingValue(byte[] pairingValue)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] H2 = new byte[length];
		int hashLength = 0;
		while (hashLength / 8 < length) {
			md.update(pairingValue);
			for (int i = 0; i < md.getDigestLength(); i++) {
				if (hashLength + i < length) {
					H2[hashLength + i] = md.digest()[i];
				}
			}
			hashLength += md.getDigestLength();
		}
		return H2;
	}

	public byte[] H2(FieldElement[] pairingOut) {
		try {
			return hashPairingValue(pairingToBytes(pairingOut));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void fileWriter(String fileName, String newFileName, byte[] H2)
			throws IOException {
		FileInputStream inStream = new FileInputStream(fileName);
		FileOutputStream outStream = new FileOutputStream(newFileName, true);
		int in = 0;
		all = 0;
		size = inStream.available();
		int tmp = 0;
		for (; (in = inStream.read()) != -1; all++) {
			// outStream.write((byte) in ^ H2[all % length]);
			outStream.write((byte) in ^ H2[tmp % length]);
			tmp = in;
		}
		inStream.close();
		outStream.close();
	}

	public void fileWite(String cipherFile, String newfileName, int skip,
			byte[] H2) throws IOException {
		FileInputStream inStream = new FileInputStream(cipherFile);
		inStream.skip(skip);
		FileOutputStream outStream = new FileOutputStream(newfileName);
		int in = 0;
		all = 0;
		size = inStream.available();
		for (; (in = inStream.read()) != -1; all++) {
			outStream.write((byte) in ^ H2[all % length]);
		}
		inStream.close();
		outStream.close();
	}

	public BigInteger idHash(String ID) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BigInteger hash = BigInteger.ZERO;
		DataInputStream in;

		md.update(ID.getBytes());
		in = new DataInputStream(new ByteArrayInputStream(md.digest()));
		try {
			hash = new BigInteger(ibe.getField().getM() - 1, new Random(in
					.readLong()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hash;
	}

	public int getWritePer() {
		if (size == 0) {
			return 0;
		}
		return 100 * all / size;
	}

	/**
	 * �閧�����o�C�g�񂩂琶������
	 * 
	 * @param key_bits
	 *            ���̒����i�r�b�g�P�ʁj
	 */
	public Key H2Key(FieldElement[] pairingOut) {
		try {
			return new SecretKeySpec(
					hashPairingValue(pairingToBytes(pairingOut)), "AES");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���p����A���S���Y���B
	 */
	private static final String ALGORITHM = "DESede";

	/**
	 * �Í����������Ȃ��܂��B
	 * 
	 * @param inFile
	 * @param outFile
	 * @throws IOException
	 */
	private void encryptFile(final File inFile, final File outFile,
			final Key key) throws IOException {
		final Cipher cipher;
		try {
			final InputStream inStream = new BufferedInputStream(
					new FileInputStream(inFile));

			cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			final OutputStream outStream = new CipherOutputStream(
					new BufferedOutputStream(new FileOutputStream(outFile)),
					cipher);

			copy(inStream, outStream);

			outStream.flush();
			outStream.close();

			inStream.close();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("�A���S���Y���̎擾�Ɏ��s���܂����B:"
					+ e.toString());
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("�p�f�B���O�s���Ŏ��s���܂����B:"
					+ e.toString());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("�閧���s���Ŏ��s���܂����B:" + e.toString());
		}
	}

	/**
	 * �X�g���[�����R�s�[���܂��B
	 * 
	 * @param inStream
	 * @param outStream
	 * @throws IOException
	 */
	private void copy(final InputStream inStream, final OutputStream outStream)
			throws IOException {
		final byte[] byteBuf = new byte[1024];
		for (;;) {
			int length = inStream.read(byteBuf);
			if (length <= 0) {
				break;
			}
			outStream.write(byteBuf, 0, length);
		}
	}

}
