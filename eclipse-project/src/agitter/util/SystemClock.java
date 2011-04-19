package agitter.util;

import java.util.Date;

public class SystemClock implements Clock {
	@Override
	public long datetime() {
		return new Date().getTime();
	}
}
