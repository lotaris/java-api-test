package com.lotaris.api.test.client;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;

/**
 * Represent a response object that can be manipulated into the tests. It wraps the
 * real response object the possibility to switch the implementation of the client
 * used to do the HTTP requests.
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiTestResponse {
	/**
	 * Jersey response
	 */
	private Response response;
	
	/**
	 * Cache to avoid stream issue
	 */
	private String cachedResponseAsString;

	/**
	 * Constructor
	 * 
	 * @param response The wrapped response
	 */
	protected ApiTestResponse(Response response) {
		this.response = response;
	}
	
	/**
	 * @return The Jersey response
	 */
	protected Response getResponse() {
		return response;
	}
	
	/**
	 * @return The HTTP code status
	 */
	public int getStatus() {
		return response.getStatus();
	}
	
	/**
	 * @param headerName The header name
	 * @return The string value of the header
	 */
	public String getHeaderString(String headerName) {
		return response.getHeaderString(headerName);
	}
	
	/**
	 * @return The response body as string
	 */
	public String getResponseAsString() {
		if (cachedResponseAsString == null) {
			cachedResponseAsString = response.readEntity(String.class);
		}
		
		return cachedResponseAsString;
	}
	
	/**
	 * @return The response body as JSON object
	 */
	public JsonObject getResponseAsJsonObject() {
		return Json.createReader(new StringReader(getResponseAsString())).readObject();
	}
}
