package com.li.common.communication.base;

import java.util.List;

/**
 * Created by lihongli on 15/11/21.
 */
public interface CommunicationListener {

	void refreshSlaves(List<SlaveEntity> slaveEntityList);
}
