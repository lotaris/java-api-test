package com.lotaris.api.test;

import com.lotaris.api.test.client.ApiTestClientRule;
import com.lotaris.api.test.client.ApiTestClient;
import com.lotaris.dcc.test.utils.client.header.ApiHeaderClientRule;
import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonassert.JsonAsserter;
import com.lotaris.api.test.client.ApiUriBuilder;
import com.lotaris.api.test.client.ApiTestResponse;
import com.lotaris.dcc.test.utils.client.header.ApiHeader;
import com.lotaris.dcc.test.utils.client.header.IApiHeaderConfiguration;
import com.lotaris.dcc.test.utils.client.header.IClassApiHeaderConfiguratorLocator;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonStructure;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Useful functionality for API tests.
 *
 * <p>An API client is automatically instantiated before each test and closed after each test.
 *
 * <p>Methods such as {@link #getResource(java.lang.String[])} are provided for GET, POST, PUT,
 * PATCH and DELETE. They perform the request (with a body for POST, PUT and PATCH) and return a
 * API test response.</p>
 *
 * <p>A JsonPath asserter (see https://code.google.com/p/json-path/) is returned by
 * <tt>withJson</tt> methods. They can be used to run assertions on the response body.</p>
 *
 * @author Simon Oulevay (simon.oulevay@lotaris.com)
 * @author Laurent Prevost, laurent.prevost@lotaris.com
 */
public abstract class AbstractApiTest {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractApiTest.class);
	
	/**
	 * Different rules to manage everything for API client and data generators
	 */
	private ApiTestClientRule clientRule;
	private ApiHeaderClientRule clientHeaderRule;

	public ApiTestClient client;
	
	@Rule
	public RuleChain chain;
	
	/**
	 * Constructor
	 */
	public AbstractApiTest() {
		preBuild();
		
		clientRule = new ApiTestClientRule(getEntryPoint());
		
		chain = RuleChain.outerRule(clientRule);

		for (TestRule rule : rulesAfterClientRule()) {
			chain = chain.around(rule);
		}
		
		clientHeaderRule = new ApiHeaderClientRule(getClassApiHeaderConfiguratorLocator(), clientRule);
		
		chain = chain.around(clientHeaderRule);

		for (TestRule rule : rulesAfterClientHeaderRule()) {
			chain = chain.around(rule);
		}
	}

	/**
	 * Prepare the construction of the API test class
	 */
	protected abstract void preBuild();
	
	/**
	 * @return Retrieve the entry point of the API to test
	 */
	protected abstract String getEntryPoint();
	
	/**
	 * @return Additional rules to set after the client rule
	 */
	protected List<TestRule> rulesAfterClientRule() {
		return new ArrayList<>();
	}
	
	/**
	 * @return Additional rules to set after the client header rule
	 */
	protected List<TestRule> rulesAfterClientHeaderRule() {
		return new ArrayList<>();
	}
	
	/**
	 * @return Retrieve the API header configurator locator
	 */
	protected abstract IClassApiHeaderConfiguratorLocator getClassApiHeaderConfiguratorLocator();
	
	/**
	 * Configure headers on the client from a header configuration
	 * 
	 * @param headerConfiguration Header configuration
	 */
	public void configureHeader(IApiHeaderConfiguration headerConfiguration) {
		clientRule.getClient().configureHeader(headerConfiguration);
	}
	
	/**
	 * Configure headers on the client from a header configuration
	 * 
	 * @param headerConfiguration Header configuration
	 */
	public void configureHeaderForNextRequest(IApiHeaderConfiguration headerConfiguration) {
		clientRule.getClient().configureHeaderForNextRequest(headerConfiguration);
	}
	
	/**
	 * Add a new header that will be added to the next requests. 
	 * 
	 * @param name Name of the header
	 * @param value Value of the header
	 */
	protected void setHeaderForNextRequest(String name, String value) {
		clientRule.getClient().setHeaderForNextRequest(name, value);
	}
	
	/**
	 * Add a new header that will be added to the next requests. 
	 * 
	 * @param header API Header object
	 */
	protected void setHeader(ApiHeader header) {
		clientRule.getClient().setHeaderForNextRequest(header);
	}
	
	/**
	 * Removes one header from the headers applied to next request.
	 * 
	 * @param name Name of the header
	 */
	protected void removeHeaderForNextRequest(String name) {
		clientRule.getClient().removeHeaderForNextRequest(name);
	}
	
	/**
	 * Replaces one header from the headers applied to next request.
	 * 
	 * @param header API Header object
	 */
	protected void replaceHeaderForNextRequest(String name, String value) {
		clientRule.getClient().replaceHeaderForNextRequest(name, value);
	}

	/**
	 * Performs a GET request on a resource.
	 *
	 * @param singlePath A single path element
	 * @return the request response
	 */
	protected ApiTestResponse getResource(String singlePath) {
		return getResource(uri(singlePath));
	}
	
	/**
	 * Performs a GET request on a resource.
	 *
	 * @param uriBuilder URI builder
	 * @return the request response
	 */
	protected ApiTestResponse getResource(ApiUriBuilder uriBuilder) {
		return clientRule.getClient().getResource(uriBuilder);
	}
	
	/**
	 * Performs a POST request on a resource.
	 *
	 * @param body The body to send
	 * @param singlePath A signle path
	 * @return the request response
	 */
	protected ApiTestResponse postResource(JsonStructure body, String singlePath) {
		return postResource(body, uri(singlePath));
	}
	
	/**
	 * Performs a POST request on a resource.
	 *
	 * @param body The body to send
	 * @param urlBuilder URI builder
	 * @return the request response
	 */
	protected ApiTestResponse postResource(JsonStructure body, ApiUriBuilder urlBuilder) {
		return clientRule.getClient().postResource(body, urlBuilder);
	}

	/**
	 * Performs a PUT request on a resource.
	 *
	 * @param body The body to send
	 * @param singlePath A single path element
	 * @return the request response
	 */
	protected ApiTestResponse putResource(JsonStructure body, String singlePath) {
		return putResource(body, uri(singlePath));
	}
	
	/**
	 * Performs a PUT request on a resource.
	 *
	 * @param body The body to send
	 * @param uriBuilder URI builder
	 * @return the request response
	 */
	protected ApiTestResponse putResource(JsonStructure body, ApiUriBuilder uriBuilder) {
		return clientRule.getClient().putResource(body, uriBuilder);
	}

	/**
	 * Performs a PATCH request on a resource.
	 *
	 * @param body The body to send
	 * @param singlePath A single path element
	 * @return the request response
	 */
	protected ApiTestResponse patchResource(JsonStructure body, String singlePath) {
		return patchResource(body, uri(singlePath));
	}
	
	/**
	 * Performs a PATCH request on a resource.
	 *
	 * @param body The body to send
	 * @param uriBuilder URI builder
	 * @return the request response
	 */
	protected ApiTestResponse patchResource(JsonStructure body, ApiUriBuilder uriBuilder) {
		return clientRule.getClient().patchResource(body, uriBuilder);
	}

	/**
	 * Performs a DELETE request on a resource.
	 *
	 * @param body The body to send
	 * @param singlePath A single path element
	 * @return the request response
	 */
	protected ApiTestResponse deleteResource(String singlePath) {
		return deleteResource(uri(singlePath));
	}
	
	/**
	 * Performs a DELETE request on a resource.
	 *
	 * @param pathBuilder URI builder
	 * @return the request response
	 */
	protected ApiTestResponse deleteResource(ApiUriBuilder uriBuilder) {
		return clientRule.getClient().deleteResource(uriBuilder);
	}

	/**
	 * Returns a JsonPath asserter for a response body string.
	 *
	 * @param response the response body to run assertions on
	 * @return a JSON asserter
	 * @see https://code.google.com/p/json-path/
	 */
	protected JsonAsserter withJson(String response) {
		return JsonAssert.with(response);
	}

	/**
	 * Returns a JsonPath asserter for a Jersey response.
	 *
	 * @param response the response whose body to run assertions on
	 * @return a JSON asserter
	 * @see https://code.google.com/p/json-path/
	 */
	protected JsonAsserter withJson(ApiTestResponse response) {
		return withJson(response.getResponseAsString());
	}
	
	/**
	 * Log the response and the body
	 * 
	 * @param response The API response
	 */
	protected void log(ApiTestResponse response) {
		LOG.info("Response with code [{}] and body [{}].", response.getStatus(), response.getResponseAsString());
	}
	
	/**
	 * Utility method to build a path builder
	 * 
	 * @param path The first elements of the path builder
	 * @return The path builder
	 */
	protected static ApiUriBuilder uri(String ... path) {
		return new ApiUriBuilder(path);
	}
}
