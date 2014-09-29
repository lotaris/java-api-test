package com.lotaris.api.test.rules;

import com.lotaris.api.test.headers.IApiHeaderConfiguration;
import com.lotaris.api.test.headers.IApiHeaderConfiguratorLocator;
import com.lotaris.api.test.headers.ApiHeaderConfigurator;
import com.lotaris.api.test.headers.ApiHeadersManager;
import com.lotaris.api.test.headers.IApiHeaderConfigurator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * JUnit rule to configure request headers for each test based on the {@link ApiHeaderConfigurator}
 * annotation on the test class (if present).
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiTestHeaderConfigurationRule implements TestRule {

	/**
	 * The header configurator locator that will be used to retrieve the header configuration.
	 */
	private IApiHeaderConfiguratorLocator headerConfiguratorLocator;
	/**
	 * The rule containing the headers manager to which the header configuration will be applied.
	 */
	private ApiTestHeadersManagerRule headersManagerRule;

	/**
	 * Constructs a new rule.
	 *
	 * @param headerConfiguratorLocator the header configurator locator
	 * @param headersManagerRule the rule containing the headers manager
	 */
	public ApiTestHeaderConfigurationRule(IApiHeaderConfiguratorLocator headerConfiguratorLocator, ApiTestHeadersManagerRule headersManagerRule) {
		this.headerConfiguratorLocator = headerConfiguratorLocator;
		this.headersManagerRule = headersManagerRule;
	}

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				configureHeaders(description);
				base.evaluate();
			}
		};
	}

	/**
	 * Configure request headers if the test class annotation is present.
	 *
	 * @param description The test description to get the info if headers must be configured
	 */
	private void configureHeaders(Description description) {

		// get the test class annotation
		final ApiHeaderConfigurator headerConfigurator = description.getAnnotation(ApiHeaderConfigurator.class);
		if (headerConfigurator != null) {

			List<IApiHeaderConfigurator> configuratiors = new ArrayList<>();
			
			// get the list of configurators to retrieve header configurations
			for (Class<? extends IApiHeaderConfigurator> apiConfigClass : headerConfigurator.value()) {
				// retrieve the header configurator
				final IApiHeaderConfigurator configurator = headerConfiguratorLocator.getHeaderConfigurator(apiConfigClass);

				// add each configuration
				for (IApiHeaderConfiguration configuration : configurator.getApiHeaderConfigurations()) {
					// apply the configuration to the headers manager
					headersManagerRule.getHeadersManager().configure(ApiHeadersManager.Operation.SET, configuration, true);
				}
			}
		}
	}
}
