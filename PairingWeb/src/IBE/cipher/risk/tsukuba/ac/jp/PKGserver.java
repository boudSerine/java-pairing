package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pairing.cipher.risk.tsukuba.ac.jp.EtaTPairing;

/**
 * Servlet implementation class PNGserver
 */
public class PKGserver extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final IBE ibe;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PKGserver() {
		super();

		ibe = new IBE(241, 70, 1, 1, true);

		ibe.setup();

		ibe.save("condition.txt");

		ibe.setPairing(new EtaTPairing(ibe.getField(), 1));
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// response.setContentType("text/html");
		// response.getWriter().println(ibe.getM());
		// response.getWriter().println(ibe.getK());
		// response.getWriter().println(ibe.getAE());
		// response.getWriter().println(ibe.getBE());
		// response.getWriter().println(ibe.isEllipticPairing());
		// response.getWriter().println(ibe.getP().getAffineX().toString());
		// response.getWriter().println(ibe.getP().getAffineY().toString());
		// response.getWriter().println(ibe.getSP().getAffineX().toString());
		// response.getWriter().println(ibe.getSP().getAffineY().toString());

		request.setAttribute("m", ibe.getM());
		request.setAttribute("k", ibe.getK());
		request.setAttribute("ae", ibe.getAE());
		request.setAttribute("be", ibe.getBE());
		request.setAttribute("elliptic", ibe.isEllipticPairing());
		request.setAttribute("Px", ibe.getP().getAffineX());
		request.setAttribute("Py", ibe.getP().getAffineY());
		request.setAttribute("sPx", ibe.getSP().getAffineX());
		request.setAttribute("sPy", ibe.getSP().getAffineY());
		RequestDispatcher rd = request.getRequestDispatcher("IBEcondition.jsp");
		rd.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.getWriter().println(ibe.getM());
		response.getWriter().println(ibe.getK());
		response.getWriter().println(ibe.getAE());
		response.getWriter().println(ibe.getBE());
		response.getWriter().println(ibe.isEllipticPairing());
		response.getWriter().println(ibe.getP().getAffineX().toString());
		response.getWriter().println(ibe.getP().getAffineY().toString());
		response.getWriter().println(ibe.getSP().getAffineX().toString());
		response.getWriter().println(ibe.getSP().getAffineY().toString());
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
