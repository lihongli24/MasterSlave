package com.li.common.communication.master;

import com.li.common.communication.base.SlaveEntity;
import com.li.common.communication.base.SlavePool;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by lihongli on 15/11/22.
 */
public class SlaveNodeCacheListener implements NodeCacheListener {
	private NodeCache cache;
	private SlavePool socketPool;

	public SlaveNodeCacheListener(NodeCache cache, SlavePool socketPool) {
		this.cache = cache;
		this.socketPool = socketPool;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void nodeChanged() throws Exception {
		if (cache.getCurrentData() != null){
			byte[] nodeData = cache.getCurrentData().getData();
			String nodeDataStr = new String(nodeData, "utf-8");
			Map<String, String> tempMap = JSONObject.fromObject(nodeDataStr);
			String historyParam = tempMap.get("historyParam");
			String sign = "";
			if (StringUtils.isNotBlank(historyParam)) {
				Map<String, Object> cacheMap = JSONObject.fromObject(historyParam);
				List<String> signs = (List<String>) cacheMap.get("signs");
				if(signs != null){
					sign = signs.get(0);
				}
			}
			SlaveEntity socketEntity = socketPool.getSlavetEntity(sign);
			if(socketEntity != null){
				socketEntity.refreshSlaveInfo(nodeDataStr);
			}
		}
	}

}
