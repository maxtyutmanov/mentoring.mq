package mentoring.mq.processing;

import mentoring.mq.common.ProcessedDocument;

public class DeduplicationFilter {
	public boolean filter(ProcessedDocument procDoc) {
		return true;
	}
}
