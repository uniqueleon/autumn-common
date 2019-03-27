package org.aztec.autumn.common.utils.jms;

import java.io.Serializable;
import java.util.Map;

import javax.jms.JMSException;

/**
 * 消息发送器。
 * @author 黎明
 *
 */
public interface IMessageSender extends IMessenger{

  /**
   * 发送带标题的文本信息
   * @param title 标题。标题用于唯一标识信息所属的队列或主题。在JMS对应的就是QUEUE或者TOPIC的名称
   * @param content 文本信息
   * @throws JMSException
   */
  public void sendText(String title,String content) throws JMSException;

  /**
   * 发送带标题的对象
   * @param title 标题。标题用于唯一标识信息所属的队列或主题。在JMS对应的就是QUEUE或者TOPIC的名称
   * @param content 可序列化对象
   * @throws JMSException
   */
  public <T extends Serializable> void sendObject(String title,T content) throws JMSException;

  /**
   * 发送带标题的映射对象
   * @param title 标题。标题用于唯一标识信息所属的队列或主题。在JMS对应的就是QUEUE或者TOPIC的名称
   * @param content 映射对象
   * @throws JMSException
   */
  public void sendMap(String title,Map<String,Object> contentMap) throws JMSException;
}
