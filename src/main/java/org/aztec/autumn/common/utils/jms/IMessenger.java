package org.aztec.autumn.common.utils.jms;

import javax.jms.JMSException;

/**
 * JMS 信使，包括信息发送器，接受器，订阅器
 * @author 黎明
 *
 */
public interface IMessenger {

  /**
   * 从代理服务器上断开
   * @throws JMSException
   */
  public void disconnect() throws JMSException;
  /**
   * 与代理服务器重连
   * @throws JMSException
   */
  public void reconnect() throws JMSException;
}
