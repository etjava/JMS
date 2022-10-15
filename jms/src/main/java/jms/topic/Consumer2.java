package jms.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *  ��Ϣ������ 2
 * @author etjav
 *
 */
public class Consumer2 {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // admin
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // admin
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// Ĭ�ϵ����ӵ�ַ tcp://localhost:61616
	
	public static void main(String[] args) {
		// �������ӹ���
		ConnectionFactory factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		// ��ȡ����
		Connection conn = null;
		try {
			// ��ȡ����
			conn = factory.createConnection();
			// ��������
			conn.start();
			// ����session
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// ��ȡ���ӵ�Ŀ�ĵ�
			Destination destination = session.createTopic("topic_1");
			// ����������
			MessageConsumer consumer = session.createConsumer(destination);
			// ������Ϣ - ������ʽ
			consumer.setMessageListener(new Linstener2());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

/**
 * ��Ϣ����
 * @author etjav
 *
 */
class Linstener2 implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			// ���յ�����Ϣ
			System.out.println("������2���յ�����Ϣ :"+((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
