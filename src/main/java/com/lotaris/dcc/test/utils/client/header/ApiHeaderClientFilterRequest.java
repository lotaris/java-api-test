/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lotaris.dcc.test.utils.client.header;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Api header client filter request to enrich headers for a request at
 * the end when everything is known like URL or method.
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiHeaderClientFilterRequest implements ClientRequestFilter {
	/**
	 * Map containing headers that will be added to all requests. Add new headers to this using the setHeader method.
	 */
	private MultivaluedMap<String, ApiHeader> headers = new MultivaluedHashMap<>();

	/**
	 * Add header for the next request
	 *
	 * @param name Name of the header
	 * @param value Value of the header
	 */
	public void setHeaderForNextRequest(final String name, final String value) {
		setHeaderForNextRequest(new ApiHeader(name, value));
	}

	/**
	 * Add header for the next request
	 *
	 * @param apiHeader The API header
	 */
	public void setHeaderForNextRequest(final ApiHeader apiHeader) {
		if (headers.containsKey(apiHeader.getName())) {
			apiHeader.setReplace(true);
			headers.get(apiHeader.getName()).add(apiHeader);
		}
		else {
			headers.putSingle(apiHeader.getName(), apiHeader);
		}
	}

	/**
	 * Add header for the all requests
	 *
	 * @param name Name of the header
	 * @param value Value of the header
	 */
	public void setHeader(final String name, final String value) {
		setHeader(new ApiHeader(name, value));
	}

	/**
	 * Add header for the all requests
	 *
	 * @param apiHeader The API header
	 */
	public void setHeader(final ApiHeader apiHeader) {
		apiHeader.setKept(true);
		setHeaderForNextRequest(apiHeader);
	}

	/**
	 * Remove header for the next request
	 *
	 * @param name Name of the header
	 */
	public void removeHeaderForNextRequest(String name) {
		if (headers.containsKey(name)) {
			ApiHeader header = new ApiHeader(name, null);
			header.setRemoved(true);
			headers.get(name).add(header);
		}
	}

	/**
	 * Replace header for the next request
	 *
	 * @param name Name of the header
	 */
	public void replaceHeaderForNextRequest(String name, String value) {
		if (headers.containsKey(name)) {
			ApiHeader header = new ApiHeader(name, value);
			header.setReplace(true);
			headers.get(name).add(header);
		}
	}

	@Override
	public void filter(ClientRequestContext requestContext) throws IOException {
		MultivaluedMap<String, ApiHeader> originalHeaders = new MultivaluedHashMap<>();
		MultivaluedMap<String, ApiHeader> realHeaders = new MultivaluedHashMap<>();

		// Build real headers
		for (List<ApiHeader> apiHeaders : headers.values()) {
			for (ApiHeader apiHeader : apiHeaders) {
				// Set a header
				if (!realHeaders.containsKey(apiHeader.getName()) && !apiHeader.isRemoved() && !apiHeader.isReplaced()) {
					realHeaders.putSingle(apiHeader.getName(), apiHeader);
					originalHeaders.putSingle(apiHeader.getName(), apiHeader);
				}
				// Replace a header
				else if (realHeaders.containsKey(apiHeader.getName()) && apiHeader.isReplaced()) {
					realHeaders.remove(apiHeader.getName());
					realHeaders.putSingle(apiHeader.getName(), apiHeader);
				}
				// Remove header
				else if (realHeaders.containsKey(apiHeader.getName()) && apiHeader.isRemoved()) {
					realHeaders.remove(apiHeader.getName());
				}
			}
		}

		// Set the header to the query
		for (List<ApiHeader> apiHeaders : realHeaders.values()) {
			for (ApiHeader apiHeader : apiHeaders) {
				requestContext.getHeaders().add(apiHeader.getName(), apiHeader.computeValue(requestContext));
			}
		}

		headers = originalHeaders;
	}
}