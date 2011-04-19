
package agitter.util;

import org.prevayler.Clock;

public class PrevaylerClockToAgitterClockAdapter implements AgitterClock {
	
	private final Clock _prevaylerClock;
	
	public PrevaylerClockToAgitterClockAdapter(final Clock prevaylerClock) {
		this._prevaylerClock = prevaylerClock;
	}

	@Override
	public long datetime() {
		return this._prevaylerClock.time().getTime();
	}

}
