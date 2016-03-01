package mentoring.mq.common;

public class ProcessedDocument {
	private HashFunction _hashFunction;
	
	private final String _remoteFilePath;
	private final byte[] _contents;
	private String _hashSum;
	
	public String getRemoteFilePath() {
		return _remoteFilePath;
	}
	
	public byte[] getContents() {
		return _contents;
	}
	
	public String getHashSum() {
		if (_hashSum == null) {
			_hashSum = _hashFunction.evaluate(this);
		}
		
		return _hashSum;
	}
	
	public ProcessedDocument(HashFunction hashFunction, String remoteFilePath, byte[] contents) {
		this._hashFunction = hashFunction;
		this._remoteFilePath = remoteFilePath;
		this._contents = contents;
	}
}
