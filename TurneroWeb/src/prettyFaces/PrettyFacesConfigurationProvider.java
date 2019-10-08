package prettyFaces;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.PrettyConfigBuilder;
import com.ocpsoft.pretty.faces.config.PrettyConfigurator;
import com.ocpsoft.pretty.faces.config.annotation.PrettyAnnotationHandler;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.spi.ConfigurationProvider;

import entity.auth.Option;
import utility.MethodName;
import utility.Utility;

public class PrettyFacesConfigurationProvider implements ConfigurationProvider {

	@Override
	public PrettyConfig loadConfiguration(ServletContext servletContext) {

		PrettyAnnotationHandler handler = new PrettyAnnotationHandler(null);

		PrettyConfigBuilder configBuilder = new PrettyConfigBuilder();

		handler.build(configBuilder);

		PrettyConfig prettyConfig = configBuilder.build();

		List<UrlMapping> mappings = new ArrayList<>();

		UrlMapping mapping = null;

		try {

			for (Option option : Utility.getOptions()) {

				mapping = new UrlMapping();

				if (option.getPrettyURL() != null) {

					mapping.setId(option.getMappingID() != null ? option.getMappingID() : "ID-" + option.getIdOption());
					mapping.setPattern(option.getPrettyURL());
					mapping.setViewId(option.getOption());
					mappings.add(mapping);

				}

			}

		} catch (Exception exception) {

			Utility.printError(PrettyFacesConfigurationProvider.class.getName(), MethodName.methodName(),
					"Can't load configuration!", exception);

		}

		prettyConfig.setMappings(mappings);

		return prettyConfig;

	}

	public static void reLoad() {

		PrettyConfigurator configurator = new PrettyConfigurator(
				(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext());

		configurator.configure();

	}

}