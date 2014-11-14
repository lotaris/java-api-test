package com.lotaris.api.test.rules;

import com.lotaris.api.test.client.ApiTestClient;
import com.lotaris.api.test.client.IApiTestClientConfiguration;
import org.junit.rules.ExternalResource;

/**
 * JUnit rule to create and release an HTTP client for each test.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 * @author Simon Oulevay <simon.oulevay@lotaris.com>
 */
public class ApiTestClientRule extends ExternalResource {

	/**
	 * API client for tests.
	 */
	private ApiTestClient client;

	/**
	 * Client configuration
	 */
	private IApiTestClientConfiguration clientConfiguration;
	
	/**
	 * Constructor
	 * 
	 * @param clientConfiguration The client configuration
	 */
	public ApiTestClientRule(IApiTestClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}
	
	@Override
	protected void before() throws Throwable {
		client = new ApiTestClient(clientConfiguration);
	}

	@Override
	protected void after() {
		client.close();
	}

	/**
	 * Returns the API test client.
	 *
	 * @return an API test client
	 */
	public ApiTestClient getClient() {
		return client;
	}	
}
