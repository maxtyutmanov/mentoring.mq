package mentoring.mq.fsw;

import java.io.Serializable;
import java.nio.file.Path;

public class FileContents implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String _path;
	private final byte[] _contents;
	
	public String getPath() {
		return _path;
	}
	
	public byte[] getContents() {
		return _contents;
	}
	
	public FileContents(Path path, byte[] contents) {
		this._path = path.toString();
		this._contents = contents;
	}
}
