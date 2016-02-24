package mentoring.mq.capture;

import java.io.IOException;
import java.nio.file.Path;

import mentoring.mq.fsw.FileContents;
import mentoring.mq.fsw.FileContentsListener;
import mentoring.mq.fsw.FileSystemWatcher;

public class CaptureAgent {
	private FileSystemWatcher _watcher = null;
	
	public void start(Path dirToCapture) throws IOException {
		_watcher = new FileSystemWatcher(dirToCapture, new FileContentsListener() {
			@Override
			public void onContentsRead(FileContents contents) {
				System.out.println("Got the file!" + contents.getPath().toString());
			}
		});
	}
	
	public void stop() throws IOException {
		if (_watcher != null) {
			_watcher.close();
			_watcher = null;
		}
	}
}
