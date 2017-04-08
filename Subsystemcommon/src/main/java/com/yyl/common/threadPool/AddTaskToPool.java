package com.yyl.common.threadPool;

import java.util.concurrent.Callable;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;

public class AddTaskToPool implements Callable<ResultPackage> {
	//封装需要的参数
	private String url;
	
	private String jsonParameter;
	//具体要处理的业务
	@Override
	public ResultPackage call() throws Exception {
		// 进行任务的处理
		return new ResultPackage();
	}

}
//向线程池里提交任务
class sendTask implements InitializingBean {
	
	@Resource
    private ProcessExecutorMananger processExecutorMananger;
	
	@Resource
	private IResultSolveService resultSolveService;
	public void doTask() {
		processExecutorMananger.getMultiSubmitCompletionPool().submit(new AddTaskToPool());
	}
	@Override
	public void afterPropertiesSet() throws Exception {
		// 注入处理结果的service，并开启定时处理
		processExecutorMananger.registerSubmitResultService(resultSolveService);
	}
	
}
