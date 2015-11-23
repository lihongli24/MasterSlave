package com.li.slave.communication.config;

import com.li.slave.communication.SlaveBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Created by lihongli on 15/11/23.
 */
public class SlaveBeanConfig {

	@Value("${communication.zookeeper.zookeeperAddress}")
	private String zookeeperAddress;

	@Value("${communication.zookeeper.listener.path}")
	private String parentListenerPath;

	@Bean
	public SlaveBean slaveBean(){
		return new SlaveBean(zookeeperAddress, parentListenerPath);
	}
}
