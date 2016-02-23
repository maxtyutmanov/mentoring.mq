package mentoring.mq.processing;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ProcessingProgram {
	public static void main(String[] args) throws Exception {
		String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));
		
		ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);
		Connection connection = null;
		Session session = null;
		MessageConsumer consumer = null;
		
		try {
			connection = factory.createConnection(user, password);
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue("TEST.QUEUE");
			consumer = session.createConsumer(dest);
			TextMessage receivedMsg = (TextMessage)consumer.receive();
			String text = receivedMsg.getText();
			
			System.out.println(text);
			
		}
		catch (JMSException ex) {
			ex.printStackTrace();
		}
		finally {
			if (connection != null) {
				connection.close();
			}
			if (session != null) {
				session.close();
			}
			if (consumer != null) {
				consumer.close();
			}
		}
	}
	
    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if( rc== null )
            return defaultValue;
        return rc;
    }
}
