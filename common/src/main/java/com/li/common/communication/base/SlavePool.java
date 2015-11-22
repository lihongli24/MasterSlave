package com.li.common.communication.base;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by lihongli on 15/11/21.
 */
public class SlavePool {

	private static final Logger logger = LoggerFactory.getLogger(SlavePool.class);

	private SlaveComparator slaveComparator;
	private List<SlaveEntity> slavetEntityList = new ArrayList<SlaveEntity>();
	private Map<String, SlaveEntity> signToSlaveMap = new HashMap<String, SlaveEntity>();
	private Map<SlaveEntity, List<String>> SlaveToSignMap = new HashMap<SlaveEntity, List<String>>();
	private CommunicationListener listener;

	public SlavePool() {
	}

	public SlavePool(CommunicationListener listener) {
		this.listener = listener;
	}

	public String sendMessage(String sign, String message) throws Exception {
		SlaveEntity slavetEntity = signToSlaveMap.get(sign);
		if (slavetEntity == null)
			slavetEntity = getNewSocket(sign);
		if (slavetEntity == null)
			throw new Exception("there is no slave online.");
		return slavetEntity.sendMessage(message);
	}

	/**
	 * 一次性设置socketpool
	 * @param socketInfos	key分别为：slaveIp、slavePort、historyParam的List
	 */
	@SuppressWarnings("unchecked")
	public synchronized void putAllSocket(List<Map<String, String>> socketInfos){
		logger.debug("begin putAllSocket socketInfos is {}", socketInfos);
		SlaveToSignMap.clear();
		signToSlaveMap.clear();
		slavetEntityList.clear();
		if(socketInfos != null && socketInfos.size() > 0){
			for (Map<String, String> map : socketInfos) {
				if(map != null){
					String slaveIp = map.get("slaveIp");
					Integer slavePort = Integer.valueOf(map.get("slavePort"));
					String historyParam = map.get("historyParam");
					SlaveEntity slavetEntity = null;
					if (StringUtils.isNotBlank(historyParam)) {
						Map<String, Object> cacheMap = JSONObject.fromObject(historyParam);
						List<String> signs = (List<String>) cacheMap.get("signs");
						if(signs != null && signs.size() == 1)
							slavetEntity = new SlaveEntity(signs.get(0), slaveIp, slavePort);
						else
							slavetEntity = new SlaveEntity(null, slaveIp, slavePort);

						SlaveToSignMap.put(slavetEntity, signs);
						for (String sign : signs) {
							signToSlaveMap.put(sign, slavetEntity);
						}
					}
					slavetEntityList.add(slavetEntity);
				}
			}
		}
		if(listener != null){
			listener.refreshSlaves(slavetEntityList);
		}
	}

	/**
	 * 根据sign获取对应的socketEntiy
	 * @param sign
	 * @return
	 */
	public SlaveEntity getSlavetEntity(String sign){
		return signToSlaveMap.get(sign);
	}

	private SlaveEntity getNewSocket(String sign) {
		List<SlaveEntity> socketEntities = getAllSocket();
		if(socketEntities == null || socketEntities.size() == 0)
			return null;
		SlaveEntity slavetEntity = socketEntities.get(0);
		signToSlaveMap.put(sign, slavetEntity);
		logger.info("getNewSocket {} for sign {}", slavetEntity.toString(), sign);
		return slavetEntity;
	}

	public List<SlaveEntity> getAllSocket() {
		Collections.sort(slavetEntityList, slaveComparator);
		return slavetEntityList;
	}

	public Boolean HasSocket() {
		if (slavetEntityList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void setComparator(SlaveComparator slaveComparator) {
		this.slaveComparator = slaveComparator;
	}

}
