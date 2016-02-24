package mentoring.mq.fsw;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Observer;

public class PollFsEventsJob implements Runnable {

	//region FIELDS
	
	private final WatchService _watchService;
	private final Path _pathToWatch;
	private final RawFsEventsListener _listener;
	
	//endregion FIELDS
	
	//region CTOR
	
	public PollFsEventsJob(WatchService watchService, Path pathToWatch, RawFsEventsListener listener) {
		this._watchService = watchService;
		this._pathToWatch = pathToWatch;
		this._listener = listener;
	}
	
	//endregion CTOR
	
	//region METHODS
	
	public void stop() {
		
	}
	
	//endregion METHODS
	
	//region Runnable implementation
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				pollWatchService();
			}
		}
		catch (InterruptedException e) {
			//TODO: log
			e.printStackTrace();
		}
	}
	
	private void pollWatchService() throws InterruptedException {
		WatchKey key = this._watchService.take();
			
		for (WatchEvent<?> event: key.pollEvents()) {
			WatchEvent.Kind kind = event.kind();
			
			if (kind == StandardWatchEventKinds.OVERFLOW) {
                continue;
            }
			
			WatchEvent<Path> eventAs = (WatchEvent<Path>)event;
			Path relativePath = eventAs.context();
			Path absolutePath = _pathToWatch.resolve(relativePath);
			
			boolean isRegularFile = Files.isRegularFile(absolutePath);
			if (isRegularFile) {
				onFileCreated(absolutePath);
			}
		}
	}
	
	protected void onFileCreated(Path absolutePath) {
		if (_listener != null) {
			_listener.fileCreated(absolutePath);
		}
	}
	
	//endregion Runnable implementation
}
