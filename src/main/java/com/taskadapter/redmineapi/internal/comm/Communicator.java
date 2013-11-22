package com.taskadapter.redmineapi.internal.comm;

import ch.boye.httpclientandroidlib.HttpRequest;
import com.taskadapter.redmineapi.RedmineException;

public interface Communicator<K> {

	/**
	 * Performs a request.
	 * 
	 * @return the response body.
	 */
	public abstract <R> R sendRequest(HttpRequest request,
			ContentHandler<K, R> contentHandler) throws RedmineException;

}