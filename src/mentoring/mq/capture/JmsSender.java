package mentoring.mq.capture;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import mentoring.mq.fsw.FileContents;

public class JmsSender implements java.io.Closeable {
	private final String PROCESSING_QUEUE_NAME = "MENTORING.MQ.PROCESSING_QUEUE";
	
	private final ConnectionFactory _factory;
	private final Connection _connection;
	
	public JmsSender() throws JMSException {
		String user = "admin";
        String password = "password";
        String host = "localhost";
        int port = 61616;
		
		_factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + String.valueOf(port));
		_connection = _factory.createConnection(user, password);
		_connection.start();
	}
	
	public void sendFileToProcessing(FileContents file) throws JMSException {
		Session session = null;
		MessageProducer producer = null;
		
		try {
			session = _connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination dest = session.createQueue(PROCESSING_QUEUE_NAME);
			producer = session.createProducer(dest);
			MapMessage mapMsg = session.createMapMessage();
			fillMessage(mapMsg, file);
			producer.send(mapMsg);
		} 
		finally {
			if (producer != null) {
				producer.close();
			}
			
			if (session != null) {
				session.close();
			}
		}
	}
	
	private void fillMessage(MapMessage msg, FileContents payload) throws JMSException {
		msg.setString("filepath", payload.getPath());
		msg.setBytes("contents", payload.getContents());
	}

	@Override
	public void close() throws IOException {
		if (_connection != null) {
			try {
				_connection.close();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
