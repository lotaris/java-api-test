package com.lotaris.api.test.client;

import org.junit.rules.ExternalResource;

/**
 * Client rule to manage the client
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiTestClientRule extends ExternalResource {
	/**
	 * API client for tests.
	 */
	private ApiTestClient client;

	/**
	 * Entry point
	 */
	private String entryPoint;

	/**
	 * Constructor
	 * 
	 * @param entryPoint Entry point for the client
	 */
	public ApiTestClientRule(String entryPoint) {
		this.entryPoint = entryPoint;
	}
	
	@Override
	protected void before() throws Throwable {
		client = new ApiTestClient(entryPoint);
	}

	@Override
	protected void after() {
		client.close();
	}
	
	/**
	 * @return The API test client
	 */
	public ApiTestClient getClient() {
		return client;
	}
}
