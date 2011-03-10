package pairing.cipher.risk.tsukuba.ac.jp;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author ’£ ˆê–}
 * 
 */
public class Messages {
	private static final String BUNDLE_NAME = "tyouiifan.cipher.risk.tsukuba.ac.jp.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * @param key
	 * @return message
	 */
	public static String getString(final String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (final MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	private Messages() {
	}
}
