package com.yyl.common.threadPool;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * 处理线程池管理器
 * 该管理器交由spring进行管理
 * @author yangyl
 *
 */
@Service
public class ProcessExecutorMananger implements InitializingBean,DisposableBean{
	private Logger logger = LoggerFactory.getLogger(ProcessExecutorMananger.class);
	
	// 读取数据队列用线程池--定时调度线程池  
	private ScheduledExecutorService queueOrderPool;
	
	// 多线程处理模式用线程池--
	private ExecutorService multiSubmitPool;							// 处理线程池
	private CompletionService<ResultPackage> multiSubmitCompletionPool;	// 线程池结果缓存
	private ScheduledExecutorService multiSubmitResultService;			// 线程池结果读取
	
	/**
	 * bean构建时，创造并声明各线程池
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		queueOrderPool = Executors.newSingleThreadScheduledExecutor();
		multiSubmitPool = Executors.newFixedThreadPool(5);
		multiSubmitCompletionPool = new ExecutorCompletionService<ResultPackage>(multiSubmitPool);
		multiSubmitResultService = Executors.newScheduledThreadPool(5);
	}
	
	/**
	 * 注册处理结果的处理器接口，同时开启定时 读结果的线程
	 * @param resultTask
	 */
	public void registerSubmitResultService(IResultSolveService resultTask) {
		SubmitResultTask multiSubmitResultTask = new SubmitResultTask(multiSubmitCompletionPool, resultTask);
		// 启动结果处理器
		multiSubmitResultService.scheduleAtFixedRate(multiSubmitResultTask, 5000, 0, TimeUnit.SECONDS);
	}
	
	
	/**
	 * 在关闭系统时清理线程池
	 * TODO 关闭时处理尚在队列中的任务
	 */
	@Override
	public void destroy() throws Exception {
		queueOrderPool.shutdown();
		
		multiSubmitPool.shutdown();
		multiSubmitResultService.shutdown();
	}
	
	/**
	 * 启动订单队列扫描任务计划
	 * @param task
	 */
	public void schelduleQueueOrderScan(Runnable task) {
		queueOrderPool.scheduleAtFixedRate(task,0,5,TimeUnit.SECONDS);
	}
	
	/**
	 * 判断当前供应商线程池空闲情况
	 * @return
	 */
	public boolean processSupplierPoolisFree() {
		return true;
	}

	public ScheduledExecutorService getQueueOrderPool() {
		return queueOrderPool;
	}
	
	public CompletionService<ResultPackage> getMultiSubmitCompletionPool() {
		return multiSubmitCompletionPool;
	}

	
	/**
	 * 线程池处理结果的业务逻辑封装
	 * 由外部传递的IHttpSubmitResultService处理返回数据
	 * @author james.hu
	 *
	 */
	private class SubmitResultTask implements Runnable {
		// 指定的线程池缓存
		private CompletionService<ResultPackage> completionService;
		// 指定的返回数据处理器
		private IResultSolveService resultService;
		
		public SubmitResultTask(CompletionService<ResultPackage> completionService,
				IResultSolveService resultService) {
			this.completionService = completionService;
			this.resultService = resultService;
		}
		
		@Override
		public void run() {
			try {
				// 获取缓存的结果
				Future<ResultPackage> future = completionService.take();
				// 每5秒超时,进入下一个任务
				ResultPackage rp = future.get(5000,TimeUnit.SECONDS);
				// 日志处理
				resultService.solveResult();
			} catch (InterruptedException | ExecutionException e) {
				// 处理异常
				throw new RuntimeException();
			} catch (TimeoutException e) {
				logger.trace("HTTP结果处理线程超时,当前任务退出");
			}
			
		}
	}
}

