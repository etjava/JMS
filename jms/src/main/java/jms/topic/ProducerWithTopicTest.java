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
 * 消息生产者 - 发布订阅模式
 * @author etjav
 *
 */
@Slf4j
public class ProducerWithTopicTest {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // admin
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // admin
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// 默认的连接地址 tcp://localhost:61616
	private static final int NUMS = 10; // 消息数量
	
	public static void main(String[] args) {
		// 创建连接Factory
		ConnectionFactory factory =  null;
		// 获取连接
		Connection conn =  null;
		// 创建Session 用于发送和接收消息的线程
		Session session =  null;
		// 定义消息的路由 - 消息的目的地
		Destination destination =  null;
		// 创建消息生产者
		MessageProducer msgProducer =  null;
		
		
		// 实例化连接工厂
		factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		
		try {
			// 在连接工厂中获取连接
			conn = factory.createConnection();
			// 打开连接
			conn.start();
			/*
			 * 获取session  - session会话是根据具体的每个连接获取的
			 * 参数解释： 
			 * 参数1 表示是否开启事务，true为开启
			 * 参数2 消息的确认方式  Session.AUTO_ACKNOWLEDGE 表示自动确认
			 * 
			 * 直接receive(接收)消息方式 有三种 分别如下
			 * Session.AUTO_ACKNOWLEDGE [推荐]
			 * 	当客户端成功的接收方法返回的时候 或是从监听(MessageListener.onMessage)方法成功返回的时候 会话会自动确认客户端收到了消息
			 * 
			 * Session.CLIENT_ACKNOWLEDGE
			 * 	客户端通过消息的acknowledge方法确认接收到消息，该模式是在会话层上进行确认的
			 * 	例如 生产了10条消息 当第五条消息被确认消费后 那么这10条消息都会被确认为已消费
			 * 
			 * Session.DUPS_OK_ACKNOWLEDGE
			 * 	这种模式当出现会话迟钝(一直接收不到消息被消费状态)会出现重复确认消息 如果生产者确认失败 那么可能就会再次发送确认消息 
			 * 
			 */
			session = conn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			// 创建消息发布(topic) - 会返回消息的目的地
			destination = session.createTopic("topic_1");
			// 创建消息生产者 - 返回消息生产者实例
			msgProducer = session.createProducer(destination);
			// 发送消息到队列中
			sendMsg(session, msgProducer);
			// 提交事务 - session在创建的时候设置了事务 因此这里需要提交事务才能真正的发送到队列
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
				try {
					if(conn!=null)
						conn.close();
				} catch (JMSException e) {
					log.error("连接关闭异常 ",e);
				}
		}
	}
	
	// 发送消息
	private static void sendMsg(Session session,MessageProducer produce) throws JMSException {
		// 模拟发送消息
		for(int i = 1; i< NUMS; i++) {
			// 创建简单的文本消息
			TextMessage textMessage = session.createTextMessage("发布的消息信息 - "+i);
			System.out.println(textMessage.getText());
			// 发送消息
			produce.send(textMessage);
		}
	}
}
