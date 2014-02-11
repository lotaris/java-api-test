package com.lotaris.dcc.test.utils.client.header;

import org.glassfish.jersey.internal.util.Base64;

/**
 * Dedicated header to set the Basic Authentication
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class AuthenticationBasicApiHeader extends ApiHeader {
	/**
	 * Basic header name
	 */
	private static final String HEADER_BASIC = "Authorization";

	/**
	 * Constructor
	 * 
	 * @param user The user name
	 * @param password The password
	 */
	public AuthenticationBasicApiHeader(String user, String password) {
		super(HEADER_BASIC, "Basic " + Base64.encodeAsString(user + ":" + password));
	}
}