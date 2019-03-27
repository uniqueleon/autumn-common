package org.aztec.autumn.common.utils.jms;

import javax.jms.JMSException;

import org.aztec.autumn.common.GlobalConfig;
import org.aztec.autumn.common.utils.UtilsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsProxy {
	
	private static final Logger LOG = LoggerFactory.getLogger(JmsProxy.class);

	private JmsProxy() {
		// TODO Auto-generated constructor stub
	}


	public static IMessageSender getSender(String destination){
		IMessengerFactory factory = UtilsFactory.getInstance().getJMSMessengerFactory();
		String[] brokerCluster = GlobalConfig.getInstance().getJmsCluster().split(",");
		if(brokerCluster == null || brokerCluster.length == 0)
			return null;
		for(String brokerUri : brokerCluster){
			try {
				IMessageSender sender = factory.getSender(brokerUri, destination);
				return sender;
			} catch (JMSException e) {
				LOG.warn(e.getMessage(),e);
				continue;
			}
			
		}
		return null;
	}
	
	public static IMessageSubscriber getSubscriber(String concern,String destination){
		IMessengerFactory factory = UtilsFactory.getInstance().getJMSMessengerFactory();
		String[] brokerCluster = GlobalConfig.getInstance().getJmsCluster().split(",");
		if(brokerCluster == null || brokerCluster.length == 0)
			return null;
		for(String brokerUri : brokerCluster){
			try {
				IMessageSubscriber subscriber = factory.getSubscriber(brokerUri, concern, destination);
				return subscriber;
			} catch (JMSException e) {
				LOG.warn(e.getMessage(),e);
				continue;
			}
			
		}
		return null;
	}
	
	public static IMessageReceiver getReceiver(String concern,String destination){
		IMessengerFactory factory = UtilsFactory.getInstance().getJMSMessengerFactory();
		String[] brokerCluster = GlobalConfig.getInstance().getJmsCluster().split(",");
		if(brokerCluster == null || brokerCluster.length == 0)
			return null;
		for(String brokerUri : brokerCluster){
			try {
				IMessageReceiver receiver = factory.getReceiver(brokerUri, destination);
				return receiver;
			} catch (JMSException e) {
				LOG.warn(e.getMessage(),e);
				continue;
			}
			
		}
		return null;
	}
	
}
