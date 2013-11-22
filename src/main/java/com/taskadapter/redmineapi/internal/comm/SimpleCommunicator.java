package com.taskadapter.redmineapi.internal.comm;

import ch.boye.httpclientandroidlib.HttpRequest;
import com.taskadapter.redmineapi.RedmineException;

/**
 * Simple communicator interface.
 * 
 * @author maxkar
 * 
 */
public interface SimpleCommunicator<T> {
	/**
	 * Performs a request.
	 * 
	 * @return the response body.
	 */
	public abstract T sendRequest(HttpRequest request) throws RedmineException;

}
