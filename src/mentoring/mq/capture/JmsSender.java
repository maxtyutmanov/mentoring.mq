package mentoring.mq.capture;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import mentoring.mq.common.JmsAdapterBase;
import mentoring.mq.common.ProcessedDocument;
import mentoring.mq.fsw.FileContents;

public class JmsSender extends JmsAdapterBase {
	private final String PROCESSING_QUEUE_NAME = "MENTORING.MQ.PROCESSING_QUEUE";
	
	public JmsSender() throws JMSException {
		super();
	}
	
	public void sendFileToProcessing(ProcessedDocument file) throws JMSException {
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
	
	private void fillMessage(MapMessage msg, ProcessedDocument payload) throws JMSException {
		msg.setString("filepath", payload.getRemoteFilePath());
		msg.setBytes("contents", payload.getContents());
	}
}
