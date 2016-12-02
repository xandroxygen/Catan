package shared.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Line implements Serializable {
	private String message;
	private String source;

	public Line(String message, String source) {
		this.message = message;
		this.source = source;
	}

	public String getMessage() {
		return message;
	}
	
	public String getSource() {
		return source;
	}
}
