package filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import entity.auth.SystemUser;
import utility.Constant;
import utility.Utility;
import web.utility.JSFUtility;

/**
 *
 * @author BryanV
 */

public class LoginFilter implements Filter {

	public static String indexPath = "/inicio.jsf?faces-redirect=true";
	public static String notAuthorizedPath = "/noAutorizado.jsf?faces-redirect=true";
	public static String E500 = "/error/500.jsf?faces-redirect=true";
	public static String INDEX = "/inicio.jsf?faces-redirect=true";

	public LoginFilter() {
	}

	/**
	 *
	 * @param request  The servlet request we are processing
	 * @param response The servlet response we are creating
	 * @param chain    The filter chain we are processing
	 *
	 * @exception IOException      if an input/output error occurs
	 * @exception ServletException if a servlet error occurs
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		try {

			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;

			HttpSession session = httpServletRequest.getSession(false);

			String indexURL = httpServletRequest.getContextPath() + indexPath;
			String notAuthorizedURL = httpServletRequest.getContextPath() + notAuthorizedPath;

			boolean ajaxRequest = isAJAXRequest(httpServletRequest);

			if (session != null) {

				SystemUser systemUser = (SystemUser) session.getAttribute(JSFUtility.USER_IN_SESSION);

				boolean loggedIn = (session != null) && (systemUser != null);
				boolean loginRequest = httpServletRequest.getRequestURI().equals(indexURL);
				boolean resourceRequest = httpServletRequest.getRequestURI()
						.startsWith(httpServletRequest.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");

				if (loggedIn || loginRequest || resourceRequest) {
					if (!resourceRequest) { // Prevent browser from caching
											// restricted resources.
						httpServletResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP
																												// 1.1.
						httpServletResponse.setHeader("Pragma", "no-cache"); // HTTP
																				// 1.0.
						httpServletResponse.setDateHeader("Expires", 0); // Proxies.
					}

					Set<String> pageList = (Set<String>) session.getAttribute("pagesInSession");

					String url = httpServletRequest.getServletPath();

					if (checkURL(pageList, url)) {

						boolean doChain = false;
						boolean result = false;

						if (systemUser != null) {

							Date lastPasswordChangeDateTime = systemUser.getLastPasswordChangeDateTime();

							if (lastPasswordChangeDateTime != null) {
								LocalDate lastChange = Utility.toLocalDate(lastPasswordChangeDateTime);
								LocalDate nextChange = lastChange.plus(Constant.PASSWORD_EXPIRATION_TIME,
										java.time.temporal.ChronoUnit.MONTHS);
								if (LocalDate.now().compareTo(nextChange) >= 0) {
									result = false;
									httpServletResponse.sendRedirect(
											httpServletRequest.getContextPath() + Constant.PAGE_CHANGE_PASSWORD);
								} else
									result = true;
							} else {
								result = false;
								httpServletResponse.sendRedirect(
										httpServletRequest.getContextPath() + Constant.PAGE_CHANGE_PASSWORD);
							}

							doChain = result;

							if (doChain) {

								if (systemUser.getMail() == null || systemUser.getOtherMail() == null) {
									result = false;
									httpServletResponse.sendRedirect(
											httpServletRequest.getContextPath() + Constant.PAGE_CHANGE_USER_DATA);

								} else
									result = true;

							}

							doChain = doChain && result;

						}

						if (doChain) {

							chain.doFilter(request, response); // So, just
							// continue
							// request.
						} else {

							// Nothing to do.

						}

					} else {
						httpServletResponse.sendRedirect(notAuthorizedURL);
					}

				} else if (ajaxRequest) {
					processAJAXRequest(httpServletRequest, httpServletResponse);
				} else {
					httpServletResponse.sendRedirect(indexURL); // So, just
					// perform
					// standard
					// synchronous
					// redirect.
				}

			} else {

				if (ajaxRequest) {
					processAJAXRequest(httpServletRequest, httpServletResponse);
				} else {
					httpServletResponse.sendRedirect(indexURL); // So, just
					// perform
					// standard
					// synchronous
					// redirect.
				}

			}

		} catch (Exception exception) {

			System.out.println("Error caught in filter!");
			System.out.println("Exception: " + exception.getMessage());

		}

	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public boolean checkURL(Set<String> pageList, String page) {
		if (pageList != null && page != null) {
			Iterator<String> iterator = pageList.iterator();
			while (iterator.hasNext()) {
				String temporalPage = iterator.next();
				if (page.equals(temporalPage))
					return true;
			}
		}
		return false;
	}

	private boolean isAJAXRequest(HttpServletRequest request) {
		boolean check = false;
		String facesRequest = request.getHeader("Faces-Request");
		if (facesRequest != null && facesRequest.equals("partial/ajax")) {
			check = true;
		}
		return check;
	}

	private void processAJAXRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		String redirectURL = httpServletResponse.encodeRedirectURL(httpServletRequest.getContextPath() + indexPath);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"")
				.append(redirectURL).append("\"></redirect></partial-response>");
		httpServletResponse.setHeader("Cache-Control", "no-cache");
		httpServletResponse.setCharacterEncoding("UTF-8");
		httpServletResponse.setContentType("text/xml");
		PrintWriter printWriter = httpServletResponse.getWriter();
		printWriter.println(stringBuilder.toString());
		printWriter.flush();
		return;
	}

}
