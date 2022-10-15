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
 * 消息消费者
 * 点对点 发送和接收消息
 * @author etjav
 *
 */
@Slf4j
public class ConsumerTest {
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // admin
	private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // admin
	private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;// 默认的连接地址 tcp://localhost:61616

	public static void main(String[] args) {
		// 创建连接Factory
		ConnectionFactory factory =  null;
		// 获取连接
		Connection conn =  null;
		// 创建Session 用于发送和接收消息的线程
		Session session =  null;
		// 定义消息的路由 - 消息的目的地
		Destination destination =  null;
		// 消息消费者
		MessageConsumer consumer = null;
		
		// 实例化连接工厂
		factory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
		
		try {
			// 在连接工厂中获取连接
			conn = factory.createConnection();
			// 打开连接
			conn.start();
			// 获取session - 每个连接对应一个session 获取消息不需要添加事务控制
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建连接的消息队列 - 会返回消息的目的地
			destination = session.createQueue("firstQueue");
			// 创建消息消费者 - 返回消息消费者实例
			consumer = session.createConsumer(destination);
			// 发送消息到队列中
			getMsg(consumer);
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

	/**
	 * 接收消息
	 * @param consumer 消息消费者
	 * @throws JMSException 
	 */
	private static void getMsg(MessageConsumer consumer) throws JMSException {
		// receive 方式【不推荐使用】
		while(true) {
			TextMessage msg = (TextMessage) consumer.receive();
			if(msg!=null) {
				System.out.println("receive 方式收到的消息:"+msg.getText());
			}else {
				// 没有收到消息 结束循环
				break;
			}
		}
	}
}
