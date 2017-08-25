package com.snail.webgame.game.thread;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.cache.GamePatchMap;
import com.snail.webgame.game.config.GameConfig;
import com.snail.webgame.game.core.GamePatch;
import com.snail.webgame.game.pvp.competition.handler.ServerHandler;

public class PatchLoadThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger("logs");
	
	@Override
	public void run() {
		String basePath = null;
		
		if (System.getProperty("user.dir") != null) {
			basePath = System.getProperty("user.dir");
		} else {
			basePath = PatchLoadThread.class.getResource("/").getPath();
		}
		
		if (basePath.endsWith(File.separator)) {
			basePath += "patch";
		} else {
			basePath += File.separator + "patch";
		}
		
		logger.warn("PatchLoadThread : basePath = " + basePath);
		
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(10);
				
				// 检查路径下版本文件
				File file = new File(basePath);
				
				if (file.exists() && file.isDirectory()) {
					File[] jarFiles = file.listFiles(new FilenameFilter() {
						
						@Override
						public boolean accept(File dir, String name) {
							if (name.endsWith("jar") && name.contains(GameConfig.getInstance().getVersion())) {
								// 只读取jar文件中版本号一致的文件
								return true;
							} else {
								return false;
							}
						}
					});
					
					if (jarFiles != null && jarFiles.length > 0) {
						for (File file2 : jarFiles) {
							try {
								if (file2 != null && file2.exists() && file2.isFile()) {
									// jar文件名格式必需为:类名-版本号.jar(SellPatch-0.0.1.jar)
									String[] strs = file2.getName().split("-");
									
									if (strs == null || strs.length != 2) {
										logger.error("PatchLoadThread : error " + file2.getName() + " format error");
										continue;
									}
									
									String className = "com.snail.webgame.game.patch." + strs[0];
									
									if (!GamePatchMap.checkJar(file2.getName(), className)) {
										// 已经加载过，不能再加载
										continue;
									}
									
									try {
										Class.forName(className);
									} catch (Exception e) {
										try {
									    	Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);  
											method.setAccessible(true);
									    	
									        URLClassLoader classLoader = (URLClassLoader) ServerHandler.class.getClassLoader();
									        method.invoke(classLoader, file2.toURI().toURL());
									        
									        Class.forName(className);
									        
									        GamePatchMap.addGamePatchJar(file2.getName(), className);
									    } catch (Exception e1) {
									    	logger.error("PatchLoadThread : error " + file2.getName() + " load failed", e1);
										}
									}
									
									try {
										GamePatch gamePatch = (GamePatch)ServerHandler.class.getClassLoader().loadClass(className).newInstance();
										gamePatch.runPatch();
									} catch (Exception e1) {
										logger.error("PatchLoadThread : error " + className + " run failed", e1);
									}
									
									logger.warn("load jar " + file2.getName() + " successed");
								}
							} catch (Exception e) {
								logger.error("PatchLoadThread : error " + file2.getName() + " read failed");
							}
						}
					}
				} else {
					file.mkdir();
					logger.warn("PatchLoadThread : basePath = " + basePath + " was be created");
				}
			} catch (Exception e) {
				logger.error("PatchLoadThread : error unknow error", e);
			}
		}
	}
	
	public static void main(String[] args) {
		PatchLoadThread t = new PatchLoadThread();
		
		t.run();
	}
}

