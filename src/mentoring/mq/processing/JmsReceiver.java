package mentoring.mq.processing;

import java.util.ArrayList;
import java.util.Arrays;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsReceiver {
	private final String PROCESSING_QUEUE_NAME = "MENTORING.MQ.PROCESSING_QUEUE";
	
	private final ConnectionFactory _factory;
	private final Connection _connection;
	private Session _session;
	
	public JmsReceiver() throws JMSException {
		String user = "admin";
        String password = "password";
        String host = "localhost";
        int port = 61616;
		
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + String.valueOf(port));
		factory.setTrustedPackages(new ArrayList(Arrays.asList("*")));
		_factory = factory;
		_connection = _factory.createConnection(user, password);
		_session = null;
	}
	
	public void start(MessageListener listener) throws JMSException {
		if (_session == null) {
			MessageConsumer consumer = null;
			
			_connection.start();
			try {
				_session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
				Destination dest = _session.createQueue(PROCESSING_QUEUE_NAME);
				consumer = _session.createConsumer(dest);
				consumer.setMessageListener(listener);
			} 
			catch (JMSException ex) {
				if (consumer != null) {
					consumer.close();
				}
				if (_session != null) {
					_session.close();
					_session = null;
				}
			}
		}
	}
	
	public void stop() throws JMSException {
		if (_session != null) {
			_session.close();
			_session = null;
			_connection.stop();
		}
	}
}
