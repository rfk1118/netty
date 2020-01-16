package chapterSeven;

import java.util.concurrent.*;

/**
 * @author 代码7-2
 */
public class ScheduledExecutorServiceDemo {

    public static void main(String[] args) {
        // 创建以一个其线程池具有10个线程的ScheduledExecutorService
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

        // 调度任务在从现在开始的60秒后执行
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule(new Runnable() {
            public void run() {
                // 该任务要打印消息
                System.out.println("60 seconds later");
            }
        }, 60, TimeUnit.SECONDS);


        // 一旦调度任务执行完成,就关闭scheduledExecutorService以释放资源
        scheduledExecutorService.shutdown();
    }
}
