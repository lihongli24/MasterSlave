package com.li.common.communication.slave;

import net.sf.json.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihongli on 15/11/22.
 */
public class RegistThread extends Thread {

	private final static Logger logger = LoggerFactory.getLogger(RegistThread.class);
	private final String parentPath;	//挂载的父路径
	private final String slaveIp;
	private final int slavePort;
	private final ISlaveInfo slaveInfo;
	private boolean runSign = true;
	private long sleepTime = 10000l;
	private CuratorFramework client;

	public RegistThread(String zookeeperAddress, String parentPath, String slaveIp, int slavePort, ISlaveInfo slaveInfo, long sleepTime) {
		this.parentPath = parentPath;
		this.slaveIp = slaveIp;
		this.slavePort = slavePort;
		this.sleepTime = sleepTime;
		this.slaveInfo = slaveInfo;
		client = CuratorFrameworkFactory.builder()
				.connectString(zookeeperAddress).sessionTimeoutMs(5000)
				.connectionTimeoutMs(3000)
				.retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
	}

	public void run() {
		String remotePath = parentPath + "/" + slaveIp + ":" + slavePort;
		logger.debug("try to regist to zookeeper path:{}", remotePath);
		try {
			sleep(sleepTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		Map<String, Object> childData = new HashMap<String, Object>();
		String childDataJson = "";
		try {
			childData.put("slaveIp", slaveIp);
			childData.put("slavePort", slavePort);
			childData.put("historyParam", StatusCache.getStatusJSON());
			childDataJson = JSONObject.fromObject(childData).toString();
			client.start();
			client.create().creatingParentsIfNeeded()
					.withMode(CreateMode.EPHEMERAL)
					.forPath(remotePath, childDataJson.getBytes("utf-8"));
			logger.info("regist success, the ip is {}, port is {}", slaveIp, slavePort);
		} catch (Exception e) {
			logger.error("regist unsuccess......", e);
		}

		int count = 0;
		while(runSign){
			try {
				if(count == 80000){
					childData.putAll(slaveInfo.getMessage("aa"));
					childDataJson = JSONObject.fromObject(childData).toString();
					//logger.info("begin setdata for path {}, childDataJson is {}", remotePath, childDataJson);
					try {
						client.setData().forPath(remotePath, childDataJson.getBytes("utf-8"));
					} catch (KeeperException.NoNodeException e) {//在setData的情况下出现session过期的情况，自动创建节点
						client.create().creatingParentsIfNeeded()
								.withMode(CreateMode.EPHEMERAL)
								.forPath(remotePath, childDataJson.getBytes("utf-8"));
					}
					count = 0;
				}else{
					count ++;
				}
			} catch (Exception e) {
				logger.error("get slaveInfo fail", e);
			}
		}

	}

	public void stopThread() {
		runSign = false;
	}
}
