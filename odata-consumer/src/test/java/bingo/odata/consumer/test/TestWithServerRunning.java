package bingo.odata.consumer.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import bingo.odata.consumer.test.mock.DemoODataServer;

public class TestWithServerRunning {

	static Thread thread;
	private static int layer = 0;
	protected static boolean enabled = true;
	
	@BeforeClass
	public static void before() {
		if(!enabled) return;
		try {
			if(null == thread) {
				thread = new DemoODataServer();
				thread.start();
				Thread.sleep(1000);
			}
			layer++;
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@AfterClass
	public static void after() {
		if(!enabled) return;
		try {
			layer--;
			if(0 == layer) {
				thread.join(1);
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
