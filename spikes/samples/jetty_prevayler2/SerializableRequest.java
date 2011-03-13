package samples.jetty_prevayler2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

public class SerializableRequest implements HttpServletRequest, Serializable {

	private static final long serialVersionUID = 1L;

	
	private final String _requestURI;
	private final String _method;

	
	public SerializableRequest(HttpServletRequest request) {
		_requestURI = request.getRequestURI();
		_method = request.getMethod();
	}

	
	public AsyncContext getAsyncContext() {
		throw new Error("Not implemented yet: ServletRequest.getAsyncContext");
	}

	@Override
	public Object getAttribute(String arg0) {
		throw new Error("Not implemented yet: ServletRequest.getAttribute");
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		throw new Error("Not implemented yet: ServletRequest.getAttributeNames");
	}

	@Override
	public String getCharacterEncoding() {
		throw new Error("Not implemented yet: ServletRequest.getCharacterEncoding");
	}

	@Override
	public int getContentLength() {
		throw new Error("Not implemented yet: ServletRequest.getContentLength");
	}

	@Override
	public String getContentType() {
		throw new Error("Not implemented yet: ServletRequest.getContentType");
	}

	public DispatcherType getDispatcherType() {
		throw new Error("Not implemented yet: ServletRequest.getDispatcherType");
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		throw new Error("Not implemented yet: ServletRequest.getInputStream");
	}

	@Override
	public String getLocalAddr() {
		throw new Error("Not implemented yet: ServletRequest.getLocalAddr");
	}

	@Override
	public String getLocalName() {
		throw new Error("Not implemented yet: ServletRequest.getLocalName");
	}

	@Override
	public int getLocalPort() {
		throw new Error("Not implemented yet: ServletRequest.getLocalPort");
	}

	@Override
	public Locale getLocale() {
		throw new Error("Not implemented yet: ServletRequest.getLocale");
	}

	@Override
	public Enumeration<Locale> getLocales() {
		throw new Error("Not implemented yet: ServletRequest.getLocales");
	}

	@Override
	public String getParameter(String arg0) {
		throw new Error("Not implemented yet: ServletRequest.getParameter");
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		throw new Error("Not implemented yet: ServletRequest.getParameterMap");
	}

	@Override
	public Enumeration<String> getParameterNames() {
		throw new Error("Not implemented yet: ServletRequest.getParameterNames");
	}

	@Override
	public String[] getParameterValues(String arg0) {
		throw new Error("Not implemented yet: ServletRequest.getParameterValues");
	}

	@Override
	public String getProtocol() {
		throw new Error("Not implemented yet: ServletRequest.getProtocol");
	}

	@Override
	public BufferedReader getReader() throws IOException {
		throw new Error("Not implemented yet: ServletRequest.getReader");
	}

	@Override
	public String getRealPath(String arg0) {
		throw new Error("Not implemented yet: ServletRequest.getRealPath");
	}

	@Override
	public String getRemoteAddr() {
		throw new Error("Not implemented yet: ServletRequest.getRemoteAddr");
	}

	@Override
	public String getRemoteHost() {
		throw new Error("Not implemented yet: ServletRequest.getRemoteHost");
	}

	@Override
	public int getRemotePort() {
		throw new Error("Not implemented yet: ServletRequest.getRemotePort");
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		throw new Error("Not implemented yet: ServletRequest.getRequestDispatcher");
	}

	@Override
	public String getScheme() {
		throw new Error("Not implemented yet: ServletRequest.getScheme");
	}

	@Override
	public String getServerName() {
		throw new Error("Not implemented yet: ServletRequest.getServerName");
	}

	@Override
	public int getServerPort() {
		throw new Error("Not implemented yet: ServletRequest.getServerPort");
	}

	public ServletContext getServletContext() {
		throw new Error("Not implemented yet: ServletRequest.getServletContext");
	}

	public boolean isAsyncStarted() {
		throw new Error("Not implemented yet: ServletRequest.isAsyncStarted");
	}

	public boolean isAsyncSupported() {
		throw new Error("Not implemented yet: ServletRequest.isAsyncSupported");
	}

	@Override
	public boolean isSecure() {
		throw new Error("Not implemented yet: ServletRequest.isSecure");
	}

	@Override
	public void removeAttribute(String arg0) {
		throw new Error("Not implemented yet: ServletRequest.removeAttribute");
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		if (arg0.equals("javax.servlet.error.servlet_name")) {
			return;
		} 
		System.out.println(arg0);
		System.out.println(arg1);
		throw new Error("Not implemented yet: ServletRequest.setAttribute");
	}

