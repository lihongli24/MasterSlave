package com.li.common.communication.slave;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihongli on 15/11/22.
 */
public class StatusCache {

	private static Map<String, Object> cacheMap = new HashMap<String, Object>();
	private static List<String> signs = new ArrayList<String>();

	static {
		cacheMap.put("signs", signs);
	}

	public static String getStatusJSON() {
		return JSONObject.fromObject(cacheMap).toString();
	}

	public static void addSign(String sign) {
		if(!signs.contains(sign))
			signs.add(sign);
	}

	public static void removeSign(String sign) {
		if(signs.contains(sign))
			signs.remove(sign);
	}

	public static void addCache(String key, Object value) {
		cacheMap.put(key, value);
	}

	public static void removeCache(String key) {
		cacheMap.remove(key);
	}

	public static Object getCache(String key) {
		return cacheMap.get(key);
	}

}
