package com.lotaris.dcc.test.utils.client.header;

/**
 * API header configuration to add headers to HTTP requests through a headers manager.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public interface IApiHeaderConfiguration {

	/**
	 * Apply this headers configuration for the next request.
	 *
	 * @param headersManager the headers manager holding the headers state
	 */
	void configureForNextRequest(ApiHeadersManager headersManager);

	/**
	 * Apply this headers configuration for all requests.
	 *
	 * @param headersManager the headers manager holding the headers state
	 */
	void configure(ApiHeadersManager headersManager);
}
