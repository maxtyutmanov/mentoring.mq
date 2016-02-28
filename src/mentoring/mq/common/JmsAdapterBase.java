package mentoring.mq.common;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;

public abstract class JmsAdapterBase implements java.io.Closeable {
	protected final ConnectionFactory _factory;
	protected final Connection _connection;
	
	protected JmsAdapterBase() throws JMSException {
		String user = "admin";
        String password = "password";
        String host = "localhost";
        int port = 61616;
		
		_factory = new ActiveMQConnectionFactory("tcp://" + host + ":" + String.valueOf(port));
		_connection = _factory.createConnection(user, password);
		_connection.start();
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
