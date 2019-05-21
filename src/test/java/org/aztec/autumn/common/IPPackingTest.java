package org.aztec.autumn.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.eclipse.jetty.server.AbstractHttpConnection.Output;

import com.google.common.collect.Lists;

public class IPPackingTest {
	
	public final static int  serverPort = 1122;
	public final static int  clientPort = 1123;

	public IPPackingTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void startServer() {
		Thread serverThread = new Thread(new TestServer());
		serverThread.setName("test-server");
		serverThread.start();
	}
	
	public static class TestServer implements Runnable{
		
		private List<SocketDataHandler> handlers = Lists.newArrayList();
		
		public TestServer() {
			handlers.add(new IPCheckHandler());
		}

		@Override
		public void run() {

			try {
				ServerSocket serverSocket = new ServerSocket(serverPort);
				while (true) {

					Socket socket = serverSocket.accept();
					InputStream is = socket.getInputStream();
					while (is.available() == 0) {
						StringBuilder builder = new StringBuilder();
						if (is.available() != 0) {
							OutputStream os = socket.getOutputStream();
							byte[] buffer = new byte[is.available()];
							is.read(buffer);
							builder.append(new String(buffer));
							os.write("ok".getBytes());
						}
						if (builder.toString().equals("begin")) {
							break;
						}
						Thread.currentThread().sleep(10l);
					}
					for (SocketDataHandler handler : handlers) {
						handler.handle(socket);
					}
					while (is.available() == 0) {
						if (is.available() != 0) {
							socket.close();
						}
						Thread.currentThread().sleep(10l);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static interface SocketDataHandler{
		
		
		public void handle(Socket clientSocket);
	}
	
	public static class IPCheckHandler implements SocketDataHandler{

		@Override
		public void handle(Socket clientSocket) {
			// TODO Auto-generated method stub

			InetAddress address = clientSocket.getInetAddress();
			System.out.println(address.getHostAddress());
		}
		
	}
	
	
	public static void connect(String modAddress,Integer modPort) throws UnknownHostException, IOException, InterruptedException {

		Socket clientSocket = new Socket();
		//clientSocket.bind(new InetSocketAddress(modAddress, modPort));
		clientSocket.connect(new InetSocketAddress("localhost", serverPort));
		OutputStream os = clientSocket.getOutputStream();
		os.write("begin".getBytes());
		os.flush();
		Thread.currentThread().sleep(10l);
		InputStream is = clientSocket.getInputStream();
		boolean isConfirmed = false;
		while(!isConfirmed  && is.available() == 0) {
			if(is.available() != 0) {

				byte[] readbyte = new byte[is.available()];
				is.read(readbyte);
				String confirmStr = new String(readbyte);
				if(confirmStr.equals("ok")) {
					isConfirmed = true;
					break;
				}
			}
		}
		os.write("end".getBytes());
		os.flush();
		clientSocket.close();
		///while
	}
	
	public static void main(String[] args) {
		try {
			//startServer();
			connect("192.168.1.14", 11234);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
