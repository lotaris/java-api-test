package com.lotaris.dcc.test.utils.client.header;

/**
 * Define an API header configurator to retrieve an object that
 * allows to configure the API Test client.
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public interface IApiHeaderConfigurator {
	/**
	 * @return The API header configuration
	 */
	IApiHeaderConfiguration getApiHeaderConfiguration();
}
