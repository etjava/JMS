package jms;

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
 * 点对点 发送和接收消息
 * 消息消费者 - 监听方式获取消息
 * 会监听是否存在未消费的消息 有则取出
 * @author etjav
 *
 */
public class ConsumerWithListenerTest {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // admin
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // admin
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// 默认的连接地址 tcp://localhost:61616

	public static void main(String[] args) {
		// 创建连接工厂
		ConnectionFactory factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		// 获取连接
		Connection conn = null;
		try {
			// 获取连接
			conn = factory.createConnection();
			// 启动连接
			conn.start();
			// 创建session
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 获取连接的目的地
			Destination destination = session.createQueue("firstQueue");
			// 创建消费者
			MessageConsumer consumer = session.createConsumer(destination);
			// 接收消息 - 监听方式
			consumer.setMessageListener(new Linstener());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

/**
 * 消息监听
 * @author etjav
 *
 */
class Linstener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		try {
			// 接收到的消息
			System.out.println("Listener接收到的消息 :"+((TextMessage)message).getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
}
