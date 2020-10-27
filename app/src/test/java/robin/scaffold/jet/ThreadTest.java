package robin.scaffold.jet;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import kotlinx.coroutines.scheduling.Task;

public class ThreadTest {
    @Test
    public void main() {
        System.out.println("start");
        ExecutorService executor = Executors.newFixedThreadPool(4);
// 定义任务:
        Callable<String> task = new Task();
// 提交任务并获得Future:
        Future<String> future = executor.submit(task);
// 从Future获取异步执行返回的结果:
        try {
            System.out.println("start1");
            String result = future.get(); // 可能阻塞
            System.out.println(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class Task implements Callable<String> {
        public String call() throws Exception {
            Thread.sleep(3000);
            return "longTimeCalculation()";
        }
    }
}
