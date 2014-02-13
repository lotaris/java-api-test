package com.lotaris.api.test;

import com.lotaris.dcc.test.rules.ApiTestClientRule;
import com.lotaris.dcc.test.rules.ApiTestHeaderConfigurationRule;
import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonassert.JsonAsserter;
import com.lotaris.dcc.test.rules.ApiTestHeadersManagerRule;
import com.lotaris.api.test.client.ApiTestRequest;
import com.lotaris.api.test.client.ApiTestRequestBody;
import com.lotaris.api.test.client.ApiUriBuilder;
import com.lotaris.api.test.client.ApiTestResponse;
import com.lotaris.api.test.headers.ApiHeader;
import com.lotaris.api.test.headers.ApiHeaderConfigurator;
import com.lotaris.api.test.headers.ApiHeadersManager;
import com.lotaris.api.test.headers.IApiHeaderConfiguration;
import com.lotaris.api.test.headers.IApiHeaderConfiguratorLocator;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonStructure;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

/**
 * Abstract API test implementation with utilities to make API calls, customize headers and make
 * assertions on JSON responses.
 *
 * <p>An API client is automatically created before each test and closed after each test.</p>
 *
 * <p>Methods such as <tt>getResource</tt> and <tt>postResource</tt> are provided for GET, POST,
 * PUT, PATCH and DELETE. They perform the request (with a body for POST, PUT and PATCH) and return
 * an API test response.</p>
 *
 * <p>A JsonPath asserter (see https://code.google.com/p/json-path/) is returned by
 * <tt>withJson</tt> methods. They can be used to run assertions on the response body.</p>
 *
 * @author Simon Oulevay (simon.oulevay@lotaris.com)
 * @author Laurent Prevost, laurent.prevost@lotaris.com
 */
public abstract class AbstractApiTest {

	/**
	 * JUnit rule to create and release an HTTP client for each test.
	 */
	private ApiTestClientRule clientRule;
	/**
	 * JUnit rule to create a headers manager for each test.
	 */
	private ApiTestHeadersManagerRule headersManagerRule;
	/**
	 * JUnit rule to configure request headers for each test based on the
	 * {@link ApiHeaderConfigurator} annotation on the test class (if present).
	 */
	private ApiTestHeaderConfigurationRule headerConfigurationRule;
	/**
	 * The default API entry point (must be provided by implementations).
	 *
	 * @see #getEntryPoint()
	 */
	private String entryPoint;
	/**
	 * Chain of JUnit rules to apply to each test.
	 */
	@Rule
	public RuleChain chain;

