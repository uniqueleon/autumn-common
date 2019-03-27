package org.aztec.autumn.common.utils.jms;

import javax.jms.JMSException;

public class MessengerFactory implements IMessengerFactory {

  public MessengerFactory() {
    // TODO Auto-generated constructor stub
  }

  @Override
  public IMessageSender getSender(String url, String destinationType) throws JMSException {
    return new MessageSender(url, DestinationType.valueOf(destinationType));
  }

  @Override
  public IMessageReceiver getReceiver(String url, String destinationType) throws JMSException {
    return new MessageReceiver(url, DestinationType.valueOf(destinationType));
  }

  @Override
  public IMessageSubscriber getSubscriber(String url, String concern,
      String destinationType) throws JMSException {
    return new MessageSubscriber(url, concern, DestinationType.valueOf(destinationType));
  }

}
