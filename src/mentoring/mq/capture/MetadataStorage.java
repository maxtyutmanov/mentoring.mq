package mentoring.mq.capture;

import mentoring.mq.common.ProcessedDocument;

public interface MetadataStorage {
	void markAsSent(ProcessedDocument doc);
	boolean wasAlreadySent(ProcessedDocument doc);
}
