package org.aztec.autumn.common.utils.jms;

import java.util.Map;

import javax.jms.JMSException;

/**
 * 消息订阅器.JMS中的订阅模式下的订阅者.在订阅模式，同一条消息可以被多个订阅者读取。但订阅者没法读取过往消息。订阅者所有接收信息的方法都是非阻塞的。
 * @author 黎明
 *
 */
public interface IMessageSubscriber extends IMessenger{

  /**
   * 改变所关注的消息。
   * @param concern 所关注的消息队列或主题名称
   * @throws JMSException
   */
  public void changeConcern(String concern) throws JMSException;
  /**
   * 获取一条文本消息。订阅者接收的所有消息，都被缓存进一个本地的消息队列中，订阅者根据先来后到取出消息。
   * @return 一条订阅的文本消息
   */
  public String getText();
  /**
   * 获取一个Map对象
   * @return map对象数据
   */
  public Map<String, Object> getMap();
  /**
   * 获取一个普通对象
   * @return 一个对象数据
   */
  public Object getObject();
  /**
   * 获取一个指定类型的对象
   * @param entityCls 指定的实体类
   * @return
   */
  public <T> T getEntity(Class<T> entityCls);
  /**
   * 启动订阅器
   * @throws JMSException
   */
  public void start() throws JMSException;
  /**
   * 停止订阅器
   * @throws JMSException
   */
  public void stop() throws JMSException;
  
  public boolean isStarted() throws JMSException;
}
