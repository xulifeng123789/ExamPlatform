package com.yangyl.mq.consumer;

import javax.annotation.Resource;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

	@Resource(name = "jmsTemplate")
	private JmsTemplate jmsTemplate;
	
	/**
	 * 接收消息
	 */
	public TextMessage receive(Destination destination) {
		TextMessage tm = (TextMessage) jmsTemplate.receive(destination);
		try {
			System.out.println("从队列" + destination.toString() + "收到了消息：\t" + tm.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return tm;

	}
	/**
     * 根据消息类型进行对应的处理
     * @param destination 消息发送/接收共同的Destination
     * @throws JMSException
     */
    public void receiveType(Destination destination) throws JMSException {
        Message message = jmsTemplate.receive(destination);

        // 如果是文本消息
        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            System.out.println("from" + destination.toString() + " get textMessage：\t" + tm.getText());
        }

        // 如果是Map消息
        if (message instanceof MapMessage) {
            MapMessage mm = (MapMessage) message;
            System.out.println("from" + destination.toString() + " get textMessage：\t" + mm.getString("msgId"));
        }

        // 如果是Object消息
        if (message instanceof ObjectMessage) {
            ObjectMessage om = (ObjectMessage) message;
            Object record = om.getObject();
            System.out.println("from" + destination.toString() + " get ObjectMessage：\t"
                    + record.toString());
        }

        // 如果是bytes消息
        if (message instanceof BytesMessage) {
            byte[] b = new byte[1024];
            int len = -1;
            BytesMessage bm = (BytesMessage) message;
            while ((len = bm.readBytes(b)) != -1) {
                System.out.println(new String(b, 0, len));
            }
        }

        // 如果是Stream消息
        if (message instanceof StreamMessage) {
            StreamMessage sm = (StreamMessage) message;
            System.out.println(sm.readString());
            System.out.println(sm.readInt());
        }
    }
}
