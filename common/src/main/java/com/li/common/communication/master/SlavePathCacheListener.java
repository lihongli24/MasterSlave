package com.li.common.communication.master;

import com.li.common.communication.base.SlavePool;
import net.sf.json.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lihongli on 15/11/22.
 */
public class SlavePathCacheListener implements PathChildrenCacheListener{

	private String listenedPath;
	private Logger logger = LoggerFactory.getLogger(SlavePathCacheListener.class);
	private SlavePool slavePool;

	public SlavePathCacheListener(String path, SlavePool slavePool){
		this.listenedPath = path;
		this.slavePool = slavePool;
	}


	@Override
	public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
		switch (pathChildrenCacheEvent.getType()){
			case CHILD_ADDED:
				addNewListener(curatorFramework, pathChildrenCacheEvent);
				updateSocketPool(listenedPath, curatorFramework);
				break;
			case CHILD_REMOVED:
				updateSocketPool(listenedPath, curatorFramework);
				break;
			default:
				break;
		}
	}


	/**
	 * 更新当前主应用的socketPool中的当前连接
	 * @param client
	 * @param listenedPath
	 */
	private void updateSocketPool(String listenedPath, CuratorFramework client) {
		List<String> childrenList = new ArrayList<String>();
		try {
			childrenList = client.getChildren().forPath(listenedPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<String> childrenDataStr = getDataByPath(client, childrenList);
		logger.debug("the method updateSocketPool the childrenDataStr is {}, slavePool is {}", childrenDataStr, slavePool);
		List<Map<String, String>> childrenDataMap = changeDataStrToMap(childrenDataStr);
		slavePool.putAllSocket(childrenDataMap);
	}

	/**
	 * 将zookeeper中子路径中的数据转化成Map
	 * @param childrenDataStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> changeDataStrToMap(List<String> childrenDataStr) {
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		if(childrenDataStr != null){
			for (String childData : childrenDataStr) {
				Map<String, String> tempMap = JSONObject.fromObject(childData);
				Map<String, String> childDataMap = new HashMap<String, String>();
				for(String key : tempMap.keySet()) {
					Object value = tempMap.get(key);
					if(value != null){
						childDataMap.put(key, value.toString());
					}
				}
				result.add(childDataMap);
			}
		}
		return result;
	}

	/**
	 *
	 * @param client
	 * @param childrenList
	 * @return
	 */
	private List<String> getDataByPath(CuratorFramework client, List<String> childrenList) {
		logger.debug("getDataByPath begin childrenList is {}", childrenList);
		List<String> childrenData = new ArrayList<String>();
		if(childrenList != null ){
			for (String childPath : childrenList) {
				String currentPath = listenedPath + "/" + childPath;
				try {
					byte[] data = client.getData().forPath(currentPath);
					childrenData.add(new String(data, "utf-8"));
				} catch (Exception e) {
					logger.error("can't get the data of path {}", currentPath);
				}
			}
		}
		logger.debug("getDataByPath end childrenData is {}", childrenData);
		return childrenData;
	}

	/**
	 * 给新增的接口增加node监听
	 * @param event
	 */
	private void addNewListener(CuratorFramework client, PathChildrenCacheEvent event) {
		String childPath = event.getData().getPath();			//新增的子节点的路径
		NodeCache cache = new NodeCache(client, childPath);
		try {
			cache.start();
		} catch (Exception e) {
			logger.error("the method addNewListener in class SlavePathCacheListener throw Exception", e);
		}
		cache.getListenable().addListener(new SlaveNodeCacheListener(cache, slavePool));
	}
}
