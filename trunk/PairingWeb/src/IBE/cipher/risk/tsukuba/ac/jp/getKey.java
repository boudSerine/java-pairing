package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pairing.cipher.risk.tsukuba.ac.jp.ECPoint;
import pairing.cipher.risk.tsukuba.ac.jp.FieldElement;

/**
 * Servlet implementation class getKey
 */
public class getKey extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IBE ibe;

	private ECPoint Q;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public getKey() {
		super();

		// befor maptopoint complete
		final FieldElement x1 = new FieldElement(241,
				new BigInteger(
						"632339556305180632117439061020992460234085159493166733110190446375604882"));
		final FieldElement y1 = new FieldElement(241,
				new BigInteger(
						"2343246538743628208776751585690708384614457342502342829230656262040574601"));

		Q = new ECPoint(x1, y1);

		String fileName = "condition.txt";

		try {
			FileReader in = new FileReader(fileName);
			BufferedReader br = new BufferedReader(in);
			String line;
			line = br.readLine();
			int m = Integer.parseInt(line);
			line = br.readLine();
			int k = Integer.parseInt(line);
			line = br.readLine();
			int aE = Integer.parseInt(line);
			line = br.readLine();
			int bE = Integer.parseInt(line);
			line = br.readLine();
			boolean elliptic = new Boolean(line);

			line = br.readLine();
			BigInteger skey = new BigInteger(line);

			ibe = new IBE(m, k, aE, bE, elliptic);

			ibe.setSkey(skey);

			System.out.println("server sKey");
			System.out.println(skey);

			System.out.println("public key P");
			line = br.readLine();
			System.out.println(line);
			FieldElement xtmp = x1.getZERO();
			xtmp.changeToBigInteger(new BigInteger(line));
			line = br.readLine();
			System.out.println(line);
			FieldElement ytmp = x1.getZERO();
			ytmp.changeToBigInteger(new BigInteger(line));
			ibe.setP(new ECPoint(xtmp, ytmp));

			ibe.setP(ibe.getCurve().mul(skey, ibe.getP()));
			System.out.println("sP in getKey");
			ibe.getP().print();

			br.close();
			in.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ID = request.getParameter("ID");
		if (isMail(ID)) {
			dataCtrl data = new dataCtrl(ibe, ibe.getField().getM() / 2);

//			System.out.println("ID = " + ID);
//			System.out.print("ID hash = ");
//			System.out.println(data.idHash(ID));
//			System.out.println(ibe.getSkey());

			ECPoint sQid = ibe.getCurve().mul(ibe.getSkey(),
					ecMul(Q, data.idHash(ID)));
//			System.out.println("sQid");
//			sQid.print();

//			ECPoint Qid = ecMul(Q, data.idHash(ID));
//			System.out.println("Qid");
//			Qid.print();

			String fileName = ID + ".key";
			try {
				FileWriter out = new FileWriter(fileName);
				BufferedWriter wr = new BufferedWriter(out);
				wr.write(sQid.getAffineX().toBigInteger().toString() + "\n");
				wr.write(sQid.getAffineY().toBigInteger().toString() + "\n");
				wr.close();
				out.close();
			} catch (IOException e) {
				System.out.println(e);
			}
			mail(ID, fileName);
		}
		request.setAttribute("ID", ID);
		request.setAttribute("send", isMail(ID));
		RequestDispatcher rd = request.getRequestDispatcher("IBEkeysend.jsp");
		rd.forward(request, response);

	}

	private boolean isMail(String address) {
		int i;
		if ((i = address.indexOf('@')) > 1) {
			if (address.indexOf('.') > i) {
				return true;
			}
		}
		return false;
	}

	public ECPoint ecMul(ECPoint P, BigInteger mul) {
		ECPoint mP = null;
		if (ibe.isEllipticPairing()) {
			mP = ibe.getCurve().mul(mul, P);
		} else {
			mP = ibe.getPairing().hyperMul(P);
		}
		return mP;
	}

	public void mail(String ID, String file) {
		String from = "pairing_crypto@yahoo.co.jp";
		pop();
		// pass cipherrisk
		String host = "smtp.mail.yahoo.co.jp";
		String filename = file;

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.host", host);
		props.put("mail.from", from);

		Session session = Session.getInstance(props);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] address = InternetAddress.parse(ID);
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("[pairing crypto] your ID key", "ISO-2022-JP");
			msg.setSentDate(new Date());

			/* 添付ファイルの処理 */
			MimeBodyPart mbp1 = new MimeBodyPart();
			String text = ID + "様\n";
			text += "このたびはPairing暗号をご利用いただきありがとうございます。\n";
			text += ID + "様の秘密鍵を当サービスの提供ページにて申請を受け付けましたので\n";
			text += "ご配信させていただいております。\n";
			text += "当サービスに心当たりのない方はこちらのメールの削除をお願いいたします。\n";
			text += "\n　Pairing　Crypto from tsukuba univ\n";

			mbp1.setText(text, "ISO-2022-JP");

			MimeBodyPart mbp2 = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(filename);
			mbp2.setDataHandler(new DataHandler(fds));
			mbp2.setFileName(MimeUtility.encodeWord(fds.getName()));

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			mp.addBodyPart(mbp2);

			msg.setContent(mp);

			Transport.send(msg);
		} catch (MessagingException mex) {
			System.out.println("\n--Exception handling in msgsendsample.java");
			mex.printStackTrace();
		} catch (java.io.UnsupportedEncodingException uex) {
		}
	}

	public void pop() {
		String host = "pop.mail.yahoo.co.jp"; // ホストアドレス
		String user = "pairing_crypto"; // アカウント
		String password = "cipherrisk"; // パスワード

		// 接続します
		Session session = Session.getDefaultInstance(System.getProperties(),
				null);
		Store store;
		try {
			store = session.getStore("pop3");
			store.connect(host, -1, user, password);
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
