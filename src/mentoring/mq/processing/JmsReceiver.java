package mentoring.mq.processing;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import mentoring.mq.common.JmsAdapterBase;

public class JmsReceiver extends JmsAdapterBase {
	private final String PROCESSING_QUEUE_NAME = "MENTORING.MQ.PROCESSING_QUEUE";

	private Session _session;
	
	public JmsReceiver() throws JMSException {
		super();
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
