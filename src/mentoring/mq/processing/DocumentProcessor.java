package mentoring.mq.processing;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import mentoring.mq.fsw.FileContents;

public class DocumentProcessor implements MessageListener {
	
	private final Path _storageDir;
	
	public DocumentProcessor(Path storageDir) {
		this._storageDir = storageDir;
	}

	@Override
	public void onMessage(Message msg) {
		System.out.println("Got the message from broker!");
		
		if (msg instanceof ObjectMessage) {
			ObjectMessage msgAs = (ObjectMessage)msg;
			
			Serializable obj;
			try {
				obj = msgAs.getObject();
				
				if (obj instanceof FileContents) {
					FileContents objAs = (FileContents)obj;
					sendToStorage(objAs);
				}
				else {
					System.out.println("Could not cast object within the message to FileContents object");
				}
				
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			System.out.println("Could not cast input message to ObjectMessage");
		}
	}
	
	private void sendToStorage(FileContents fileContents) {
		Path remotePath = Paths.get(fileContents.getPath());
		Path remoteFileName = remotePath.getFileName();
		
		Path targetPath = _storageDir.resolve(remoteFileName);
		
		try {
			Files.write(targetPath, fileContents.getContents());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
