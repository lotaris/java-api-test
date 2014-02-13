package com.lotaris.api.test.headers;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;

/**
 * <tt>Authorization</tt> header for Basic Authentication.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class AuthenticationBasicApiHeader extends ApiHeader {

	/**
	 * Constructs a new header.
	 *
	 * @param user the username
	 * @param password the password
	 */
	public AuthenticationBasicApiHeader(String user, String password) {
		super(HttpHeaders.AUTHORIZATION, "Basic " + Base64.encodeBase64String((user + ":" + password).getBytes()));
	}
}