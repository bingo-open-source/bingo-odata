package bingo.odata.consumer.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import bingo.odata.consumer.test.mock.DemoODataServer;

public class TestWithServerRunning {

	private static Thread thread;
	private static int layer = 0;
	
	@BeforeClass
	public static void before() {
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
