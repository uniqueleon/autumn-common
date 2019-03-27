package org.aztec.autumn.common.utils.server.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JettyCustomServer extends Server {

  private String descriptor = "webapps/WEB-INF/web.xml";
  private String resourceBase = "webapps";
  private String contextPath = "/";
  private int managePort;
  private static final Logger LOG = LoggerFactory.getLogger(JettyCustomServer.class);

  public JettyCustomServer(String descriptor, String resourceBase,
      String contextPath,int managePort) {
    super();
    this.managePort = managePort;
    this.descriptor = descriptor;
    this.resourceBase = resourceBase;
    this.contextPath = contextPath;
  }
  
  public JettyCustomServer( String resourceBase,
	      String contextPath,int managePort) {
	    super();
	    this.managePort = managePort;
	    this.descriptor = resourceBase + "/WEB-INF/web.xml";
	    this.resourceBase = resourceBase;
	    this.contextPath = contextPath;
	  }
  
  public JettyCustomServer(int managePort) throws FileNotFoundException, IOException {
    super();
    this.managePort = managePort;
  }

  public void startServer() throws Exception{
    LOG.info("Prepare to start HTTP Server(Jetty)!");
 // 服务器的监听端口
    Server server = new Server(managePort);
    // 关联一个已经存在的上下文
    WebAppContext context = new WebAppContext();
    // 设置描述符位置
    context.setDescriptor(descriptor);
    // 设置Web内容上下文路径
    context.setResourceBase(resourceBase);
    // 设置上下文路径
    //context.setContextPath(contextPath);
    context.setContextPath(contextPath);
    //context.setClassLoader(JettyCustomServer.class.getClassLoader());
    context.setParentLoaderPriority(true);
    File libDir = new File(resourceBase + "/lib");
    if(!libDir.exists() && !libDir.isDirectory()){
      System.err.println("lib doesn't exists!Jetty server start failed!");
      LOG.error("lib doesn't exists!Jetty server start failed!");
      return;
    }
    //System.out.println(libDir.toURI().toString());
    context.setExtraClasspath(libDir.getAbsolutePath());
    server.setHandler(context);
    //Thread.currentThread().setContextClassLoader(context.getClassLoader());
    // 启动
    LOG.info("Starting HTTP Server(Jetty)!");
    server.start();
    LOG.info("Start Finished!");
    // server.join();
  }
}
