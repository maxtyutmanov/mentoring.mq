package mentoring.mq.common;

import org.apache.commons.codec.digest.DigestUtils;

public class ShaHashFunction implements HashFunction {
	public String evaluate(ProcessedDocument doc) {
		return DigestUtils.sha512Hex(doc.getContents());
	}
}