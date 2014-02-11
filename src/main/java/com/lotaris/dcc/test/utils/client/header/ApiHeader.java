package com.lotaris.dcc.test.utils.client.header;

import javax.ws.rs.client.ClientRequestContext;

/**
 * Define an API header to configure a request
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiHeader {
	/**
	 * Header name
	 */
	private String name;

	/**
	 * Header value
	 */
	private String value;
	
	/**
	 * Flags to keep the state
	 */
	private boolean remove = false;
	private boolean replace = false;
	private boolean keep = false;

	/**
	 * Constructor
	 * 
	 * @param name The name of the header
	 * @param value The value of the header
	 */
	public ApiHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * @return The name of the header
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Header value without computing
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @param remove Set the state of the header to be removed
	 */
	void setRemoved(boolean remove) {
		this.remove = remove;
	}

	/**
	 * @param remove Set the state of the header to be replaced
	 */
	void setReplace(boolean replace) {
		this.replace = replace;
	}
	
	/**
	 * @param remove Set the state of the header to be kept
	 */
	void setKept(boolean keep) {
		this.keep = keep;
	}

	/**
	 * @return True if the header must be removed
	 */
	boolean isRemoved() {
		return remove;
	}

	/**
	 * @return True if the header must be replaced
	 */
	boolean isReplaced() {
		return replace;
	}

	/**
	 * @return True if the header must be kept
	 */
	boolean isKept() {
		return keep;
	}
	
	/**
	 * Build the header value
	 * 
	 * @param requestContext The request context to get info to build richer headers
	 * @return The header value
	 */
	public String computeValue(ClientRequestContext requestContext) {
		return value != null ? value.toString() : null;
	}
}