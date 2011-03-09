/**
 * 
 */
package IBE.cipher.risk.tsukuba.ac.jp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

/**
 * @author 張　一凡
 * 
 */
public class PairingCrypto extends JFrame implements ActionListener {

	private static String ID;
	private static String encFileName;
	private static String decFileName;
	private static String keyFileName;
	private static JFrame frame;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 1) {
			url = args[0];
		} else {
			url = "http://localhost:8080/PairingWeb/PKGserver";
		}
		System.out.println(url);
		// 自分自身のインスタンス化
		frame = new PairingCrypto();
		// フレームのセットアップ
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setSize(600, 135);
		frame.setResizable(false);
		frame.setVisible(true); // 可視化（リアライズ）
	}

	private JTextField encFileText;
	private JTextField decFileText;
	private JTextField keyFileText;
	private JTextField idText;
	private JProgressBar encPb;
	private JProgressBar decPb;

	/**
	 * 
	 */
	/**
	 * 
	 */
	public PairingCrypto() {
		super("Pairing Crypto");

		Container con = this.getContentPane();

		JTabbedPane jtp = new JTabbedPane();

		creatTab(jtp);

		con.add(jtp);
	}

	/**
	 * @param jtp
	 */
	private void creatTab(JTabbedPane jtp) {
		JPanel encFile = getPanel();
		JPanel encID = getPanel();
		JPanel decFile = getPanel();
		JPanel keyFile = getPanel();

		encFileText = new JTextField(encFileName);
		encFileText.setActionCommand("encFile");
		encFileText.addActionListener(this);
		JButton encFileSelector = new JButton("参照");
		encFileSelector.setActionCommand("encFile");
		encFileSelector.addActionListener(this);
		encFile.add(new JLabel(" 暗号元ファイル: "), "West");
		encFile.add(encFileText, "Center");
		encFile.add(encFileSelector, "East");

		idText = new JTextField(ID);
		idText.setActionCommand("encrypt");
		encID.add(new JLabel(" 受信者ID(メールアドレス)  "), "West");
		encID.add(idText, "Center");

		decFileText = new JTextField(decFileName);
		JButton decFileSelector = new JButton("参照");
		decFileSelector.setActionCommand("decFile");
		decFileSelector.addActionListener(this);
		decFile.add(new JLabel(" 暗号化ファイル: "), "West");
		decFile.add(decFileText, "Center");
		decFile.add(decFileSelector, "East");

		keyFileText = new JTextField(keyFileName);
		JButton keyFileSelector = new JButton("参照");
		keyFileSelector.setActionCommand("keyFile");
		keyFileSelector.addActionListener(this);
		keyFile.add(new JLabel(" 秘密鍵ファイル: "), "West");
		keyFile.add(keyFileText, "Center");
		keyFile.add(keyFileSelector, "East");

		JButton encButton = new JButton("暗号化");
		encButton.setActionCommand("encrypt");
		encButton.addActionListener(this);
		JButton mailButton = new JButton("メール");
		mailButton.setActionCommand("mail");
		mailButton.addActionListener(this);

		encPb = new JProgressBar(1, 100);

		JPanel encButtons = new JPanel();
		encButtons.setLayout(new GridBagLayout());
		encButtons.add(encButton);
		encButtons.add(mailButton);
		encButtons.add(encPb);

		JPanel encTab = new JPanel();
		encTab.setLayout(new BorderLayout());
		encTab.add(encFile, "North");
		encTab.add(encID, "Center");
		encTab.add(encButtons, "South");

		decPb = new JProgressBar(1, 100);

		JButton decButton = new JButton("復号");
		decButton.setActionCommand("decrypt");
		decButton.addActionListener(this);

		JPanel decButtons = new JPanel();
		decButtons.setLayout(new GridBagLayout());
		decButtons.add(decButton);
		decButtons.add(decPb);

		JPanel decTab = new JPanel();
		decTab.setLayout(new BorderLayout());
		decTab.add(decFile, "North");
		decTab.add(keyFile, "Center");
		decTab.add(decButtons, "South");

		jtp.addTab("暗号化", encTab);
		jtp.addTab("復号", decTab);
	}

	private static JPanel getPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		return panel;
	}

	private JFileChooser chooser = new JFileChooser();
	private dataCtrl writer;
	private static String url;

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand() == "encFile") { // Openボタンが押されたときの処理
			int returnVal = chooser.showOpenDialog(this);
			try {
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// System.out.println(chooser.getSelectedFile());
					encFileName = chooser.getSelectedFile().toString();
					encFileText.setText(encFileName);
				}
			} catch (Exception ex) {
			}
		} else if (ae.getActionCommand() == "decFile") {
			int returnVal = chooser.showOpenDialog(this);
			try {
				if (returnVal == JFileChooser.APPROVE_OPTION) { // ＯＫボタンでJFileChooserが閉じられた後の処理
					// System.out.println(chooser.getSelectedFile());
					decFileName = chooser.getSelectedFile().toString();
					decFileText.setText(decFileName);
				}
			} catch (Exception ex) {
			}
		} else if (ae.getActionCommand() == "keyFile") {
			int returnVal = chooser.showOpenDialog(this);
			try {
				if (returnVal == JFileChooser.APPROVE_OPTION) { // ＯＫボタンでJFileChooserが閉じられた後の処理
					// System.out.println(chooser.getSelectedFile());
					keyFileName = chooser.getSelectedFile().toString();
					keyFileText.setText(keyFileName);
				}
			} catch (Exception ex) {
			}
		} else if (ae.getActionCommand() == "encrypt") {
			ID = idText.getText();
			final Encrypt enc = new Encrypt(url);
			writer = enc.getWriter();

			Thread pbThread = new Thread(new Runnable() {
				public void run() {
					encPbUpdate();
				}
			});
			pbThread.start();

			Thread encThread = new Thread(new Runnable() {
				public void run() {
					encrypt(enc);
					messageBox("暗号化が完了しました。");
				}
			});
			encThread.start();

		} else if (ae.getActionCommand() == "decrypt") {
			final Decrypt dec = new Decrypt(url);
			writer = dec.getWriter();
			Thread new_thread = new Thread(new Runnable() {
				public void run() {
					decPbUpdate();
				}
			});
			new_thread.start();

			Thread decThread = new Thread(new Runnable() {
				public void run() {
					decrypt(dec);
					messageBox("復号が完了しました。");
				}
			});
			decThread.start();

		} else if (ae.getActionCommand() == "mail") {
			ID = idText.getText();
			final Encrypt enc = new Encrypt(url);
			writer = enc.getWriter();

			Thread pbThread = new Thread(new Runnable() {
				public void run() {
					encPbUpdate();
				}
			});
			pbThread.start();

			Thread encThread = new Thread(new Runnable() {
				public void run() {
					encrypt(enc);
					mail();
				}
			});
			encThread.start();
		}
	}

	public void encrypt(Encrypt enc) {
		try {
			enc.encrypt(ID, encFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void decrypt(Decrypt dec) {
		try {
			dec.decrypt(decFileName, keyFileName);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void encPbUpdate() {
		while (writer.getWritePer() < 100) {
			encPb.setValue(writer.getWritePer() + 1);
			encPb.repaint();
		}
	}

	public void decPbUpdate() {
		while (writer.getWritePer() < 100) {
			decPb.setValue(writer.getWritePer() + 1);
			decPb.repaint();
		}
	}

	public void messageBox(String message) {
		JOptionPane.showMessageDialog(null, message, "Pairing Crypto message",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public void mail() {
		Desktop dsk = Desktop.getDesktop();
		try {
			String body = "mailto:" + ID + "?attach=";
			body = body + URLEncoder.encode(encFileText.getText());
			// body = body + encFileText.getText() ;
			System.out.println(body);
			URI uri = new URI(body);
			dsk.mail(uri);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
