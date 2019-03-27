package org.aztec.autumn.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class NetworkUtils {

	public NetworkUtils() {
		// TODO Auto-generated constructor stub
	}

	public static List<String> getLocalAddress() throws SocketException {

		List<String> sb = new ArrayList<>();
		Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
		while (en.hasMoreElements()) {
			NetworkInterface intf = (NetworkInterface) en.nextElement();
			Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
			while (enumIpAddr.hasMoreElements()) {
				InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
						&& inetAddress.isSiteLocalAddress()) {
					sb.add(inetAddress.getHostAddress().toString());
				}
			}
		}
		return sb;
	}
	

	public static void main(String[] args) {
		try {
			System.out.println(getLocalAddress());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
