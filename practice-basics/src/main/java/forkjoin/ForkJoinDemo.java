package com.horsi.forkjoin;

import java.util.concurrent.ForkJoinPool;

/**
 *  fork join demo类
 *  知识点，任务拆分，工作窃取
 *
 *  使用步骤
 *  1：创建fork join连接池
 *  2：创建任务类，继承RecursiveTask（带返回值）或者RecursiveAction（不带返回值）
 *  3：自定义任务类中的compute方法，根据规则进行拆解
 *  4：任务类中invokeAll提交任务，通过join等待获取返回结果
 *  5：通过fork join线程调用任务，获取返回结果
 *
 *  实现需求：
 * 计算连续整数之和
 * @author daijs
 * @since 2020/5/15 10:06
 */
public class ForkJoinDemo {

	/**
	 * 获取fork join池 10个线程
	 */
	private static ForkJoinPool forkJoinPool = new ForkJoinPool (10, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

	public static Integer calculateTotal(Integer start, Integer end) {

		ForkJoinDemoTask demoTask = new ForkJoinDemoTask(start,end);
		forkJoinPool.submit(demoTask);

		return demoTask.join();
	}

	public static void main(String[] args) {
		System.out.println("计算结果是"+calculateTotal(100,501));
	}

}
