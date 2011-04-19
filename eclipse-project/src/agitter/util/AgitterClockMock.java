
package agitter.util;


public class AgitterClockMock implements AgitterClock {

	private long _datetime;

	public void setDatetime(long milis) {
		_datetime = milis;
	}

	@Override
	public long datetime() {
		return _datetime;
	}

}
