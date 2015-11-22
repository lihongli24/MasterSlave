package com.li.slave.communication;

import com.li.common.communication.slave.ISlaveInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lihongli on 15/11/22.
 */
public class SlaveInfo implements ISlaveInfo {

	@Override
	public Map<String, Object> getMessage(String heartBeatStr) {
		return new HashMap<String, Object>();
	}
}