	@Override
	public void setCharacterEncoding(String arg0)
			throws UnsupportedEncodingException {
		throw new Error("Not implemented yet: ServletRequest.setCharacterEncoding");
	}

	public AsyncContext startAsync() throws IllegalStateException {
		throw new Error("Not implemented yet: ServletRequest.startAsync");
	}

	public AsyncContext startAsync(ServletRequest arg0, ServletResponse arg1)
			throws IllegalStateException {
		throw new Error("Not implemented yet: ServletRequest.startAsync");
	}

	public boolean authenticate(HttpServletResponse arg0) throws IOException,
			ServletException {
		throw new Error("Not implemented yet: HttpServletRequest.authenticate");
	}

	@Override
	public String getAuthType() {
		throw new Error("Not implemented yet: HttpServletRequest.getAuthType");
	}

	@Override
	public String getContextPath() {
		throw new Error("Not implemented yet: HttpServletRequest.getContextPath");
	}

	@Override
	public Cookie[] getCookies() {
		throw new Error("Not implemented yet: HttpServletRequest.getCookies");
	}

	@Override
	public long getDateHeader(String arg0) {
		throw new Error("Not implemented yet: HttpServletRequest.getDateHeader");
	}

	@Override
	public String getHeader(String arg0) {
		throw new Error("Not implemented yet: HttpServletRequest.getHeader");
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		throw new Error("Not implemented yet: HttpServletRequest.getHeaderNames");
	}

	@Override
	public Enumeration<String> getHeaders(String arg0) {
		throw new Error("Not implemented yet: HttpServletRequest.getHeaders");
	}

	@Override
	public int getIntHeader(String arg0) {
		throw new Error("Not implemented yet: HttpServletRequest.getIntHeader");
	}

	@Override
	public String getMethod() {
		return _method;
	}

	public Part getPart(String arg0) throws IOException, ServletException {
		throw new Error("Not implemented yet: HttpServletRequest.getPart");
	}

	public Collection<Part> getParts() throws IOException, ServletException {
		throw new Error("Not implemented yet: HttpServletRequest.getParts");
	}

	@Override
	public String getPathInfo() {
		throw new Error("Not implemented yet: HttpServletRequest.getPathInfo");
	}

	@Override
	public String getPathTranslated() {
		throw new Error("Not implemented yet: HttpServletRequest.getPathTranslated");
	}

	@Override
	public String getQueryString() {
		throw new Error("Not implemented yet: HttpServletRequest.getQueryString");
	}

	@Override
	public String getRemoteUser() {
		throw new Error("Not implemented yet: HttpServletRequest.getRemoteUser");
	}

	@Override
	public String getRequestURI() {
		return _requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		throw new Error("Not implemented yet: HttpServletRequest.getRequestURL");
	}

	@Override
	public String getRequestedSessionId() {
		throw new Error("Not implemented yet: HttpServletRequest.getRequestedSessionId");
	}

	@Override
	public String getServletPath() {
		throw new Error("Not implemented yet: HttpServletRequest.getServletPath");
	}

	@Override
	public HttpSession getSession() {
		throw new Error("Not implemented yet: HttpServletRequest.getSession");
	}

	@Override
	public HttpSession getSession(boolean arg0) {
		throw new Error("Not implemented yet: HttpServletRequest.getSession");
	}

	@Override
	public Principal getUserPrincipal() {
		throw new Error("Not implemented yet: HttpServletRequest.getUserPrincipal");
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		throw new Error("Not implemented yet: HttpServletRequest.isRequestedSessionIdFromCookie");
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		throw new Error("Not implemented yet: HttpServletRequest.isRequestedSessionIdFromURL");
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		throw new Error("Not implemented yet: HttpServletRequest.isRequestedSessionIdFromUrl");
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		throw new Error("Not implemented yet: HttpServletRequest.isRequestedSessionIdValid");
	}

	@Override
	public boolean isUserInRole(String arg0) {
		throw new Error("Not implemented yet: HttpServletRequest.isUserInRole");
	}

	public void login(String arg0, String arg1) throws ServletException {
		throw new Error("Not implemented yet: HttpServletRequest.login");
	}

	public void logout() throws ServletException {
		throw new Error("Not implemented yet: HttpServletRequest.logout");
	}

}
