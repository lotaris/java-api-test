package com.lotaris.api.test.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.json.Json;
import javax.json.JsonStructure;
import javax.json.JsonWriter;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;

/**
 * HTTP request body wrapper.
 *
 * @author Simon Oulevay <simon.oulevay@lotaris.com>
 */
public class ApiTestRequestBody {

	//<editor-fold defaultstate="collapsed" desc="Media Type Constants">
	/**
	 * The <tt>application/json</tt> media type.
	 */
	public static final String APPLICATION_JSON = ContentType.APPLICATION_JSON.getMimeType();
	//</editor-fold>

	//<editor-fold defaultstate="collapsed" desc="Factory Methods">
	/**
	 * Constructs a request body with content type <tt>application/json</tt> from a JSON string.
	 *
	 * @param json the raw JSON string to use as request body
	 * @return an API request body
	 */
	public static ApiTestRequestBody fromJson(String json) {
		return new ApiTestRequestBody(json.getBytes(), APPLICATION_JSON);
	}

	/**
	 * Constructs a request body with content type <tt>application/json</tt> from a JSON structure.
	 *
	 * @param json the JSON structure to use as request body
	 * @return an API request body
	 */
	public static ApiTestRequestBody fromJson(JsonStructure json) {

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (JsonWriter writer = Json.createWriter(baos)) {
			writer.write(json);
		}

		return new ApiTestRequestBody(baos.toByteArray(), APPLICATION_JSON);
	}
	//</editor-fold>
	/**
	 * The request body.
	 */
	private byte[] body;
	/**
	 * The content type (media type and charset) of the body.
	 */
	private ContentType contentType;

	/**
	 * Constructs a new request body. The body is assumed to be encoded with the UTF-8 charset.
	 *
	 * @param body the request body
	 * @param mediaType the media type of the body
	 */
	public ApiTestRequestBody(byte[] body, String mediaType) {
		this(body, mediaType, StandardCharsets.UTF_8);
	}

	/**
	 * Constructs a new request body.
	 *
	 * @param body the request body
	 * @param mediaType the media type of the body
	 * @param charset the charset of the body
	 */
	public ApiTestRequestBody(byte[] body, String mediaType, Charset charset) {
		this.body = body;
		this.contentType = ContentType.create(mediaType, charset);
	}

	/**
	 * Converts the body to an Apache HTTP entity.
	 *
	 * @return an HTTP entity
	 * @see #{@link ApiTestRequest}
	 */
	protected HttpEntity toEntity() {
		return new InputStreamEntity(new ByteArrayInputStream(body), contentType);
	}
}
