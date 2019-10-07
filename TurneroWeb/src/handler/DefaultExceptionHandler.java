package handler;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

public class DefaultExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;

	@SuppressWarnings("deprecation")
	public DefaultExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> queue = getUnhandledExceptionQueuedEvents().iterator();

		while (queue.hasNext()) {
			ExceptionQueuedEvent item = queue.next();
			ExceptionQueuedEventContext exceptionQueuedEventContext = (ExceptionQueuedEventContext) item.getSource();

			try {

				String URL = getURL();

				if (exceptionQueuedEventContext.getException() instanceof ViewExpiredException) {
					// System.err.println("URL: " + URL);
					// System.err.println("View expired!");
					redirectToTheErrorPage(URL);
				} else {
					Throwable throwable = exceptionQueuedEventContext.getException();
					System.err.println("URL: " + URL);
					System.err.println("Exception: " + throwable.getMessage());
				}

			} finally {
				queue.remove();
			}

		}
	}

	private String getURL() {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

		String contextPath = externalContext.getRequestContextPath();
		String servletPath = externalContext.getRequestServletPath();
		String URL = contextPath + servletPath;

		return URL;

	}

	private void redirectToTheErrorPage(String URL) {

		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(URL);
		} catch (IOException exception) {
			System.err.println("IOException: " + exception.getMessage());
		}

	}

}
