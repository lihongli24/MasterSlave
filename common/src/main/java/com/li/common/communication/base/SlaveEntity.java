package com.li.common.communication.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihongli on 15/11/21.
 * 节点信息
 */
@SuppressWarnings("serial")
public class SlaveEntity implements Serializable{

	private Logger logger = LoggerFactory.getLogger(SlaveEntity.class);

	private String sign;
	private String slaveIp;
	private Integer slavePort;
	private Map<String, Object> slaveInfoMap = new HashMap<String, Object>();

	public SlaveEntity(String sign, String slaveIp, Integer slavePort){
		this.sign = sign;
		this.slaveIp = slaveIp;
		this.slavePort = slavePort;
	}

	public String getSign() {
		return sign;
	}

	public Map<String, Object> getSlaveInfoMap() {
		return slaveInfoMap;
	}

	public String getSlaveIp() {
		return slaveIp;
	}

	public Integer getSlavePort() {
		return slavePort;
	}

	public void clearSlaveInfoMap() {
		this.slaveInfoMap.clear();
	}

	public void setSlaveInfo(String key, Object value) {
		this.slaveInfoMap.put(key, value);
	}

	public void removeSlaveInfo(String key) {
		this.slaveInfoMap.remove(key);
	}

	@SuppressWarnings("unchecked")
	public void refreshSlaveInfo(String jsonStr) {
		Map<String, Object> tempMap = JSONObject.fromObject(jsonStr);
		Map<String, Object> newSlaveInfoMap = new HashMap<String, Object>();
		for(String key : tempMap.keySet()) {
			newSlaveInfoMap.put(key, tempMap.get(key));
		}
		this.slaveInfoMap = newSlaveInfoMap;
		logger.debug("slaveInfo:{}", this.toString());
	}

	public String sendMessage(String message) throws Exception {
	/*	Socket socket = null;
		BufferedReader reader = null;
		PrintWriter writer = null;

		try {
			socket = new Socket(slaveIp, slavePort + 1);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			writer = new PrintWriter(socket.getOutputStream());
			writer.println(message);
			writer.flush();
			String result = reader.readLine();
			return result;
		} catch (Exception e) {
			logger.error("socketEntity sendMessage error", e);
			throw new Exception(e);
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
				if (socket != null)
					socket.close();
			} catch (Exception e) {
				logger.error("socketEntity close error", e);
			}
		}*/
		return "";
	}

	public String toString() {
		return "[" + this.slaveIp + ":" + this.slavePort + "/" + (this.slavePort + 1) + "  " + slaveInfoMap.toString() + "]";
	}


}
