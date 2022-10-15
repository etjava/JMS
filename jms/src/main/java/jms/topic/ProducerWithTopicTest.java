package jms.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * ��Ϣ������ - ��������ģʽ
 * @author etjav
 *
 */
@Slf4j
public class ProducerWithTopicTest {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // admin
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // admin
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// Ĭ�ϵ����ӵ�ַ tcp://localhost:61616
	private static final int NUMS = 10; // ��Ϣ����
	
	public static void main(String[] args) {
		// ��������Factory
		ConnectionFactory factory =  null;
		// ��ȡ����
		Connection conn =  null;
		// ����Session ���ڷ��ͺͽ�����Ϣ���߳�
		Session session =  null;
		// ������Ϣ��·�� - ��Ϣ��Ŀ�ĵ�
		Destination destination =  null;
		// ������Ϣ������
		MessageProducer msgProducer =  null;
		
		
		// ʵ�������ӹ���
		factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		
		try {
			// �����ӹ����л�ȡ����
			conn = factory.createConnection();
			// ������
			conn.start();
			/*
			 * ��ȡsession  - session�Ự�Ǹ��ݾ����ÿ�����ӻ�ȡ��
			 * �������ͣ� 
			 * ����1 ��ʾ�Ƿ�������trueΪ����
			 * ����2 ��Ϣ��ȷ�Ϸ�ʽ  Session.AUTO_ACKNOWLEDGE ��ʾ�Զ�ȷ��
			 * 
			 * ֱ��receive(����)��Ϣ��ʽ ������ �ֱ�����
			 * Session.AUTO_ACKNOWLEDGE [�Ƽ�]
			 * 	���ͻ��˳ɹ��Ľ��շ������ص�ʱ�� ���ǴӼ���(MessageListener.onMessage)�����ɹ����ص�ʱ�� �Ự���Զ�ȷ�Ͽͻ����յ�����Ϣ
			 * 
			 * Session.CLIENT_ACKNOWLEDGE
			 * 	�ͻ���ͨ����Ϣ��acknowledge����ȷ�Ͻ��յ���Ϣ����ģʽ���ڻỰ���Ͻ���ȷ�ϵ�
			 * 	���� ������10����Ϣ ����������Ϣ��ȷ�����Ѻ� ��ô��10����Ϣ���ᱻȷ��Ϊ������
			 * 
			 * Session.DUPS_OK_ACKNOWLEDGE
			 * 	����ģʽ�����ֻỰ�ٶ�(һֱ���ղ�����Ϣ������״̬)������ظ�ȷ����Ϣ ���������ȷ��ʧ�� ��ô���ܾͻ��ٴη���ȷ����Ϣ 
			 * 
			 */
			session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// ������Ϣ����(topic) - �᷵����Ϣ��Ŀ�ĵ�
			destination = session.createTopic("topic_1");
			// ������Ϣ������ - ������Ϣ������ʵ��
			msgProducer = session.createProducer(destination);
			// ������Ϣ��������
			sendMsg(session, msgProducer);
			// �ύ���� - session�ڴ�����ʱ������������ ���������Ҫ�ύ������������ķ��͵�����
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
				try {
					if(conn!=null)
						conn.close();
				} catch (JMSException e) {
					log.error("���ӹر��쳣 ",e);
				}
		}
	}
	
	// ������Ϣ
	private static void sendMsg(Session session,MessageProducer produce) throws JMSException {
		// ģ�ⷢ����Ϣ
		for(int i = 1; i< NUMS; i++) {
			// �����򵥵��ı���Ϣ
			TextMessage textMessage = session.createTextMessage("��������Ϣ��Ϣ - "+i);
			System.out.println(textMessage.getText());
			// ������Ϣ
			produce.send(textMessage);
		}
	}
}
