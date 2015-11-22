package com.li.common.communication.master;

import com.li.common.communication.base.SlaveComparator;
import com.li.common.communication.base.SlavePool;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lihongli on 15/11/22.
 */
public abstract class MasterZookeeperThread extends Thread{
	private String listenedPath;    //需要监听的地址
	private CuratorFramework client;    //连接zookeeper的客户端

	private SlavePool slavePool;
	private SlaveComparator slaveComparatorImpl;

	private Logger logger = LoggerFactory.getLogger(MasterZookeeperThread.class);

	public MasterZookeeperThread(String zookeeperAddress, String listenedPath){
		this.listenedPath = listenedPath;
		client = CuratorFrameworkFactory.builder()
				.connectString(zookeeperAddress).sessionTimeoutMs(5000)
				.connectionTimeoutMs(3000)
				.retryPolicy(new ExponentialBackoffRetry(1000,3)).build();

		slavePool = getSlavePool();
		this.slaveComparatorImpl = getSlaveComparator();
		this.slavePool.setComparator(slaveComparatorImpl);
	}

	@Override
	public void run() {
		client.start();
		PathChildrenCache cache = new PathChildrenCache(client, listenedPath, true);

		try{
			cache.start();
		}catch (Exception e){
			logger.error("error", e);
		}
		cache.getListenable().addListener(new SlavePathCacheListener(listenedPath, slavePool));
	}

	abstract protected SlaveComparator getSlaveComparator();


	protected SlavePool getSlavePool() {
		return slavePool;
	}


	protected void setSlavePool(SlavePool slavePool) {
		this.slavePool = slavePool;
	}

}
