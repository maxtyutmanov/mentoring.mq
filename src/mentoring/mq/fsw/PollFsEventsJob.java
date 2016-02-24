package mentoring.mq.fsw;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

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
			System.out.println("Exiting the poll job");
		}
		catch (InterruptedException e) {
			//TODO: log
			e.printStackTrace();
		}
	}
	
	private void pollWatchService() throws InterruptedException {
		WatchKey key = null;
		
		try {
			key = this._watchService.take();
			System.out.println("Got the events for path!");
				
			for (WatchEvent<?> event: key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				System.out.println("Looping through events list");
				
				if (kind == StandardWatchEventKinds.OVERFLOW) {
	                continue;
	            }
				
				WatchEvent<Path> eventAs = (WatchEvent<Path>)event;
				Path relativePath = eventAs.context();
				Path absolutePath = _pathToWatch.resolve(relativePath);
				
				System.out.println("File found" + absolutePath.toString());
				
				boolean isRegularFile = Files.isRegularFile(absolutePath);
				if (isRegularFile) {
					System.out.println("Regular file found" + absolutePath.toString());
					onFileCreated(absolutePath);
				}
			}
		}
		finally {
			if (key != null) {
				key.reset();
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
