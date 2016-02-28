package mentoring.mq.common;

import org.apache.commons.codec.digest.DigestUtils;

public class HashFunction {
	public String project(ProcessedDocument doc) {
		return DigestUtils.sha512Hex(doc.getContents());
	}
}