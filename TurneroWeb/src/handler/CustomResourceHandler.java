package handler;

import java.util.Calendar;
import java.util.Date;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.application.ResourceWrapper;

public class CustomResourceHandler extends ResourceHandlerWrapper {

	private final static Date deployTime = Calendar.getInstance().getTime();

	private ResourceHandler wrapped;

	public CustomResourceHandler(ResourceHandler wrapped) {

		this.wrapped = wrapped;
	}

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

	@Override
	public Resource createResource(String resourceName) {
		return createResource(resourceName, null, null);
	}

	@Override
	public Resource createResource(String resourceName, String libraryName) {
		return createResource(resourceName, libraryName, null);
	}

	@Override
	public Resource createResource(String resourceName, String libraryName, String contentType) {
		final Resource resource = super.createResource(resourceName, libraryName, contentType);

		if (resource == null) {
			return null;
		}

		return new ResourceWrapper() {

			@Override
			public String getRequestPath() {
				String requestPath = super.getRequestPath();
				if (!requestPath.contains("?"))
					return super.getRequestPath() + String.format("?cv=%s", deployTime.getTime());
				else
					return super.getRequestPath() + String.format("&cv=%s", deployTime.getTime());
			}

			@Override // Necessary because this is missing in ResourceWrapper
						// (will be fixed in JSF 2.2).
			public String getResourceName() {
				return resource.getResourceName();
			}

			@Override // Necessary because this is missing in ResourceWrapper
						// (will be fixed in JSF 2.2).
			public String getLibraryName() {
				return resource.getLibraryName();
			}

			@Override // Necessary because this is missing in ResourceWrapper
						// (will be fixed in JSF 2.2).
			public String getContentType() {
				return resource.getContentType();
			}

			@Override
			public Resource getWrapped() {
				return resource;
			}
		};
	}

//	@Override
//	public void handleResourceRequest(FacesContext context) throws IOException {
//
//		System.err.println("Can't found: " + context.getExternalContext().getRequestServletPath());
//
//		super.handleResourceRequest(context);
//
//	}

}
