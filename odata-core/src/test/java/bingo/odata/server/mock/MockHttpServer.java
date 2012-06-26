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
package bingo.odata.server.mock;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;

public class MockHttpServer {
	
	private static final Log log = LogFactory.get(MockHttpServer.class);

	private final int    port;
	private final String contextPath;
	private final Server server;
	private final MockHttpHandler handler;
	
	public MockHttpServer(MockHttpHandler handler) {
		this.port = 8080;
		this.contextPath = "";
		this.server = new Server(port);
		this.handler = handler;
		
		this.createHandler();
	}
	
	public MockHttpServer(int port,String contextPath,MockHttpHandler handler) {
		this.port = port;
		this.contextPath = contextPath;
		this.handler = handler;
		this.server = new Server(port);
		
		this.createHandler();
	}

	public synchronized MockHttpServer start() throws Throwable {
		server.start();
		
		log.info("mock http server running at port {}",port);
		
		return this;
	}
	
	public synchronized void join() throws Throwable {
		server.join();
	}
	
	public synchronized MockHttpServer shutdown() throws Throwable {
		server.stop();
		return this;
	}
	
	private void createHandler(){
		ContextHandler context = new ContextHandler(contextPath);
		
		context.setHandler(new AbstractHandler() {
			public void handle(String target, Request req, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				try {
	                handler.handle(request, response);
                } catch (Throwable e) {
                	throw new ServletException(e.getMessage(),e);
                }
			}
		});
		
		server.setHandler(context);
	}
}