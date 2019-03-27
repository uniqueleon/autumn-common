package org.aztec.autumn.common.utils.jms;

/**
 * 目的地类型。即消息存放的地方
 * @author 黎明
 *
 */
public enum DestinationType {

  //主题，消息存放于主题中，用于发布者/订阅者模式中
  TOPIC,
  //消息存放于队列中，用于生产者/消费者模式
  QUEUE;
}
