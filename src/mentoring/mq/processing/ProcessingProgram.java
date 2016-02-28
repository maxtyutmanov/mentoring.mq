package mentoring.mq.processing;

import java.nio.file.Paths;

public class ProcessingProgram {
	public static void main(String[] args) throws Exception {
		DocumentProcessor processor = new DocumentProcessor(Paths.get("D:\\garbage\\file_storage"));
		
		JmsReceiver receiver = new JmsReceiver();
		receiver.start(processor);
		
		System.out.println("Receiver is up and ready to process messages...");
		
		System.in.read();
		
		receiver.stop();
		receiver.close();
	}
}
