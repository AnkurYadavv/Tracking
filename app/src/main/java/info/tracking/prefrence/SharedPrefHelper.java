package info.tracking.prefrence;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedPrefHelper {
	private static final String PREF_FILE = "Session";
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	private static Map<Context, SharedPrefHelper> instances = new HashMap<Context, SharedPrefHelper>();
	
	
	public void clear()
	{
	     settings.edit();
	     editor.clear();
	     editor.commit();
	}
	
	public SharedPrefHelper(Context context)
	{
		settings = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		editor = settings.edit();
	}
	public static SharedPrefHelper getInstance(Context context) {
		if (!instances.containsKey(context))
			instances.put(context, new SharedPrefHelper(context));
		return instances.get(context);
	}
	 
	public String getString(String key, String defValue)
	{
		return settings.getString(key, defValue);
	}
	public SharedPrefHelper setString(String key, String value)
	{
		editor.putString(key, value);
		editor.commit();
		return this;
	}
	public int getInt(String key, int defValue)
	{
		return settings.getInt(key, defValue);
	}
	public SharedPrefHelper setInt(String key, int value)
	{
		editor.putInt(key, value);
		editor.commit();
		return this;
	}

	public boolean getBoolean(String key, boolean defValue)
	{
		return settings.getBoolean(key, defValue);
	}

	public SharedPrefHelper setBoolean(String key, boolean value)
	{
		editor.putBoolean(key, value);
		editor.commit();
		return this;
	}
	
	
	
}
