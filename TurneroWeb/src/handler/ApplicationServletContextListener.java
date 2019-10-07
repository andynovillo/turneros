package handler;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		ServletContext servletContext = servletContextEvent.getServletContext();

		servletContext.setInitParameter("facelets.DEVELOPMENT", System.getProperty("devMode"));
		servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD",
				System.getProperty("faceletsRefreshPeriod"));

	}

}
