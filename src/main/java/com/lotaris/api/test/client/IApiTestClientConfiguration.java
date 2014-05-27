package com.lotaris.api.test.client;

/**
 * Define the configuration of an API Test client
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public interface IApiTestClientConfiguration {
	/**
	 * @return True if proxy should be used
	 */
	boolean isProxyEnabled();
	
	/**
	 * @return Proxy host
	 */
	String getProxyHost();
	
	/**
	 * @return Proxy port
	 */
	int getProxyPort();
}
