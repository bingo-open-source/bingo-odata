package bingo.odata.consumer;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class ServerTestBase {

	private static Thread thread;
	
	@BeforeClass
	public static void before() {
		try {
			thread = new DemoODataServer();
			thread.start();
			Thread.sleep(1000);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void after() {
		try {
			thread.join(1);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
