package mentoring.mq.fsw;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ReadFileContentsJob implements Runnable {
	
	//region CONSTS
	
	private final int QUEUE_SIZE = 500;
	private final int MAX_NUMBER_OF_RETRIES = 3;
	private final int MILLIS_TO_WAIT_BEFORE_RETRY = 200;
	
	//endregion CONSTS
	
	//region FIELDS
	
	private final BlockingQueue<Path> _pathsToProcess;
	private final FileContentsListener _listener;
	
	//endregion FIELDS
	
	//region CTOR
	
	public ReadFileContentsJob(FileContentsListener listener) {
		_pathsToProcess = new ArrayBlockingQueue<Path>(QUEUE_SIZE);
		_listener = listener;
	}
	
	//endregion CTOR
	
	//region METHODS
	
	public void enqueuePath(Path path) throws InterruptedException {
		_pathsToProcess.put(path);
	}
	
	//endregion METHODS

	//region Runnable implementation
	
	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				pollQueueForPath();
			}
		}
		catch (InterruptedException e) {
			//TODO: log
			e.printStackTrace();
		}
	}
	
	private void pollQueueForPath() throws InterruptedException {
		Path path = _pathsToProcess.take();
		System.out.println("got the file from processing queue: " + path.toString());
		
		int numberOfRetries = 0;
		
		while (true) {
			if (++numberOfRetries >= MAX_NUMBER_OF_RETRIES) {
				//file is unavailable for too long. Give up on it.
				System.out.println("Too many attempts were made to read file contents: " + path.toString());
				//TODO: log
				break;
			}

			try {
				byte[] contents = Files.readAllBytes(path);
				onFileContentsRead(path, contents);
				//file is (finally) read, returning
				break;
			}
			catch (FileNotFoundException fnfe) {
				//file has already been deleted. Give up on it.
				System.out.println("Cannot find the file taken from processing queue: " + path.toString());
				//TODO: log
				break;
			}
			catch (IOException ioe) {
				Thread.sleep(MILLIS_TO_WAIT_BEFORE_RETRY);
				continue;
			}
		}
	}
	
	//endregion Runnable implementation
	
	protected void onFileContentsRead(Path filePath, byte[] contents) {
		if (_listener != null) {
			_listener.onContentsRead(new FileContents(filePath, contents));
		}
	}
}
