package mentoring.mq.common;

public class ProcessedDocument {
	private final String _remoteFilePath;
	private final byte[] _contents;
	
	public String getRemoteFilePath() {
		return _remoteFilePath;
	}
	
	public byte[] getContents() {
		return _contents;
	}
	
	public ProcessedDocument(String remoteFilePath, byte[] contents) {
		this._remoteFilePath = remoteFilePath;
		this._contents = contents;
	}
}
