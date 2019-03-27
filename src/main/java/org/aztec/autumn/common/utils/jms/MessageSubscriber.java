package org.aztec.autumn.common.utils.jms;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSubscriber extends Messenger implements MessageListener, IMessageSubscriber {

	private Queue<String> receivedTexts = new LinkedList<String>();
	private Queue<Object> receivedObjects = new LinkedList<Object>();
	private Queue<Map<String, Object>> receivedMap = new LinkedList<Map<String, Object>>();
	private String concernTopic;
	private static final Logger LOG = LoggerFactory.getLogger(MessageReceiver.class);
	private Session subscriberSession;
	private boolean started;

	public MessageSubscriber(String url, DestinationType type) throws JMSException {
		// TODO Auto-generated constructor stub
		super(url, type);
	}

	public MessageSubscriber(String url, String concernTopic, DestinationType type) throws JMSException {
		super(url, type);
		this.concernTopic = concernTopic;
	}

	public void changeConcern(String concern) throws JMSException {
		this.concernTopic = concern;
		receivedTexts.clear();
		receivedObjects.clear();
		receivedMap.clear();
		stop();
		subscriberSession = getSession();
		Destination dest = getDestination(subscriberSession, concernTopic);
		MessageConsumer consumer = subscriberSession.createConsumer(dest);
		consumer.setMessageListener(this);
	}

	public void start() throws JMSException {
		started = true;
		subscriberSession = getSession();
		Destination dest = getDestination(subscriberSession, concernTopic);
		MessageConsumer consumer = subscriberSession.createConsumer(dest);
		consumer.setMessageListener(this);
	}

	@Override
	public void onMessage(Message paramMessage) {
		try {
			if (paramMessage instanceof TextMessage) {
				receivedTexts.add(((TextMessage) paramMessage).getText());
			} else if (paramMessage instanceof MapMessage) {
				MapMessage txtMsg = (MapMessage) paramMessage;
				Enumeration<String> keys = txtMsg.getMapNames();
				Map<String, Object> contentMap = new HashMap<String, Object>();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					contentMap.put(key, txtMsg.getObject(key));
				}
			} else if (paramMessage instanceof ObjectMessage) {
				receivedObjects.add(((ObjectMessage) paramMessage).getObject());
			}
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getText() {
		return receivedTexts.poll();
	}

	public Map<String, Object> getMap() {
		return receivedMap.poll();
	}

	public Object getObject() {
		return receivedObjects.poll();
	}

	public <T> T getEntity(Class<T> entityCls) {
		return (T) receivedObjects.poll();
	}

	public void stop() throws JMSException {
		subscriberSession.close();
		// disconnect();
	}
	@Override
	public boolean isStarted() throws JMSException {
		return started;
	}

	public static void main(String[] args) {

		try {
			IMessageSubscriber receriver = new MessageSubscriber("tcp://123.206.200.174:61616/", "TEST",
					DestinationType.TOPIC);
			receriver.start();
			String text = receriver.getText();
			int maxWaitTime = 100;
			int waitTime = 0;
			while (waitTime < maxWaitTime) {
				text = receriver.getText();
				if (text != null) {
					System.out.println(text);
					waitTime = 0;
				} else {
					System.out.println("wait for message to come");
					Thread.currentThread().sleep(1000);
					waitTime++;
				}
			}
			receriver.stop();
			receriver.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		}
	}

}
