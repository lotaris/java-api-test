package com.lotaris.dcc.test.utils.client.header;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enrich the tests to allow automatic
 * configuration of the headers
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiHeaderConfigurator {
	/**
	 * @return API header configurator class
	 */
	Class<? extends IApiHeaderConfigurator> value();
}
