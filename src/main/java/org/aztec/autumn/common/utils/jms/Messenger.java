package org.aztec.autumn.common.utils.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class Messenger implements IMessenger {

	protected static ConnectionFactory factory;
	protected static Connection connection;
	protected DestinationType type;

	public Messenger(String url, DestinationType type) throws JMSException {
		// TODO Auto-generated constructor stub
		if (factory == null)
			factory = new ActiveMQConnectionFactory(url);
		this.type = type;
		if (connection == null) {

			connection = factory.createConnection();
			connection.start();
		}
	}
	

	public void disconnect() throws JMSException {
		if (connection != null) {
			synchronized (connection) {
				connection.stop();
				connection.close();
				connection = null;
			}
		}
	}

	public void reconnect() throws JMSException {
		if (connection != null) {
			synchronized (connection) {
				connection = factory.createConnection();
			}
		}
		else{
			connection = factory.createConnection();
			connection.start();
		}
	}

	protected Session getSession() throws JMSException {
		if(connection == null)
			reconnect();
		return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	protected Destination getDestination(Session session, String destination) throws JMSException {
		if (type == null)
			throw new JMSException("send mode can't be null!");
		switch (type) {
		case TOPIC:
			return session.createTopic(destination);
		case QUEUE:
			return session.createQueue(destination);
		default:
			break;
		}
		return null;
	}

}
