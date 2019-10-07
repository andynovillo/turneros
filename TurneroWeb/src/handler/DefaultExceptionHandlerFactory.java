package handler;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class DefaultExceptionHandlerFactory extends ExceptionHandlerFactory {
	private ExceptionHandlerFactory parent;

	public DefaultExceptionHandlerFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;
	}

	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler exceptionHandler = parent.getExceptionHandler();
		exceptionHandler = new DefaultExceptionHandler(exceptionHandler);
		return exceptionHandler;
	}
}