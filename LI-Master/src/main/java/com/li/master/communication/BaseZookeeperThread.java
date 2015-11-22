package com.li.master.communication;

import com.li.common.communication.base.SlaveComparator;
import com.li.common.communication.base.SlavePool;
import com.li.common.communication.master.MasterZookeeperThread;

/**
 * Created by lihongli on 15/11/22.
 */
public class BaseZookeeperThread extends MasterZookeeperThread {

	private SlavePool baseSlavePool = new SlavePool();


	public BaseZookeeperThread(String zookeeperAddress, String listenedPath){
		super(zookeeperAddress, listenedPath);
	}


	@Override
	protected SlaveComparator getSlaveComparator() {
		return null;
	}

	@Override
	protected SlavePool getSlavePool() {
		return baseSlavePool;
	}
}
