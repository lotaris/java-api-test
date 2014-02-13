package com.lotaris.api.test.headers;

/**
 * Object capable of producing an {@link IApiHeaderConfiguration} to configure API request headers.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public interface IApiHeaderConfigurator {

	/**
	 * @return an API header configuration
	 */
	IApiHeaderConfiguration getApiHeaderConfiguration();
}
