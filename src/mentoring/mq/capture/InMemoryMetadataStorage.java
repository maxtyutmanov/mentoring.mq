package mentoring.mq.capture;

import java.util.HashSet;

import mentoring.mq.common.ProcessedDocument;

public class InMemoryMetadataStorage implements MetadataStorage {
	private HashSet<String> _sentFileHashes;
	
	public InMemoryMetadataStorage() {
		_sentFileHashes = new HashSet<String>();
	}
	
	public void markAsSent(ProcessedDocument doc) {
		String hs = doc.getHashSum();
		_sentFileHashes.add(hs);
	}
	
	public boolean wasAlreadySent(ProcessedDocument doc) {
		String hs = doc.getHashSum();
		return _sentFileHashes.contains(hs);
	}
}
