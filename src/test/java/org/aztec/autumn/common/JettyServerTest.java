package org.aztec.autumn.common;

import org.aztec.autumn.common.utils.server.http.JettyCustomServer;

public class JettyServerTest {

	public JettyServerTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		try {
			JettyCustomServer jcs = new JettyCustomServer("test/webapps","/",1999);
			jcs.startServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
