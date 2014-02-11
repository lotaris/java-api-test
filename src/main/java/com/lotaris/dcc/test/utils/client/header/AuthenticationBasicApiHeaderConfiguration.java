package com.lotaris.dcc.test.utils.client.header;

import com.lotaris.api.test.client.ApiTestClient;

/**
 * Represent an HTTP Basic authentication header to setup the authentication
 * through the HTTP headers.
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class AuthenticationBasicApiHeaderConfiguration implements IApiHeaderConfiguration {
	private String user;
	private String password;

	/**
	 * Constructor
	 * 
	 * @param user The user
	 * @param password The password
	 */
	public AuthenticationBasicApiHeaderConfiguration(String user, String password) {
		this.user = user;
		this.password = password;
	}

	@Override
	public void configureForNextRequest(ApiTestClient client) {
		client.setHeaderForNextRequest(buildHeader());
	}

	@Override
	public void configure(ApiTestClient client) {
		client.setHeader(buildHeader());
	}
	
	/**
	 * @return The header built for the basic authentication
	 */
	private ApiHeader buildHeader() {
		return new AuthenticationBasicApiHeader(user, password);
	}
}
