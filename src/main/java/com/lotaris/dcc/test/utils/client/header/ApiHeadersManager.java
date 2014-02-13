package com.lotaris.dcc.test.utils.client.header;

import com.lotaris.api.test.client.ApiTestRequest;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Manager of API request headers that allows setting headers for all requests or only for the next
 * request.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @author Simon Oulevay <simon.oulevay@lotaris.com>
 */
public class ApiHeadersManager {

	/**
	 * Headers that will be added to all requests.
	 */
	private MultivaluedMap<String, ApiHeader> headers = new MultivaluedHashMap<>();

	/**
	 * Sets a header for the next request.
	 *
	 * @param name header name
	 * @param value header value
	 */
	public void setHeaderForNextRequest(final String name, final String value) {
		setHeaderForNextRequest(new ApiHeader(name, value));
	}

	/**
	 * Sets a header for the next request.
	 *
	 * @param header the header
	 */
	public void setHeaderForNextRequest(final ApiHeader header) {
		if (headers.containsKey(header.getName())) {
			header.setReplace(true);
			headers.get(header.getName()).add(header);
		} else {
			headers.putSingle(header.getName(), header);
		}
	}

	/**
	 * Sets a header for all requests.
	 *
	 * @param name header name
	 * @param value header value
	 */
	public void setHeader(final String name, final String value) {
		setHeader(new ApiHeader(name, value));
	}

	/**
	 * Sets a header for all requests.
	 *
	 * @param header the header
	 */
	public void setHeader(final ApiHeader header) {
		header.setKept(true);
		setHeaderForNextRequest(header);
	}

	/**
	 * Removes a header for the next request.
	 *
	 * @param name header name
	 */
	public void removeHeaderForNextRequest(String name) {
		if (headers.containsKey(name)) {
			ApiHeader header = new ApiHeader(name, null);
			header.setRemoved(true);
			headers.get(name).add(header);
		}
	}

	/**
	 * Replaces a header for the next request.
	 *
	 * @param name header name
	 */
	public void replaceHeaderForNextRequest(String name, String value) {
		if (headers.containsKey(name)) {
			ApiHeader header = new ApiHeader(name, value);
			header.setReplace(true);
			headers.get(name).add(header);
		}
	}

	/**
	 * Configures headers for the next request.
	 *
	 * @param headerConfiguration the configuration to apply
	 */
	public void configureHeaderForNextRequest(IApiHeaderConfiguration headerConfiguration) {
		headerConfiguration.configureForNextRequest(this);
	}

	/**
	 * Configures headers for all requests.
	 *
	 * @param headerConfiguration the configuration to apply
	 */
	public void configureHeader(IApiHeaderConfiguration headerConfiguration) {
		headerConfiguration.configure(this);
	}

	/**
	 * Configures the specified request with the headers of this manager. This will set all
	 * permanent headers and header changes specific to the next request. Changes for the next
	 * request are then cleared.
	 *
	 * @param request the request to configure
	 */
	public void configure(ApiTestRequest request) {
		for (final ApiHeader header : buildHeadersForNextRequest()) {
			request.setHeader(header.getName(), header.computeValue(request));
		}
	}

	/**
	 * Returns the headers to apply to the next request.
	 *
	 * @return a list of API headers
	 */
	private List<ApiHeader> buildHeadersForNextRequest() {

		final MultivaluedMap<String, ApiHeader> nextRequestHeaders = new MultivaluedHashMap<>();
		final MultivaluedMap<String, ApiHeader> remainingHeaders = new MultivaluedHashMap<>();

		// Build real headers
		for (List<ApiHeader> apiHeaders : headers.values()) {
			for (ApiHeader apiHeader : apiHeaders) {
				// Set a header
				if (!nextRequestHeaders.containsKey(apiHeader.getName()) && !apiHeader.isRemoved() && !apiHeader.isReplaced()) {
					nextRequestHeaders.putSingle(apiHeader.getName(), apiHeader);
					remainingHeaders.putSingle(apiHeader.getName(), apiHeader);
				} // Replace a header
				else if (nextRequestHeaders.containsKey(apiHeader.getName()) && apiHeader.isReplaced()) {
					nextRequestHeaders.remove(apiHeader.getName());
					nextRequestHeaders.putSingle(apiHeader.getName(), apiHeader);
				} // Remove header
				else if (nextRequestHeaders.containsKey(apiHeader.getName()) && apiHeader.isRemoved()) {
					nextRequestHeaders.remove(apiHeader.getName());
				}
			}
		}

		headers = remainingHeaders;

		final List<ApiHeader> headerList = new ArrayList<>();
		for (List<ApiHeader> currentHeaders : nextRequestHeaders.values()) {
			headerList.addAll(currentHeaders);
		}

		return headerList;
	}
}