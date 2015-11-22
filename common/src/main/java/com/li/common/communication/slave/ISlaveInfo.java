package com.li.common.communication.slave;

import java.util.Map;

/**
 * Created by lihongli on 15/11/22.
 */
public interface ISlaveInfo {
	Map<String, Object> getMessage(String heartBeatStr);
}
