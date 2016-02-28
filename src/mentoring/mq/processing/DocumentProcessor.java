package mentoring.mq.processing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

public class DocumentProcessor implements MessageListener {
	
	private final Path _storageDir;
	
	public DocumentProcessor(Path storageDir) {
		this._storageDir = storageDir;
	}

	@Override
	public void onMessage(Message msg) {
		System.out.println("Got the message from broker!");
		
		if (msg instanceof MapMessage) {
			MapMessage msgAs = (MapMessage)msg;
			
			try {
				String filePath = msgAs.getString("filepath");
				byte[] contents = msgAs.getBytes("contents");
				
				sendToStorage(filePath, contents);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Could not cast input message to MapMessage");
		}
	}
	
	private void sendToStorage(String remotePathStr, byte[] fileContents) {
		Path remotePath = Paths.get(remotePathStr);
		Path remoteFileName = remotePath.getFileName();
		
		Path targetPath = _storageDir.resolve(remoteFileName);
		
		try {
			Files.write(targetPath, fileContents);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
