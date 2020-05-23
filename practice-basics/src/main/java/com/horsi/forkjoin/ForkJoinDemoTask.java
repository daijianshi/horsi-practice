package com.horsi.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 *  fork join 任务类，实现一百一拆分，左闭右开
 * @author daijs
 * @since 2020/5/15 10:07
 */
public class ForkJoinDemoTask extends RecursiveTask<Integer> {

	private Integer start;
	private Integer end;

	public ForkJoinDemoTask(Integer start, Integer end) {
		this.start = start;
		this.end = end;
	}

	/**阈值**/
	private Integer threshold = 100;

	@Override
	protected Integer compute() {
		Integer sum = 0;
		if(end - start <= threshold) {
			System.out.println("未达到阈值，直接执行");
			sum = calculateSum(start,end);
		} else {
			System.out.println("进行拆分");
			int num = (end - start) / 100;
			List<ForkJoinDemoTask> taskList = new ArrayList<>(num);

			//自动拆分
			taskList.add(new ForkJoinDemoTask(start, start + (end - start) /2));
			taskList.add(new ForkJoinDemoTask(start + (end - start) /2 , end));
			invokeAll(taskList);
			for (ForkJoinDemoTask demoTask : taskList) {
				try {
					sum += demoTask.get();
				} catch (Exception e) {
					System.out.println("进行拆分时出现错误");
					e.printStackTrace();
				}
			}
		}

		return sum;
	}

	private Integer calculateSum(Integer start, Integer end) {
		int sum = 0;
		for(int i = start;i<=end-1;i++) {
			sum += i;
		}
		return sum;
	}
}
