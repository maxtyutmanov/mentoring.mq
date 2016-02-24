package mentoring.mq.fsw;

import java.nio.file.Path;

public interface RawFsEventsListener {
	void fileCreated(Path absolutePath);
}
