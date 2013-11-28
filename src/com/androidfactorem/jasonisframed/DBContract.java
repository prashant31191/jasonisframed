package com.androidfactorem.jasonisframed;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DBContract {

	private static final String FuzzProductionsUrl = "http://www.bergenchessmates.com/test.json";

	private static final int HTTP_STATUS_OK = 200;
	private static byte[] buffr = new byte[1024];

	public static class ApiException extends Exception {
		private static final long serialVersionUID = 1L;

		public ApiException(String errormsg) {
			super(errormsg);
		}

		public ApiException(String errormsg, Throwable thrw) {
			super(errormsg, thrw);
		}
	}

	protected static synchronized String downloadedJSON(String... params)
			throws ApiException {
		String returnval = null;
		String url = FuzzProductionsUrl;

		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		try {
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {

				throw new ApiException(
						"Invalid response from test website"
								+ status.toString());
			}

			HttpEntity entity = response.getEntity();
			InputStream inStrm = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();

			int readCount = 0;
			while ((readCount = inStrm.read(buffr)) != -1) {
				content.write(buffr, 0, readCount);
			}
			returnval = new String(content.toByteArray());

		} catch (Exception e) {
			throw new ApiException(
					"Problem connecting to test JSON file "
							+ e.getMessage(), e);
		}

		return returnval;
	}
}
