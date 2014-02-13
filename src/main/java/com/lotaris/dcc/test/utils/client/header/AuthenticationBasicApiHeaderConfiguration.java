package com.lotaris.dcc.test.utils.client.header;

/**
 * API header configuration that will add {@link AuthenticationBasicApiHeader} to the request.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class AuthenticationBasicApiHeaderConfiguration implements IApiHeaderConfiguration {

	/**
	 * The username to use for basic authentication.
	 */
	private String user;
	/**
	 * The password to use for basic authentication.
	 */
	private String password;

	/**
	 * Constructs a new configuration.
	 *
	 * @param user the username
	 * @param password the password
	 */
	public AuthenticationBasicApiHeaderConfiguration(String user, String password) {
		this.user = user;
		this.password = password;
	}

	@Override
	public void configureForNextRequest(ApiHeadersManager headersManager) {
		headersManager.setHeaderForNextRequest(buildHeader());
	}

	@Override
	public void configure(ApiHeadersManager headersManager) {
		headersManager.setHeader(buildHeader());
	}

	/**
	 * @return the API header for basic authentication
	 */
	private ApiHeader buildHeader() {
		return new AuthenticationBasicApiHeader(user, password);
	}
}
