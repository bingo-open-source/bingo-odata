/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.odata.consumer.test.mock;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import bingo.odata.producer.requests.ODataRequestController;
import bingo.odata.producer.server.ODataHttpHandler;
import bingo.odata.producer.server.ODataHttpServer;

public class DemoODataServer extends Thread {
	
	private static final String SERVICE_ROOT_PATH = "/demo";
	
	private static final ODataRequestController controller = new ODataRequestController();
	
	static {
		controller.setProducer(new DemoODataProducer());
	}
	
	public static void main(String[] args) {
		new DemoODataServer().run();
	}
	
	public void run() {
		ODataHttpServer server = new ODataHttpServer(new Handler());
		try {
			server.start().join();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final class Handler extends ODataHttpHandler {
		private static final long serialVersionUID = -2264118275203202029L;

		@Override
        protected String getServiceRootPath(HttpServletRequest request) {
			return SERVICE_ROOT_PATH;
        }

		@Override
        protected ODataRequestController getController() {
	        return controller;
        }
	}
}