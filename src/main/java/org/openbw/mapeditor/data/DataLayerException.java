package org.openbw.mapeditor.data;

import java.io.IOException;

public class DataLayerException extends IOException {

	private static final long serialVersionUID = -2835995868720164590L;

	public DataLayerException(String message) {
        super(message);
    }
	
	public DataLayerException(String message, Throwable cause) {
        super(message, cause);
    }
}
