package com.lotaris.api.test.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder class to maintain list of path elements
 * 
 * @author Laurent Prevost <laurent.prevost@lotaris.com>
 */
public class ApiUriBuilder {
	/**
	 * The query parameters
	 */
	private Map<String, Object> queryParams = new HashMap<>();

	/**
	 * Path elements
	 */
	private List<String> pathElements = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param path The path elements
	 */
	public ApiUriBuilder(String ... path) {
		path(path);
	}

	/**
	 * Add path elements
	 * @param path Path elements to add
	 */
	public final ApiUriBuilder path(String ... path) {
		pathElements.addAll(Arrays.asList(path));
		return this;
	}
	

	/**
	 * Add query parameter element
	 * 
	 * @param name The name of the query parameter
	 * @param value The value of the query parameter
	 * @return The query parameter builder
	 */
	public ApiUriBuilder queryParam(String name, Object value) {
		queryParams.put(name, value);
		return this;
	}

	/**
	 * @return The map of query parameters
	 */
	protected Map<String, Object> getQueryParams() {
		return queryParams;
	}
	
	/**
	 * @return List of path elements
	 */
	protected List<String> getPathElements() {
		return pathElements;
	}
}
