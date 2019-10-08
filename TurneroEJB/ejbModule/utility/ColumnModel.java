package utility;

import java.io.Serializable;

public class ColumnModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String header;
	private String property;
	private String style;
	private Boolean paintable;
	private Boolean rendered;
	private Boolean exportable;

	public ColumnModel(String header, String property, String style, Boolean paintable) {

		this.header = header;
		this.property = property;
		this.style = style;
		this.paintable = paintable;

	}

	public ColumnModel(String header, String property, String style, Boolean paintable, Boolean rendered,
			Boolean exportable) {

		this.header = header;
		this.property = property;
		this.style = style;
		this.paintable = paintable;
		this.rendered = rendered;
		this.exportable = exportable;

	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getHeader() {
		return header;
	}

	public String getProperty() {
		return property;
	}

	public Boolean getPaintable() {
		return paintable;
	}

	public void setPaintable(Boolean paintable) {
		this.paintable = paintable;
	}

	public Boolean getRendered() {
		return rendered;
	}

	public void setRendered(Boolean rendered) {
		this.rendered = rendered;
	}

	public Boolean getExportable() {
		return exportable;
	}

	public void setExportable(Boolean exportable) {
		this.exportable = exportable;
	}

}
