/*******************************************************************************
 * Copyright (C) 2016 Salzburg Research Forschungsgesellschaft
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *   
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *  
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package http;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Client {

	private URI uri;
	private ContentType contentType = ContentType.APPLICATION_JSON;
	private final URIBuilder uriBuilder;
	List<NameValuePair> params = new ArrayList<>();
	List<Header> headers = new ArrayList<>();

	public Client(String service) throws URISyntaxException {
		this.uriBuilder = new URIBuilder(service);
		this.uri = uriBuilder.build();
	}

	public Client setPath(String path) {
		String p = uriBuilder.getPath();
		if (!p.endsWith("/")) {
			if (!path.startsWith("/")) {
				p += "/" + path;
			} else {
				p += path;
			}
		} else {
			if (path.startsWith("/")) {
				p += path.substring(1);
			} else {
				p += path;
			}
		}
		uriBuilder.setPath(p);
		return this;
	}
	public Client setContentType(ContentType type) {
		this.contentType = type;
		return this;
	}
	public Client setParameter(String name, String value) {
		uriBuilder.setParameter(name, value);
		params.add(new BasicNameValuePair(name, value));
		return this;
	}

	public Client setHeader(String key, String value) {
		headers.add(new BasicHeader(key, value));
		return this;
	}

	public Client setParameter(String name, Number value) {
		uriBuilder.setParameter(name, value.toString());
		params.add(new BasicNameValuePair(name, value.toString()));
		return this;
	}

	public Client setParameter(String name, Boolean bool) {
		uriBuilder.setParameter(name, bool.toString());
		params.add(new BasicNameValuePair(name, bool.toString()));
		return this;
	}

	public <T> List<T> doGetList(Class<T> resultClass) throws IOException, URISyntaxException {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet get = new HttpGet(uriBuilder.build());
			for (Header h : headers) {
				get.addHeader(h);
			}

			return httpClient.execute(get, new ResponseHandler<List<T>>() {
				// handle the result
				@Override
				public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();
					if (statusLine.getStatusCode() >= 300) {
						throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
					}
					if (entity == null) {
						throw new ClientProtocolException("Response contains no content");
					}
					ObjectMapper mapper = new ObjectMapper();
					try {
						//
						CollectionType listType = mapper.getTypeFactory().constructCollectionType(List.class,
								resultClass);
						List<T> myObjects = mapper.readValue(entity.getContent(), listType);
						return myObjects;
					} catch (IOException e) {
						e.printStackTrace();

					}
					return new ArrayList<>();
				}
			});
		} finally {
			httpClient.close();
		}

	}

	private String serializeToJson(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(object);
			System.out.println(object.getClass().getSimpleName() + ":  " + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	public <T> T doGet(Class<T> resultClass) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpGet get = new HttpGet(uriBuilder.build());
			for (Header h : headers) {
				get.addHeader(h);
			}
			return httpClient.execute(get, new ResponseHandler<T>() {
				// handle the reult
				@Override
				public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();
					if (statusLine.getStatusCode() >= 300) {
						throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
					}
					if (entity == null) {
						throw new ClientProtocolException("Response contains no content");
					}
					ObjectMapper mapper = new ObjectMapper();
					try {
						//
						InputStreamReader reader = new InputStreamReader(entity.getContent());
						
						T myObjects = mapper.readValue(entity.getContent(), resultClass);
						return myObjects;
					} catch (IOException e) {
						e.printStackTrace();

					}
					return null;
				}
			});
		} finally {
			httpClient.close();
		}
	}

	public <T> T doPost(Class<T> resultClass) throws IOException, URISyntaxException {
		return doPost(null, resultClass);
	}

	public <T> T doPost(Object obj, Class<T> resultClass) throws IOException, URISyntaxException {
		
		if (obj != null) {
			if ( obj instanceof Map) {
				
			}
			return doPost(serializeToJson(obj), resultClass, this.contentType);
		} else {
			// required when no
			return doPost((String) null, resultClass, this.contentType);
		}

	}
	/**
	 * Perform a POST and send the provided map as form parameter
	 * @param params
	 * @param resultClass
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public <T> T doPost(Map<String, String> params, Class<T> resultClass) throws IOException, URISyntaxException {
		HttpPost post = new HttpPost(uriBuilder.build());
		List<NameValuePair> formParams = new ArrayList<>();
		for (String key : params.keySet()) {
			formParams.add(new BasicNameValuePair(key, params.get(key)));
		}
		post.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
		return executePost(post, resultClass);
	}
	/**
	 * Create a new http client and send the provided POSt
	 * @param thePost
	 * @param resultClass
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private <T> T executePost(HttpPost thePost, Class<T> resultClass) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			return httpClient.execute(thePost, new ResponseHandler<T>() {

				@Override
				public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();
					if (statusLine.getStatusCode() >= 300) {
						throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
					}
					if (entity == null) {
						throw new ClientProtocolException("Response contains no content");
					} else {
						if (entity.getContentLength() == 0) {
							if (resultClass.isAssignableFrom(Boolean.class) && statusLine.getStatusCode() == 200) {
								return resultClass.cast(Boolean.TRUE);
							}

						}
					}
					ObjectMapper mapper = new ObjectMapper();
					try {
						//
						T myObjects = mapper.readValue(entity.getContent(), resultClass);
						return myObjects;
					} catch (IOException e) {
						e.printStackTrace();

					}
					return null;
				}
			});
		} finally {
			httpClient.close();
		}
	}

	public <T> T doPost(String jsonString, Class<T> resultClass, ContentType contentType)
			throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpPost post = new HttpPost(uriBuilder.build());
			//
			if (contentType.equals(ContentType.APPLICATION_FORM_URLENCODED) && params.size() > 0) {
				// create a new post
				post = new HttpPost(uri);
				post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

			} else {
				if (jsonString != null) {
					HttpEntity entity = new StringEntity(jsonString, contentType);
					post.setEntity(entity);
				}

			}
			for (Header h : headers) {
				post.addHeader(h);
			}

			return httpClient.execute(post, new ResponseHandler<T>() {

				@Override
				public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();
					if (statusLine.getStatusCode() >= 300) {
						throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
					}
					if (entity == null) {
						throw new ClientProtocolException("Response contains no content");
					} else {
						if (entity.getContentLength() == 0) {
							if (resultClass.isAssignableFrom(Boolean.class) && statusLine.getStatusCode() == 200) {
								return resultClass.cast(Boolean.TRUE);
							}

						}
					}
					ObjectMapper mapper = new ObjectMapper();
					try {
						//
						T myObjects = mapper.readValue(entity.getContent(), resultClass);
						return myObjects;
					} catch (IOException e) {
						e.printStackTrace();

					}
					return null;
				}
			});
		} finally {
			httpClient.close();
		}
	}

	public <T> T doDelete(Class<T> resultClass) throws IOException, URISyntaxException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			HttpDelete delete = new HttpDelete(uriBuilder.build());
			//
			for (Header h : headers) {
				delete.addHeader(h);
			}

			return httpClient.execute(delete, new ResponseHandler<T>() {

				@Override
				public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
					StatusLine statusLine = response.getStatusLine();
					HttpEntity entity = response.getEntity();
					if (statusLine.getStatusCode() >= 300) {
						throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
					}
					if (entity == null) {
						throw new ClientProtocolException("Response contains no content");
					} else {
						if (entity.getContentLength() == 0) {
							if (resultClass.isAssignableFrom(Boolean.class) && statusLine.getStatusCode() == 200) {
								return resultClass.cast(Boolean.TRUE);
							}

						}
					}
					ObjectMapper mapper = new ObjectMapper();
					try {
						//
						T myObjects = mapper.readValue(entity.getContent(), resultClass);
						return myObjects;
					} catch (IOException e) {
						e.printStackTrace();

					}
					return null;
				}
			});
		} finally {
			httpClient.close();
		}
	}

}
