package IBE.cipher.risk.tsukuba.ac.jp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class login
 */
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public login() {
		super();
		// TODO Auto-generated constructor stub
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
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		res.setContentType("text/html; charset=Windows-31J");
		req.setCharacterEncoding("Windows-31J");

		PrintWriter out = res.getWriter();
		out.println("<HTML>");
		out.println("<BODY>");
		out.println(req.getParameter("ID") + "<br>");
		String vehicle[] = req.getParameterValues("pass");
		for (int i = 0; i < vehicle.length; i++) {
			out.println(vehicle[i] + "<br>");
		}

		out.println("</BODY>");
		out.println("</HTML>");

		if (req.getParameter("ID").equals(req.getParameter("pass"))) {

		}

	}

}
