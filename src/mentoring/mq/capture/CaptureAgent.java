package mentoring.mq.capture;

import java.io.IOException;
import java.nio.file.Path;

import javax.jms.JMSException;

import mentoring.mq.fsw.FileContents;
import mentoring.mq.fsw.FileContentsListener;
import mentoring.mq.fsw.FileSystemWatcher;

public class CaptureAgent implements java.io.Closeable {
	private FileSystemWatcher _watcher = null;
	private final JmsAdapter _jmsAdapter;
	
	public CaptureAgent() throws JMSException {
		_jmsAdapter = new JmsAdapter();
	}
	
	public void start(Path dirToCapture) throws IOException {
		_watcher = new FileSystemWatcher(dirToCapture, new FileContentsListener() {
			@Override
			public void onContentsRead(FileContents contents) {
				System.out.println("Got the file!" + contents.getPath().toString());
				try {
					_jmsAdapter.sendFileToProcessing(contents);
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
