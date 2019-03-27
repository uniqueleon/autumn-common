package org.aztec.autumn.common.utils.net.rpc.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.aztec.autumn.common.utils.net.rpc.entity.RpcInvokeParam;
import org.aztec.autumn.common.utils.net.rpc.entity.RpcInvokeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClientSocket {
	private Socket serverSocket;
	private String hostName;
	private int port;
	
	private Logger logger = LoggerFactory.getLogger(RpcClientSocket.class);

	public RpcClientSocket(String hostName, int port) throws IOException {
		serverSocket = new Socket();
		this.hostName = hostName;
		this.port = port;
	}
	
	public void connect() throws IOException{
		serverSocket.connect(new InetSocketAddress(hostName, port));
	}
	

	public void sendInvokeParam(RpcInvokeParam rpcInvParam) throws IOException {
		logger.info("Sending invoke param[" + rpcInvParam + "] to server side!");
		ObjectOutputStream oos = new ObjectOutputStream(
				serverSocket.getOutputStream());
		oos.writeObject(rpcInvParam);
		oos.flush();
		serverSocket.getOutputStream().flush();
	}

	public RpcInvokeResult getInvokeResult() throws Throwable {
		ObjectInputStream ois = new ObjectInputStream(
				serverSocket.getInputStream());
		while (ois.available() > 0) {
			Thread.sleep(1);
			//System.out.println("waiting for result!");
			ois = new ObjectInputStream(serverSocket.getInputStream());
		}
		RpcInvokeResult result = (RpcInvokeResult) ois.readObject();
		logger.info("Result data has been received from server side");
		return result;
	}
	
	public void close() throws IOException
	{
		this.serverSocket.close();
	}
}
