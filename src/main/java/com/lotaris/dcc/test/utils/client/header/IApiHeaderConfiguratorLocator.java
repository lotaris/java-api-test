package com.lotaris.dcc.test.utils.client.header;

/**
 * Locator of {@link IApiHeaderConfigurator} instances.
 *
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public interface IApiHeaderConfiguratorLocator {

	/**
	 * @param klass the type of the header configurator to retrieve
	 * @return a header configurator of the specified type
	 */
	IApiHeaderConfigurator getHeaderConfigurator(Class<? extends IApiHeaderConfigurator> klass);
}
