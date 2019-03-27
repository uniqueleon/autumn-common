package org.aztec.autumn.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketUtils {


	Socket clientSocket;
	OutputStream os;
	InputStream is;
	private Queue<String> response;
	
	public SocketUtils(String ip,int port) throws IOException {
		super();
		this.clientSocket = new Socket(ip, port);
		this.os = clientSocket.getOutputStream();
		this.is = clientSocket.getInputStream();
		Thread checkerThread = new Thread(new ReponseChecker());
		checkerThread.start();
		response = new LinkedBlockingQueue<>();
	}
	

	public void sendCommand(String command)
			throws UnsupportedEncodingException, IOException, InterruptedException {
		os = clientSocket.getOutputStream();
		//clientSocket.
		os.write(command.getBytes("UTF-8"));
		os.flush();
	}
	
	public List<String> getReceiveMsgs(){
		List<String> retMsgs = new ArrayList<>();
		synchronized (response) {
			while(!response.isEmpty()){
				retMsgs.add(response.poll());
			}
		}
		return retMsgs;
	}
	
	class ReponseChecker implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			while(true){
				try {
					StringBuilder builder = new StringBuilder();
					while (is.available() < 0) {
						Thread.currentThread().sleep(10);
					}
					while (is.available() > 0 || builder.toString().trim().isEmpty()) {
						byte[] readBytes = new byte[is.available()];
						is.read(readBytes);
						String readStr = new String(readBytes);
						if (!readStr.isEmpty())
							builder.append(new String(readBytes));
						//System.out.println("getting data....");
						Thread.currentThread().sleep(10);
					}
					//System.out.println("BEAR RECEIVE:" + builder.toString());
					response.add(builder.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}

}
