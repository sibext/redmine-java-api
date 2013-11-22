package com.taskadapter.redmineapi.internal.comm;

import com.taskadapter.redmineapi.RedmineConfigurationException;
import com.taskadapter.redmineapi.internal.comm.naivessl.NaiveSSLFactory;
import ch.boye.httpclientandroidlib.Header;
import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpHost;
import ch.boye.httpclientandroidlib.HttpVersion;
import ch.boye.httpclientandroidlib.auth.AuthScope;
import ch.boye.httpclientandroidlib.auth.UsernamePasswordCredentials;
import ch.boye.httpclientandroidlib.conn.ClientConnectionManager;
import ch.boye.httpclientandroidlib.conn.scheme.PlainSocketFactory;
import ch.boye.httpclientandroidlib.conn.scheme.Scheme;
import ch.boye.httpclientandroidlib.conn.scheme.SchemeRegistry;
import ch.boye.httpclientandroidlib.conn.ssl.SSLSocketFactory;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.impl.conn.PoolingClientConnectionManager;
import ch.boye.httpclientandroidlib.params.BasicHttpParams;
import ch.boye.httpclientandroidlib.params.HttpParams;
import ch.boye.httpclientandroidlib.params.HttpProtocolParams;
import ch.boye.httpclientandroidlib.protocol.HTTP;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

class HttpUtil {
	public static DefaultHttpClient getNewHttpClient(
			ClientConnectionManager connectionManager) {
		try {

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			final DefaultHttpClient result = new DefaultHttpClient(
					connectionManager, params);
			configureProxy(result);
			return result;
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	static PoolingClientConnectionManager createConnectionManager(
			int maxConnections) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException,
			KeyManagementException, UnrecoverableKeyException {
        SSLSocketFactory factory = NaiveSSLFactory.createNaiveSSLSocketFactory();

        SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		registry.register(new Scheme("https", 443, factory));

        PoolingClientConnectionManager manager = new PoolingClientConnectionManager(registry);
		manager.setMaxTotal(maxConnections);
		manager.setDefaultMaxPerRoute(maxConnections);
		return manager;
	}

	private static void configureProxy(DefaultHttpClient httpclient) {
		String proxyHost = System.getProperty("http.proxyHost");
		String proxyPort = System.getProperty("http.proxyPort");
		if (proxyHost != null && proxyPort != null) {
			int port;
			try {
				port = Integer.parseInt(proxyPort);
			} catch (NumberFormatException e) {
				throw new RedmineConfigurationException("Illegal proxy port "
						+ proxyPort, e);
			}
			HttpHost proxy = new HttpHost(proxyHost, port);
			httpclient.getParams().setParameter(
					ch.boye.httpclientandroidlib.conn.params.ConnRoutePNames.DEFAULT_PROXY,
					proxy);
			String proxyUser = System.getProperty("http.proxyUser");
			if (proxyUser != null) {
				String proxyPassword = System.getProperty("http.proxyPassword");
				httpclient.getCredentialsProvider().setCredentials(
						new AuthScope(proxyHost, port),
						new UsernamePasswordCredentials(proxyUser,
								proxyPassword));
			}
		}
	}

	/**
	 * Returns entity encoding.
	 * 
	 * @param entity
	 *            entitity to get encoding.
	 * @return entity encoding string.
	 */
	public static String getEntityEncoding(HttpEntity entity) {
		final Header header = entity.getContentEncoding();
		if (header == null)
			return null;
		return header.getValue();
	}

	/**
	 * Returns entity charset to use.
	 * 
	 * @param entity
	 *            entity to check.
	 * @return entity charset to use in decoding.
	 */
	public static String getCharset(HttpEntity entity) {
		final String guess = EntityUtils.getContentCharSet(entity);
		return guess == null ? HTTP.DEFAULT_CONTENT_CHARSET : guess;
	}
}
