package com.lotaris.dcc.test.rules;

import com.lotaris.api.test.client.ApiTestClient;
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

	@Override
	protected void before() throws Throwable {
		client = new ApiTestClient();
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
