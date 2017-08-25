package com.snail.webgame.engine.gate.threadpool;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.dom4j.Document;
import org.dom4j.Element;

import com.snail.webgame.engine.common.util.XMLUtil4DOM;

public class ThreadPoolManager {
    private static ThreadPoolManager thisInstance;

    private static HashMap<String, ThreadPoolExecutor> poolsMap; 
    private static String filePath = "poolConfig.xml";
    private ThreadPoolConfig [] poolConfigs = null;
    private ThreadPoolManager()
    {
    	poolsMap = new HashMap<String, ThreadPoolExecutor> ();
    	Load();
    	initThreadPool();
    }
    public static synchronized ThreadPoolManager getInstance () {
    	
    	if(thisInstance==null)
    	{
    		thisInstance = new ThreadPoolManager();
    	}
        return thisInstance;
    }
    /**
     * 获得线程池
     * @param poolName
     * @return
     */
    public  synchronized ThreadPoolExecutor Pool(String poolName)
    {
    
    	return  poolsMap.get(poolName);
    	
    }
    private void initThreadPool()
    {
    	for(int i=0;i<poolConfigs.length;i++)
    	{
    		ThreadPoolConfig poolConfig = poolConfigs[i];
    		BlockingQueue<Runnable> query = null;
    		if(poolConfig.getQueueType().equals("1"))
    		{
    			
    			if(poolConfig.getMaxQueues().length()==0)
    			{
    				query = new LinkedBlockingQueue<Runnable>();
    			}
    			else
    			{
    				query = new LinkedBlockingQueue<Runnable>(Integer.parseInt(poolConfig.getMaxQueues()));
    			
    			}

    		}
    		else if(poolConfig.getQueueType().equals("2"))
    		{
    			
    		}
    		
			ThreadPoolExecutor threadPool =new ThreadPoolExecutor(
					Integer.parseInt(poolConfig.getMinThreads()),
					Integer.parseInt(poolConfig.getMaxThreads()),
		            Long.parseLong(poolConfig.getKeepAlive()), TimeUnit.SECONDS,
		            query, new ThreadPoolExecutor.CallerRunsPolicy());
			threadPool.allowCoreThreadTimeOut(true);
			poolsMap.put(poolConfig.getPoolName(), threadPool);
    	}
    }

	private void Load()
	{
		File file = new File(filePath);
		if(file.exists())
		{
			ReadConfig(filePath);
		}
		else	
		{
			 System.out.println("System can not find threadpool Config!");
		}
		
	}
	
    private void  ReadConfig (String fileName) {
        File file = new File (fileName);
        
        if (file.exists()) {
            try {
                Document doc = XMLUtil4DOM.file2Document (file);
                Element rootEle = null;
                if (doc != null) {
                    rootEle = doc.getRootElement();
                    List poolConfigEles = rootEle.elements("pool");
                    if (poolConfigEles != null) {
                        int configCount = poolConfigEles.size();
                        poolConfigs = new ThreadPoolConfig[configCount];
                        for (int i = 0; i < configCount; i++) {
                            Element tempElement = (Element) poolConfigEles.get(i);
                            ThreadPoolConfig configVo = new ThreadPoolConfig();
                            configVo.setMaxQueues(tempElement.element("maxQueues").getText().trim());
                            configVo.setMaxThreads(tempElement.element("maxThreads").getText().trim());
                            configVo.setMinThreads(tempElement.element("minThreads").getText().trim());
                            configVo.setPoolName(tempElement.element("poolName").getText().trim());
                            configVo.setPriority(tempElement.element("priority").getText().trim());
                            configVo.setKeepAlive(tempElement.element("keepAlive").getText().trim());
                            configVo.setQueueType(tempElement.element("queueType").getText().trim());
                            poolConfigs[i] = configVo;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
    }
}
