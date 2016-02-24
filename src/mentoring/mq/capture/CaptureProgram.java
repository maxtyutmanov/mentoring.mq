package mentoring.mq.capture;
import java.nio.file.Paths;

import javax.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class CaptureProgram {
	public static void main(String[] args) throws Exception {
		CaptureAgent ca = new CaptureAgent();
		
		ca.start(Paths.get("D:\\фотки\\жигули"));
		
		System.out.println("Capture agent started.");
		
		System.in.read();
		
		System.out.println("Stopping the capture agent...");
		
		ca.stop();
		
		System.out.println("Capture agent has successfully been interrupted");
	}
}
