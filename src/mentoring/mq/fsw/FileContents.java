package mentoring.mq.fsw;

import java.nio.file.Path;

public class FileContents {
	
	private final Path _path;
	private final byte[] _contents;
	
	public Path getPath() {
		return _path;
	}
	
	public byte[] getContents() {
		return _contents;
	}
	
	public FileContents(Path path, byte[] contents) {
		this._path = path;
		this._contents = contents;
	}
}
