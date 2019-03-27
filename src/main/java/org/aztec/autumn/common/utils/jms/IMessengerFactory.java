package org.aztec.autumn.common.utils.jms;

import javax.jms.JMSException;

/**
 * 信使工厂，用于生成各类信使
 * @author 黎明
 *
 */
public interface IMessengerFactory {

  /**
   * 获取消息发送器
   * @param url 消息代理服务器连接url
   * @param destinationType 地址类型
   * @return 消息发送器
   * @throws JMSException
   */
  public IMessageSender getSender(String url,String destinationType)  throws JMSException;

  /**
   * 获取消息接收器
   * @param url 消息代理服务器连接url
   * @param destinationType 地址类型
   * @return 消息发送器
   * @throws JMSException
   */
  public IMessageReceiver getReceiver(String url,String destinationType)  throws JMSException;

  /**
   * 获取消息订阅器
   * @param url 消息代理服务器连接url
   * @param destinationType 地址类型
   * @return 消息订阅器
   * @throws JMSException
   */
  public IMessageSubscriber getSubscriber(String url,String concern,String destinationType)  throws JMSException;
}
