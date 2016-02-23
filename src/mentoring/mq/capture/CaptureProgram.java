package mentoring.mq.capture;
import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class CaptureProgram {
	public static void main(String[] args) throws Exception {
		String user = env("ACTIVEMQ_USER", "admin");
        String password = env("ACTIVEMQ_PASSWORD", "password");
        String host = env("ACTIVEMQ_HOST", "localhost");
        int port = Integer.parseInt(env("ACTIVEMQ_PORT", "61616"));
		
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		Message message;
		try {
	        ConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + port);
	        connection = factory.createConnection(user, password);
	        connection.start();
	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        Destination dest = session.createQueue("TEST.QUEUE");
	        producer = session.createProducer(dest);
	        
	        message = session.createTextMessage("Hello ActiveMQ!");
	        
	        producer.send(message);
		}
		catch (JMSException jmsEx) {
			jmsEx.printStackTrace();
		}
		finally {
			if (producer != null) {
				producer.close();
			}
			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
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
