package com.li.slave.communication;

import com.li.common.communication.slave.SlaveMainThread;
import com.li.common.communication.slave.StatusCache;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by lihongli on 15/11/22.
 */
@Component
@ConfigurationProperties
public class SlaveBean {


	@Value("${communication.zookeeper.zookeeperAddress}")
	private static  String zookeeperAddress;

	@Value("${communication.zookeeper.listener.path}")
	private static String parentListenerPath;

	private Long sleepTime = 1000l;

	public static String sign;

	public SlaveBean(){
		sign = RandomStringUtils.random(10,"ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		StatusCache.addSign(sign);
		zookeeperAddress = "localhost:2181";
		parentListenerPath = "/my/listenerPath";
		SlaveMainThread.startSlave(zookeeperAddress, parentListenerPath, Application.slaveIp, Application.slavePort, sleepTime, new SlaveInfo());
	}

	public static void stopSlave() {
		SlaveMainThread.thread.stopThread();
	}

}
