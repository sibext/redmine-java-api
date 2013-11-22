package com.taskadapter.redmineapi.internal.json;

import com.sibext.json.JSONException;
import com.sibext.json.JSONWriter;

/**
 * Json object writer.
 * 
 * @author maxkar
 * 
 */
public interface JsonObjectWriter<T> {
	public void write(JSONWriter writer, T object) throws JSONException;
}
