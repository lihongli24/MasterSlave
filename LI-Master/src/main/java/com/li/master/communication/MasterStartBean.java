package com.li.master.communication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by lihongli on 15/11/22.
 */
@Component
public class MasterStartBean {

	@Value("${communication.zookeeper.zookeeperAddress}")
	private static String zookeeperAddress;

	@Value("${communication.zookeeper.listener.path}")
	private static String parentListenerPath;

	public MasterStartBean(){
		BaseZookeeperThread zookeeperThread = new BaseZookeeperThread(zookeeperAddress, parentListenerPath);
		zookeeperThread.start();
	}



}
