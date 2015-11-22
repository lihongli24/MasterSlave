package com.li.common.communication.slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lihongli on 15/11/22.
 */
public class SlaveMainThread extends Thread{
	private static final Logger logger = LoggerFactory.getLogger(SlaveMainThread.class);
	private String parentPath;
	private String slaveIp;
	private Integer slavePort;
	private Long sleepTime;
	private ISlaveInfo slaveInfo;
	private RegistThread registThread = null;
	private String zookeeperAddress = null;

	public SlaveMainThread(String zookeeperAddress, String parentPath, String slaveIp, Integer slavePort, Long sleepTime, ISlaveInfo slaveInfo){
		this.zookeeperAddress = zookeeperAddress;
		this.parentPath = parentPath;
		this.slaveIp = slaveIp;
		this.slavePort = slavePort;
		this.sleepTime = sleepTime;
		this.slaveInfo = slaveInfo;
	}

	@Override
	public void run() {
		try{
			registThread = new RegistThread(zookeeperAddress,parentPath, slaveIp, slavePort, slaveInfo, sleepTime);
			registThread.start();
		}catch (Exception e){
			logger.info("connection close......", e);
			try{
				if(registThread != null){
					registThread.stopThread();
				}
			}catch (Exception ea){
				logger.error("connection close error", ea);
			}

		}
	}

	public void stopThread() {
		try {
			if (registThread != null)
				registThread.stopThread();
			Thread.sleep(500l);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.interrupt();
	}

	public static SlaveMainThread thread;

	public static void startSlave(String zookeeperAddress, String parentPath, String slaveIp, Integer slavePort, Long sleepTime, ISlaveInfo slaveInfo) {
		thread = new SlaveMainThread(zookeeperAddress, parentPath, slaveIp, slavePort, sleepTime, slaveInfo);
		thread.start();
	}
}
