package org.aztec.autumn.common.utils.jms;

import java.util.Map;

import javax.jms.JMSException;

/**
 * 信息接受器。JMS中生产者/消费者模式中的消费者。在消费者模式下，接收器会从消息队列中排它性地取出一条消息，被取出的消息只能由该接收器接收使用。
 * 不管消息队列中的消息何时发布，接收器都可以接收。接收器的所有方法都是阻塞式的。
 * @author 黎明
 *
 */
public interface IMessageReceiver extends IMessenger{

  /**
   * 接收文本
   * @param name 队列名
   * @return
   * @throws JMSException
   */
  public String receiveText(String name) throws JMSException;
  /**
   * 接收多个文本
   * @param name 队列名
   * @param msgNum 消息个数
   * @return
   * @throws JMSException
   */
  public String[] receiveText(String name, int msgNum) throws JMSException;

  /**
   * 接收对象
   * @param name 队列名
   * @param msgNum 消息个数
   * @return
   * @throws JMSException
   */
  public Object[] receiveObject(String name, int msgNum) throws JMSException;
  
  /**
   * 接收指定类型对象
   * @param name 队列名
   * @param entityCls 指定的实体类对象
   * @return
   * @throws JMSException
   */
  public <T> T receiveObject(String name,Class<T> entityCls)  throws JMSException;

  /**
   * 接收Map对象 
   * @param name 队列名
   * @param msgNum 消息个数
   * @return
   * @throws JMSException
   */
  public Map[] receiveMap(String name, int msgNum) throws JMSException;
}
