import com.rds.task.TaskMonitorThread;
import com.rs.model.Config;


public class Test1 {
	public static void main(String[] args) {
		Config config = new Config(); 
		Thread monitorThread = new Thread(new TaskMonitorThread(config));
		monitorThread.start();
		try {
			monitorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("主线程执行完成");
	}
}
