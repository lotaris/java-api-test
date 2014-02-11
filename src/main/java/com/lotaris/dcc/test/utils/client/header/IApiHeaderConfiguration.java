package com.lotaris.dcc.test.utils.client.header;

import com.lotaris.api.test.client.ApiTestClient;

/**
 * Define an API header configuration
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public interface IApiHeaderConfiguration {
	/**
	 * Configure the API test client with the headers of the 
	 * API header configuration for the next request
	 * 
	 * @param client The API test client
	 */
	void configureForNextRequest(ApiTestClient client);

	/**
	 * Configure the API test client with the headers of the 
	 * API header configuration for all the requests of a test
	 * 
	 * @param client The API test client
	 */
	void configure(ApiTestClient client);
}