	//<editor-fold defaultstate="collapsed" desc="Constructor">
	public AbstractApiTest() {
		// We use a private method that cannot be overidden to ensure that the construction
		// template cannot be changed.
		build();
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Construction Template">
	/**
	 * Template method responsible for initializing the API test class.
	 */
	private void build() {
		preBuild();

		// cache entry point
		entryPoint = getEntryPoint();

		// create rules
		clientRule = new ApiTestClientRule();
		headersManagerRule = new ApiTestHeadersManagerRule();
		headerConfigurationRule = new ApiTestHeaderConfigurationRule(getHeaderConfiguratorLocator(), headersManagerRule);

		// make sure the client and headers manager rules are run first
		chain = RuleChain.outerRule(clientRule).around(headersManagerRule);

		// then run implementation rules that may depend on the client or headers manager
		for (TestRule rule : rulesAfterClientRules()) {
			chain = chain.around(rule);
		}

		// run the header configuration rule
		chain = chain.around(headerConfigurationRule);

		// run the rest of the rules
		for (TestRule rule : rulesAfterHeaderConfigurationRule()) {
			chain = chain.around(rule);
		}
	}

	/**
	 * Prepare the construction of the API test class. Allows implementations to set up required
	 * resources.
	 */
	protected abstract void preBuild();

	/**
	 * Returns the default API entry point used to build URIs.
	 *
	 * @return a base URI
	 * @see #uri(java.lang.String[])
	 */
	protected abstract String getEntryPoint();

	/**
	 * Returns the operations that must be run after the HTTP client and headers manager have been
	 * created.
	 *
	 * @return a list of JUnit rules
	 */
	protected List<TestRule> rulesAfterClientRules() {
		return new ArrayList<>();
	}

	/**
	 * Returns additional operations that must be run after request headers have been configured.
	 *
	 * @return a list of JUnit rules
	 */
	protected List<TestRule> rulesAfterHeaderConfigurationRule() {
		return new ArrayList<>();
	}

	/**
	 * Returns a locator that will be used to retrieve the header configuration to be applied for
	 * each test.
	 *
	 * @return a header configurator locator
	 */
	protected abstract IApiHeaderConfiguratorLocator getHeaderConfiguratorLocator();
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Helpers: URIs">
	/**
	 * Returns an URI builder relative to the default API entry point.
	 *
	 * @param pathElements initial path elements (optional)
	 * @return an URI builder
	 * @see #getEntryPoint()
	 */
	protected ApiUriBuilder uri(String... pathElements) {
		return new ApiUriBuilder(entryPoint).path(pathElements);
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Helpers: HTTP requests">
	/**
	 * Performs a GET request on a resource.
	 *
	 * @param singlePath a single path element
	 * @return the API response
	 */
	protected ApiTestResponse getResource(String singlePath) {
		return getResource(uri(singlePath));
	}

	/**
	 * Performs a GET request on a resource.
	 *
	 * @param uriBuilder URI builder
	 * @return the API response
	 */
	protected ApiTestResponse getResource(ApiUriBuilder uriBuilder) {
		return executeJsonRequest(ApiTestRequest.GET, uriBuilder, null);
	}

	/**
	 * Performs a POST request on a resource.
	 *
	 * @param body the request body
	 * @param singlePath a single path element
	 * @return the API response
	 */
	protected ApiTestResponse postResource(JsonStructure body, String singlePath) {
		return postResource(body, uri(singlePath));
	}

	/**
	 * Performs a POST request on a resource.
	 *
	 * @param body the request body
	 * @param urlBuilder URI builder
	 * @return the API response
	 */
	protected ApiTestResponse postResource(JsonStructure body, ApiUriBuilder urlBuilder) {
		return executeJsonRequest(ApiTestRequest.POST, urlBuilder, body);
	}

	/**
	 * Performs a PUT request on a resource.
	 *
	 * @param body the request body
	 * @param singlePath a single path element
	 * @return the API response
	 */
	protected ApiTestResponse putResource(JsonStructure body, String singlePath) {
		return putResource(body, uri(singlePath));
	}

	/**
	 * Performs a PUT request on a resource.
	 *
	 * @param body the request body
	 * @param uriBuilder URI builder
	 * @return the API response
	 */
	protected ApiTestResponse putResource(JsonStructure body, ApiUriBuilder uriBuilder) {
		return executeJsonRequest(ApiTestRequest.PUT, uriBuilder, body);
	}

	/**
	 * Performs a PATCH request on a resource.
	 *
	 * @param body the request body
	 * @param singlePath a single path element
	 * @return the API response
	 */
	protected ApiTestResponse patchResource(JsonStructure body, String singlePath) {
		return patchResource(body, uri(singlePath));
	}

	/**
	 * Performs a PATCH request on a resource.
	 *
	 * @param body the request body
	 * @param uriBuilder URI builder
	 * @return the API response
	 */
	protected ApiTestResponse patchResource(JsonStructure body, ApiUriBuilder uriBuilder) {
		return executeJsonRequest(ApiTestRequest.PATCH, uriBuilder, body);
	}

	/**
	 * Performs a DELETE request on a resource.
	 *
	 * @param singlePath a single path element
	 * @return the API response
	 */
	protected ApiTestResponse deleteResource(String singlePath) {
		return deleteResource(uri(singlePath));
	}

	/**
	 * Performs a DELETE request on a resource.
	 *
	 * @param pathBuilder URI builder
	 * @return the API response
	 */
	protected ApiTestResponse deleteResource(ApiUriBuilder uriBuilder) {
		return executeJsonRequest(ApiTestRequest.DELETE, uriBuilder, null);
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Helpers: header configuration">
	/**
	 * Set a header for all subsequent requests. Previous headers with the same name are
	 * overwritten.
	 *
	 * @param name header name
	 * @param value header value
	 */
	protected void setHeaderForAllRequests(String name, String value) {
		setHeaderForAllRequests(new ApiHeader(name, value));
	}

	/**
	 * Set a header for all subsequent requests. Previous headers with the same name are
	 * overwritten.
	 *
	 * @param header the header to set
	 */
	protected void setHeaderForAllRequests(ApiHeader header) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.SET, header, true);
	}

	/**
	 * Set request headers for all subsequent requests. Previous headers with the same name are
	 * overwritten.
	 *
	 * @param headerConfiguration the headers to set
	 */
	protected void setHeadersForAllRequests(IApiHeaderConfiguration headerConfiguration) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.SET, headerConfiguration, true);
	}

	/**
	 * Set a header for the next request. Previous headers with the same name are overwritten (for
	 * the next request).
	 *
	 * @param name header name
	 * @param value header value
	 */
	protected void setHeaderForNextRequest(String name, String value) {
		setHeaderForNextRequest(new ApiHeader(name, value));
	}

	/**
	 * Set a header for the next request. Previous headers with the same name are overwritten (for
	 * the next request).
	 *
	 * @param header the header to set
	 */
	protected void setHeaderForNextRequest(ApiHeader header) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.SET, header, false);
	}

	/**
	 * Set request headers for the next request. Previous headers with the same name are overwritten
	 * (for the next request).
	 *
	 * @param headerConfiguration the headers to set
	 */
	protected void setHeadersForNextRequest(IApiHeaderConfiguration headerConfiguration) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.SET, headerConfiguration, false);
	}

	/**
	 * Alias for {@link #setHeaderForNextRequest(java.lang.String, java.lang.String)}.
	 *
	 * @param name header name
	 * @param value header value
	 */
	protected void replaceHeaderForNextRequest(String name, String value) {
		setHeaderForNextRequest(name, value);
	}

	/**
	 * Alias for
	 * {@link #setHeaderForNextRequest(com.lotaris.dcc.test.utils.client.header.ApiHeader)}
	 *
	 * @param header the header to set
	 */
	protected void replaceHeaderForNextRequest(ApiHeader header) {
		setHeaderForNextRequest(header);
	}

	/**
	 * Removes a header for the next request. All headers with the specified name are removed.
	 *
	 * @param name name of the header to remove
	 */
	protected void removeHeaderForNextRequest(String name) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.REMOVE, new ApiHeader(name, null), false);
	}

	/**
	 * Removes headers for the next request. All headers with the specified names are removed.
	 *
	 * @param headerConfiguration headers to remove
	 */
	protected void removeHeadersForNextRequest(IApiHeaderConfiguration headerConfiguration) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.REMOVE, headerConfiguration, false);
	}

	/**
	 * Removes a header for all subsequent requests. All headers with the specified name are
	 * removed.
	 *
	 * @param name name of the header to remove
	 */
	protected void removeHeaderForAllRequests(String name) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.REMOVE, new ApiHeader(name, null), true);
	}

	/**
	 * Removes headers for all subsequent requests. All headers with the specified names are
	 * removed.
	 *
	 * @param headerConfiguration headers to remove
	 */
	protected void removeHeadersForAllRequests(IApiHeaderConfiguration headerConfiguration) {
		headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.REMOVE, headerConfiguration, true);
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Helpers: JSON assertions">
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
	 * Returns a JsonPath asserter for an API response.
	 *
	 * @param response the response whose body to run assertions on
	 * @return a JSON asserter
	 * @see https://code.google.com/p/json-path/
	 */
	protected JsonAsserter withJson(ApiTestResponse response) {
		return withJson(response.getResponseAsString());
	}
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Private Utilities">
	/**
	 * Executes a standard API request. By default:
	 *
	 * <ul>
	 * <li>the <tt>Accept</tt> header is set to <tt>application/json</tt>;</li>
	 * <li>if provided, the json structure is used as the request body and the <tt>Content-Type</tt>
	 * header is set to <tt>application/json</tt>;</li>
	 * <li>request headers are configured by the headers manager rule.</li>
	 * </ul>
	 *
	 * @param method the HTTP method
	 * @param uriBuilder the URI builder
	 * @param json an optional JSON request body
	 * @return the API response
	 */
	private ApiTestResponse executeJsonRequest(String method, ApiUriBuilder uriBuilder, JsonStructure json) {
		
		// prepare request entity (if present); Content-Type header is defined by the entity
		final ApiTestRequestBody entity = json != null ? ApiTestRequestBody.fromJson(json) : null;
		
		// build request and set Accept header
		final ApiTestRequest request = new ApiTestRequest(method, uriBuilder, entity);
		request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		
		// configure request headers
		headersManagerRule.getHeadersManager().applyConfiguration(request);
		
		// perform the request and return the response
		return clientRule.getClient().execute(request);
	}
	//</editor-fold>
}
