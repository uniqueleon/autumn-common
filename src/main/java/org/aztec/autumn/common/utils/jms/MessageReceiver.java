package org.aztec.autumn.common.utils.jms;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MessageReceiver extends Messenger implements IMessageReceiver  {

  public MessageReceiver(String url, DestinationType type) throws JMSException {
    super(url,type);
  }

  public String[] receiveText(String name, int msgNum)
      throws JMSException {
    Session session = getSession();
    Destination dest = getDestination(session, name);
    MessageConsumer consumer = session.createConsumer(dest);
    String[] retText = new String[msgNum];
    for (int i = 0; i < msgNum; i++) {
      TextMessage txtMsg = (TextMessage) consumer.receive();
      retText[i] = txtMsg.getText();
    }
    session.close();
    return retText;
  }

  public Object[] receiveObject(String name, int msgNum)
      throws JMSException {
    Session session = getSession();
    Destination dest = getDestination(session, name);
    MessageConsumer consumer = session.createConsumer(dest);
    Object[] retObj = new Object[msgNum];
    for (int i = 0; i < msgNum; i++) {
      ObjectMessage txtMsg = (ObjectMessage) consumer.receive();
      retObj[i] = txtMsg.getObject();
    }
    session.close();
    return retObj;
  }

  public Map[] receiveMap(String name, int msgNum)
      throws JMSException {
    Session session = getSession();
    Destination dest = getDestination(session, name);
    MessageConsumer consumer = session.createConsumer(dest);
    Map[] retObj = new Map[msgNum];
    for (int i = 0; i < msgNum; i++) {
      MapMessage txtMsg = (MapMessage) consumer.receive();
      Enumeration<String> keys = txtMsg.getMapNames();
      retObj[i] = new HashMap();
      while (keys.hasMoreElements()) {
        String key = keys.nextElement();
        retObj[i].put(key, txtMsg.getObject(key));
      }
    }
    session.close();
    return retObj;
  }

  public static void main(String[] args) {

    try {
      MessageReceiver receriver = new MessageReceiver("tcp://10.10.1.108:61617",
          DestinationType.TOPIC);
      String[] retText = receriver.receiveText("TEST  sync topic", 10);
      System.out.println(Arrays.toString(retText));
      receriver.disconnect();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
    }
  }

  @Override
  public String receiveText(String name) throws JMSException {
    return receiveText(name, 1)[0];
  }

  @Override
  public <T> T receiveObject(String name, Class<T> entityCls)
      throws JMSException {
    return (T) receiveObject(name, 1)[0];
  }


}
