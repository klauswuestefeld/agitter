package agitter.server.socialauth.tests;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoogleAuthServlet extends HttpServlet {


	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//"https://www.google.com/accounts/o8/ud?openid.ax.type.postcode=http%3A%2F%2Faxschema.org%2Fcontact%2FpostalCode%2Fhome&openid.ax.type.country=http%3A%2F%2Faxschema.org%2Fcontact%2Fcountry%2Fhome&openid.ax.mode=fetch_request&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.ax.type.firstname=http%3A%2F%2Faxschema.org%2FnamePerson%2Ffirst&openid.ax.required=nickname%2Cemail%2Cdob%2Cgender%2Clastname%2Clanguage%2Cfirstname%2Cpostcode%2Cfullname%2Ccountry&openid.ax.type.lastname=http%3A%2F%2Faxschema.org%2FnamePerson%2Flast&openid.ns.sreg=http%3A%2F%2Fopenid.net%2Fextensions%2Fsreg%2F1.1&openid.realm=http%3A%2F%2Fwww.vagaloom.com%2Fagitosweb&openid.return_to=http%3A%2F%2Fwww.vagaloom.com%2Fagitosweb%2Fsocial_auth%3Fstatus%3Dsuccess&openid.ax.type.gender=http%3A%2F%2Faxschema.org%2Fperson%2Fgender&openid.ns.oauth=http%3A%2F%2Fspecs.openid.net%2Fextensions%2Foauth%2F1.0&openid.oauth.consumer=www.vagaloom.com&openid.assoc_handle=AOQobUdmOVKm3dr91QUUfb5XXDakBhtA6Y4NVAXcKZL_Tn79EDZA0AM7&openid.ax.type.email=http%3A%2F%2Faxschema.org%2Fcontact%2Femail&openid.oauth.scope=http%3A%2F%2Fwww.google.com%2Fm8%2Ffeeds%2F&openid.sreg.optional=nickname%2Cemail%2Cdob%2Cgender%2Clanguage%2Cpostcode%2Cfullname%2Ccountry&openid.trust_root=http%3A%2F%2Fwww.vagaloom.com&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&openid.ax.type.language=http%3A%2F%2Faxschema.org%2Fpref%2Flanguage&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.ns.ax=http%3A%2F%2Fopenid.net%2Fsrv%2Fax%2F1.0&openid.ax.type.nickname=http%3A%2F%2Faxschema.org%2FnamePerson%2Ffriendly&openid.mode=checkid_setup&openid.ax.type.dob=http%3A%2F%2Faxschema.org%2FbirthDate&openid.ax.type.fullname=http%3A%2F%2Faxschema.org%2FnamePerson";
		redirect(resp, req.getParameter("openid.return_to").toString());
	}

	private void redirect(HttpServletResponse response, String redirect) {
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", redirect);
	}

	private static final long serialVersionUID = 1L;
}
