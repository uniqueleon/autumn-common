package org.aztec.autumn.common.utils.jms;

import java.io.Serializable;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;


public class MessageSender extends Messenger implements IMessageSender{  
  
  public MessageSender(String url,DestinationType type) throws JMSException {
    super(url,type);
  }

  public void sendText(String title,String content) throws JMSException{

    Session session = getSession();
    MessageProducer producer = session.createProducer(getDestination(session,title));
    TextMessage message = session.createTextMessage();
    message.setText(content);
    producer.send(message);
    session.close();
  }
  
  public <T extends Serializable> void sendObject(String title,T content) throws JMSException{

    Session session = getSession();
    MessageProducer producer = session.createProducer(getDestination(session,title));
    ObjectMessage message = session.createObjectMessage();
    message.setObject(content);
    producer.send(message);
    session.close();
  }
  
  public void sendMap(String title,Map<String,Object> contentMap) throws JMSException{

    Session session = getSession();
    MessageProducer producer = session.createProducer(getDestination(session,title));
    MapMessage message = session.createMapMessage();
    for(String key : contentMap.keySet()){
      message.setObject(key, contentMap.get(key));
    }
    producer.send(message);
    session.close();
  }
  
  public static void main(String[] args) {
    try {
      IMessageSender sender = new MessageSender("tcp://123.206.195.58:61616/", DestinationType.TOPIC);
      int i = 0;
      while(i < 10){
        sender.sendText("TEST","what the fuck-" + i);
        i ++;
        //Thread.currentThread().sleep(1000);
      }
      sender.disconnect();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
