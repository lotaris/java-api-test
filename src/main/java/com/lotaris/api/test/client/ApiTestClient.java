package com.lotaris.api.test.client;

import com.lotaris.dcc.test.utils.client.header.ApiHeader;
import com.lotaris.dcc.test.utils.client.header.ApiHeaderClientFilterRequest;
import com.lotaris.dcc.test.utils.client.header.IApiHeaderConfiguration;
import java.util.Map;
import javax.json.JsonStructure;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

/**
 * Jersey client wrapper to make requests on the server API.
 *
 * @author Simon Oulevay <simon.oulevay@lotaris.com>
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiTestClient {
	/**
	 * The entry point
	 */
	private String entryPoint;
	
	/**
	 * The internal Jersey client.
	 */
	private JerseyClient client;

	/**
	 * Header filter client request
	 */
	private ApiHeaderClientFilterRequest headerClientFilter;

	/**
	 * Constructs a new client.
	 * 
	 * @param entryPoint The entry point
	 */
	public ApiTestClient(String entryPoint) {
		this.entryPoint = entryPoint;
		
		client = new JerseyClientBuilder().build();
		
		headerClientFilter = new ApiHeaderClientFilterRequest();
		
		client.register(headerClientFilter);
	}

	/**
	 * Closes this client and all associated resources.
	 */
	public void close() {
		client.close();
	}
	
	/**
	 * Do a GET request
	 * 
	 * @param uriBuilder The uri builder
	 * @return The request response
	 */
	public ApiTestResponse getResource(ApiUriBuilder uriBuilder) {
		return response(
			target(uriBuilder)
			.request()
			.accept(MediaType.APPLICATION_JSON)
			.get()
		);
	}
	
	/**
	 * Do a POST request
	 * 
	 * @param uriBuilder The uri builder
	 * @return The request response
	 */
	public ApiTestResponse postResource(JsonStructure body, ApiUriBuilder uriBuilder) {
		return response(
			target(uriBuilder)
			.request()
			.accept(MediaType.APPLICATION_JSON)
			.post(Entity.entity(body.toString(), MediaType.APPLICATION_JSON))
		);
	}

	/**
	 * Do a PUT request
	 * 
	 * @param uriBuilder The uri builder
	 * @return The request response
	 */
	public ApiTestResponse putResource(JsonStructure body, ApiUriBuilder uriBuilder) {
		return response(
			target(uriBuilder)
			.request()
			.accept(MediaType.APPLICATION_JSON)
			.put(Entity.entity(body.toString(), MediaType.APPLICATION_JSON))
		);
	}
	
	/**
	 * Do a PATCH request
	 * 
	 * @param uriBuilder The uri builder
	 * @return The request response
	 */
	public ApiTestResponse patchResource(JsonStructure body, ApiUriBuilder uriBuilder) {
		return response(
			target(uriBuilder)
			.request()
			.accept(MediaType.APPLICATION_JSON)
			.method("PATCH", Entity.entity(body.toString(), MediaType.APPLICATION_JSON))
		);		
	}

	/**
	 * Do a DELETE request
	 * 
	 * @param uriBuilder The uri builder
	 * @param queryParamBuilder The query parameters builder
	 * @return The request response
	 */
	public ApiTestResponse deleteResource(ApiUriBuilder uriBuilder) {
		return response(
			target(uriBuilder)
			.request()
			.accept(MediaType.APPLICATION_JSON)
			.delete()
		);
	}
	
	/**
	 * Configure headers on the client from a header configuration
	 * 
	 * @param headerConfiguration Header configuration
	 */
	public void configureHeaderForNextRequest(IApiHeaderConfiguration headerConfiguration) {
		headerConfiguration.configureForNextRequest(this);
	}

	/**
	 * Configure headers on the client from a header configuration
	 * 
	 * @param headerConfiguration Header configuration
	 */
	public void configureHeader(IApiHeaderConfiguration headerConfiguration) {
		headerConfiguration.configure(this);
	}
	
	/**
	 * Add header for the next request
	 * 
	 * @param name Name of the header
	 * @param value Value of the header
	 */
	public void setHeaderForNextRequest(final String name, final String value) {
		headerClientFilter.setHeaderForNextRequest(name, value);
	}
	
	/**
	 * Add header for the next request
	 * 
	 * @param apiHeader API header
	 */
	public void setHeaderForNextRequest(final ApiHeader apiHeader) {
		headerClientFilter.setHeaderForNextRequest(apiHeader);
	}
	
	/**
	 * Add header for the all requests
	 * 
	 * @param name Name of the header
	 * @param value Value of the header
	 */
	public void setHeader(final String name, final String value) {
		headerClientFilter.setHeader(name, value);
	}

	/**
	 * Add header for the all requests
	 * 
	 * @param apiHeader API header
	 */
	public void setHeader(final ApiHeader apiHeader) {
		headerClientFilter.setHeader(apiHeader);
	}
	
	/**
	 * Remove header for the next request
	 * 
	 * @param name Name of the header
	 */
	public void removeHeaderForNextRequest(String name) {
		headerClientFilter.removeHeaderForNextRequest(name);
	}
	
	/**
	 * Replace header for the next request
	 * 
	 * @param name Name of the header
	 * @param value Value of the header
	 */
	public void replaceHeaderForNextRequest(String name, String value) {
		headerClientFilter.replaceHeaderForNextRequest(name, value);
	}

	/**
	 * Retrieve a target from the jersey client from the path and query param builder
	 * 
	 * @param uriBuilder The uri builder
	 * @return The jersey web target
	 */
	private JerseyWebTarget target(ApiUriBuilder uriBuilder) {
		return configure(client.target(entryPoint), uriBuilder);
	}
	
	/**
	 * Build response object from jersey response
	 * 
	 * @param response The jersey response
	 * @return The API response
	 */
	private ApiTestResponse response(Response response) {
		return new ApiTestResponse(response);
	}
	
	/**
	 * Configure a target from the API uri builder
	 * 
	 * @param target The jersey target to configure
	 * @param uriBuilder The uri builder
	 * @return The target updated
	 */
	private static JerseyWebTarget configure(JerseyWebTarget target, ApiUriBuilder uriBuilder) {
		if (uriBuilder != null) {
			for (String path : uriBuilder.getPathElements()) {
				target = target.path(path);
			}

			for (Map.Entry<String, Object> queryParam : uriBuilder.getQueryParams().entrySet()) {
				target = target.queryParam(queryParam.getKey(), queryParam.getValue());
			}
		}
		
		return target;
	}
}
