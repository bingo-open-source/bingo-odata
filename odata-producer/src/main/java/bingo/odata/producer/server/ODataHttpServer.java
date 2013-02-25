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
package bingo.odata.producer.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;

import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.lang.servlet.Servlets;

public class ODataHttpServer {
	
	private static final Log log = LogFactory.get(ODataHttpServer.class);

	private final int     port;
	private final String  contextPath;
	private final Server  server;
	private final Servlet servlet;
	private final Filter  filter;
	
	public ODataHttpServer(Object handler) {
		this(8080,"",handler);
	}
	
	public ODataHttpServer(int port,Object handler) {
		this(8080,"",handler);
	}
	
	public ODataHttpServer(String contextPath,Object handler) {
		this(8080,contextPath,handler);
	}
	
	public ODataHttpServer(int port,String contextPath,Object handler) {
		this(contextPath,handler,new Server(port));
	}
	
	protected ODataHttpServer(String contextPath,Object handler,Server server){
		this.port        = server.getConnectors()[0].getPort();
		this.contextPath = contextPath;
		this.server      = server;
		
		if(handler instanceof Servlet){
			this.servlet = (Servlet)handler;
			this.filter  = null;
		}else if(handler instanceof Filter){
			this.filter  = (Filter)handler;
			this.servlet = null;
		}else{
			throw new IllegalArgumentException("handler must be servlet or filter");
		}
		
		this.createHandler();
	}
	
	public String getContextPath() {
		return contextPath;
	}

	public synchronized ODataHttpServer start() throws Throwable {
		server.start();
		
		log.info("odata http server running at port {}, path '{}'",port,contextPath);
		
		return this;
	}
	
	public synchronized void join() throws Throwable {
		server.join();
	}
	
	public synchronized ODataHttpServer shutdown() throws Throwable {
		server.stop();
		return this;
	}
	
	private void createHandler(){
		ContextHandler context = new ContextHandler(contextPath);
		
		context.setHandler(new AbstractHandler() {
			public void handle(String target, Request req, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
				if(null == filter){
					servlet.service(request, response);
				}else{
					filter.doFilter(request, response, Servlets.NOT_FOUND_FILTER_CHAIN);
				}
			}
		});
		
		server.setHandler(context);
		
		try {
	        if(null == filter){
	        	servlet.init(new Servlets.EmptyServletConfig(context.getServletContext()));
	        }else{
	        	filter.init(new Servlets.EmptyFilterConfig(context.getServletContext()));
	        }
        } catch (ServletException e) {
        	throw new RuntimeException("error init handler",e);
        }
	}
}