package jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * ��Ϣ������
 * ��Ե� ���ͺͽ�����Ϣ
 * @author etjav
 *
 */
@Slf4j
public class ConsumerTest {
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // admin
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // admin
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// Ĭ�ϵ����ӵ�ַ tcp://localhost:61616

	public static void main(String[] args) {
		// ��������Factory
		ConnectionFactory factory =  null;
		// ��ȡ����
		Connection conn =  null;
		// ����Session ���ڷ��ͺͽ�����Ϣ���߳�
		Session session =  null;
		// ������Ϣ��·�� - ��Ϣ��Ŀ�ĵ�
		Destination destination =  null;
		// ��Ϣ������
		MessageConsumer consumer = null;
		
		// ʵ�������ӹ���
		factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		
		try {
			// �����ӹ����л�ȡ����
			conn = factory.createConnection();
			// ������
			conn.start();
			// ��ȡsession - ÿ�����Ӷ�Ӧһ��session ��ȡ��Ϣ����Ҫ����������
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// �������ӵ���Ϣ���� - �᷵����Ϣ��Ŀ�ĵ�
			destination = session.createQueue("firstQueue");
			// ������Ϣ������ - ������Ϣ������ʵ��
			consumer = session.createConsumer(destination);
			// ������Ϣ��������
			getMsg(consumer);
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

	/**
	 * ������Ϣ
	 * @param consumer ��Ϣ������
	 * @throws JMSException 
	 */
	private static void getMsg(MessageConsumer consumer) throws JMSException {
		// receive ��ʽ�����Ƽ�ʹ�á�
		while(true) {
			TextMessage msg = (TextMessage) consumer.receive();
			if(msg!=null) {
				System.out.println("receive ��ʽ�յ�����Ϣ:"+msg.getText());
			}else {
				// û���յ���Ϣ ����ѭ��
				break;
			}
		}
	}
}
