package guardachuva.agitos.server.socialauth.tests;

import guardachuva.agitos.server.utils.Flash;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AgitosServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (req.getRequestURI().endsWith("index.html")) {
			System.out.println("Agitos errors: " + Flash.flash(req).errors());
			resp.setStatus(200);
		} else
			resp.setStatus(404);
	}

	private static final long serialVersionUID = 1L;
}
