package com.yangyl.mq.producer;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.StreamMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

	@Resource(name = "jmsTemplate")
	private JmsTemplate jmsTemplate;
	
	/**
	 * 向指定队列发送消息
	 */
	public void sendMessage(Destination destination, final String msg) {
		System.out.println("向队列" + destination.toString() + "发送了消息------------" + msg);
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
	}
	/**
     * 向指定Destination发送map消息
     * @param destination
     * @param message
     */
    public void sendMapMessage(Destination destination, final String message){
        if(null == destination){
            destination = jmsTemplate.getDefaultDestination();
        }
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("msgId",message);
                return mapMessage;
            }
        });
        System.out.println("springJMS send map message...");
    }
	/**
	 * 向指定队列发送对象消息
	 */
	public void sendObjectMessage(Destination destination, final Serializable object) {
		System.out.println("向队列" + destination.toString() + "发送了消息------------" + object.toString());
		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createObjectMessage(object);
			}
		});
	}

	/**
     * 向指定Destination发送字节消息
     * @param destination
     * @param bytes
     */
    public void sendBytesMessage(Destination destination, final byte[] bytes){
        if(null == destination){
            destination = jmsTemplate.getDefaultDestination();
        }
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                BytesMessage bytesMessage = session.createBytesMessage();
                bytesMessage.writeBytes(bytes);
                return bytesMessage;

            }
        });
        System.out.println("springJMS send bytes message...");
    }
    /**
     * 向默认队列发送Stream消息
     */
    public void sendStreamMessage(Destination destination) {
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                StreamMessage message = session.createStreamMessage();
                message.writeString("stream string");
                message.writeInt(11111);
                return message;
            }
        });
        System.out.println("springJMS send Strem message...");
    }
	/**
	 * 向默认队列发送消息
	 */
	public void sendMessage(final String msg) {
		String destination = jmsTemplate.getDefaultDestination().toString();
		System.out.println("向队列" + destination + "发送了消息------------" + msg);
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
	}
}
