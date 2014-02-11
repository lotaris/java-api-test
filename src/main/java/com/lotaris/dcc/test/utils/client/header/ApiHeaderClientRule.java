package com.lotaris.dcc.test.utils.client.header;

import com.lotaris.api.test.client.ApiTestClientRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


/**
 * Header API test client 
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiHeaderClientRule implements TestRule {
	/**
	 * The header configurator locator
	 */
	private IClassApiHeaderConfiguratorLocator headerConfiguratorLocator;
	
	/**
	 * API test client
	 */
	private ApiTestClientRule client;
	
	/**
	 * Constructor
	 * 
	 * @param headerConfiguratorLocator The header configurator locator
	 * @param client The API test client
	 */
	public ApiHeaderClientRule(IClassApiHeaderConfiguratorLocator headerConfiguratorLocator, ApiTestClientRule client) {
		this.headerConfiguratorLocator = headerConfiguratorLocator;
		this.client = client;
	}
	
	@Override
	public Statement apply(Statement base, Description description) {
		return innerApply(base, description);
	}

	/**
	 * Method to avoid problems with anonymous class and final variables otherwise
	 * the content of this method can be put on the method apply
	 */
	private Statement innerApply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				setupBaseHeader(description);
				base.evaluate();
			}
		};
	}		
	
	/**
	 * Setup the headers if necessary
	 * 
	 * @param description The test description to get the info if headers must be configured
	 */
	private void setupBaseHeader(Description description) {
		ApiHeaderConfigurator headerConfigurator = description.getAnnotation(ApiHeaderConfigurator.class);
		
		if (headerConfigurator != null) {
			IApiHeaderConfigurator configurator = headerConfiguratorLocator.getHeaderConfigurator(headerConfigurator.value());
			configurator.getApiHeaderConfiguration().configure(client.getClient());
		}
	}
}
