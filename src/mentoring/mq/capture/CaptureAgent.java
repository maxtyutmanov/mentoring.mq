package mentoring.mq.capture;

import java.io.IOException;
import java.nio.file.Path;

import javax.jms.JMSException;

import mentoring.mq.common.HashFunction;
import mentoring.mq.common.ProcessedDocument;
import mentoring.mq.common.ShaHashFunction;
import mentoring.mq.fsw.FileContents;
import mentoring.mq.fsw.FileContentsListener;
import mentoring.mq.fsw.FileSystemWatcher;

public class CaptureAgent implements java.io.Closeable {
	private FileSystemWatcher _watcher = null;
	private final HashFunction _hashFunction = new ShaHashFunction();
	private final JmsSender _jmsAdapter;
	private final MetadataStorage _metadataStorage = new InMemoryMetadataStorage();
	
	public CaptureAgent() throws JMSException {
		_jmsAdapter = new JmsSender();
	}
	
	public void start(Path dirToCapture) throws IOException {
		_watcher = new FileSystemWatcher(dirToCapture, new FileContentsListener() {
			@Override
			public void onContentsRead(FileContents contents) {
				System.out.println("Got the file!" + contents.getPath().toString());
				try {
					ProcessedDocument doc = new ProcessedDocument(_hashFunction, contents.getPath(), contents.getContents());
					
					if (!_metadataStorage.wasAlreadySent(doc))
					{
						_jmsAdapter.sendFileToProcessing(doc);
						_metadataStorage.markAsSent(doc);
					}
				} 
				catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void stop() throws IOException {
		if (_watcher != null) {
			_watcher.close();
			_watcher = null;
		}
	}

	@Override
	public void close() throws IOException {
		this.stop();
		_jmsAdapter.close();
	}
}
