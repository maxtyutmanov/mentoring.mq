package mentoring.mq.fsw;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FileSystemWatcher implements Closeable {
	//region FIELDS
	
	private final WatchService _watchService;
	private final WatchKey _watchKey;
	private final Thread _fsEventsPoller;
	private final Thread _fileContentsReader;
	
	//endregion FIELDS
	
	//region CTOR
	
	public FileSystemWatcher(Path dirToWatch, FileContentsListener listener) throws IOException {
		_watchService = FileSystems.getDefault().newWatchService();
		_watchKey = dirToWatch.register(_watchService, StandardWatchEventKinds.ENTRY_CREATE);
		
		final ReadFileContentsJob readContentsJob = new ReadFileContentsJob(listener);
		final PollFsEventsJob pollJob = new PollFsEventsJob(_watchService, dirToWatch, new RawFsEventsListener() {
			
			@Override
			public void fileCreated(Path absolutePath) {
				try {
					readContentsJob.enqueuePath(absolutePath);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		_fsEventsPoller = new Thread(pollJob);
		_fileContentsReader = new Thread(readContentsJob);
		
		_fsEventsPoller.start();
		_fileContentsReader.start();
	}
	
	//endregion CTOR
	
	//region Closeable implementation

	@Override
	public void close() throws IOException {
		if (_watchKey != null) {
			_watchKey.cancel();
		}
		
		_fsEventsPoller.interrupt();
		try
		{
			_fsEventsPoller.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		_fileContentsReader.interrupt();
		try {
			_fileContentsReader.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (_watchService != null) {
			_watchService.close();
		}
	}
	
	//endregion region Closeable implementation
}
