// Modified or written by Luca Marrocco for inclusion with hoptoad.
// Copyright (c) 2009 Luca Marrocco.
// Licensed under the Apache License, Version 2.0 (the "License")

package code.lucamarrocco.hoptoad;

import java.io.*;
import java.net.*;

public class HoptoadNotifier {

	private void addingProperties(final HttpURLConnection connection) throws ProtocolException {
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-type", "text/xml");
		connection.setRequestProperty("Accept", "text/xml, application/xml");
		connection.setRequestMethod("POST");
	}

	private HttpURLConnection createConnection(HoptoadNotice notice) throws IOException {
        String host = (notice.getHost() != null) ? notice.getHost() : "hoptoadapp.com" ;
        String port = (notice.getPort()  != null) ? notice.getPort() : "80" ;
        String protocol = notice.isSecure() ? "https" : "http" ;
		final HttpURLConnection connection = (HttpURLConnection) new URL( protocol + "://" + host + ":" + port + "/notifier_api/v2/notices").openConnection();
		return connection;
	}

	private void err(final HoptoadNotice notice, final Exception e) {
		System.out.println(notice.toString());
		e.printStackTrace();
	}

	public int notify(final HoptoadNotice notice) {
		try {
			final HttpURLConnection toHoptoad = createConnection(notice);
			addingProperties(toHoptoad);
      String toPost = new NoticeApi2(notice).toString();
      return send(toPost, toHoptoad);
		} catch (final Exception e) {
			err(notice, e);
		}
		return 0;
	}

	private int send(final String yaml, final HttpURLConnection connection) throws IOException {
		int statusCode;
		final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(yaml);
		writer.close();

		statusCode = connection.getResponseCode();
		return statusCode;
	}

}
