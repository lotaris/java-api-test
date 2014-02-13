package com.lotaris.dcc.test.utils.client.header;

import com.lotaris.api.test.client.ApiTestRequest;

/**
 * HTTP request header with meta information to indicate whether it concerns only the next request
 * or should be kept.
 *
 * @see ApiHeadersManager
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiHeader {

	/**
	 * Header name.
	 */
	private String name;
	/**
	 * Header value.
	 */
	private String value;
	/**
	 * Whether the header should be removed for the next request.
	 */
	private boolean remove = false;
	/**
	 * Whether the header should be replaced for the next request.
	 */
	private boolean replace = false;
	/**
	 * Whether the header should be kept for all requests.
	 */
	private boolean keep = false;

	/**
	 * Constructs a new header.
	 *
	 * @param name header name
	 * @param value header value
	 */
	public ApiHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * Returns the name of the header.
	 *
	 * @return the header name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the value of the header.
	 *
	 * @return the header value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets whether the header should be removed for the next request.
	 *
	 * @param remove true to remove the header
	 */
	void setRemoved(boolean remove) {
		this.remove = remove;
	}

	/**
	 * Sets whether the header should be replaced for the next request.
	 *
	 * @param replace true to replace the header
	 */
	void setReplace(boolean replace) {
		this.replace = replace;
	}

	/**
	 * Sets whether the header should be kept for all requests.
	 *
	 * @param keep true to keep the header
	 */
	void setKept(boolean keep) {
		this.keep = keep;
	}

	/**
	 * @return true if the header should be removed for the next request
	 */
	boolean isRemoved() {
		return remove;
	}

	/**
	 * @return true if the header should be replaced for the next request
	 */
	boolean isReplaced() {
		return replace;
	}

	/**
	 * @return true if the header should be kept for all requests
	 */
	boolean isKept() {
		return keep;
	}

	/**
	 * Returns the value of the header. The API request is provided for headers whose value may
	 * depend on request information.
	 *
	 * @param request the API request
	 * @return the header value
	 */
	public String computeValue(ApiTestRequest request) {
		return value != null ? value.toString() : null;
	}
}