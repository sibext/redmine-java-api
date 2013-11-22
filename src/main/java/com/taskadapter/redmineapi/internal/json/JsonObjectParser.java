package com.taskadapter.redmineapi.internal.json;

import com.sibext.json.JSONException;
import com.sibext.json.JSONObject;

/**
 * Json object parser.
 * 
 * @author maxkar
 * 
 * @param <T>
 *            parsing result type.
 */
public interface JsonObjectParser<T> {
	public T parse(JSONObject input) throws JSONException;
}
