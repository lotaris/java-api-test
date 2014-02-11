package com.lotaris.dcc.test.utils.client.header;

/**
 * Define the way to retrieve the header configurators
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public interface IClassApiHeaderConfiguratorLocator {
	/**
	 * @param cl The class of the header configurator to retrieve
	 * @return The header configurator retrieved
	 */
	IApiHeaderConfigurator getHeaderConfigurator(Class<? extends IApiHeaderConfigurator> cl);
}
