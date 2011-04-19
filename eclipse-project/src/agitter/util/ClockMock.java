package agitter.util;


import java.util.Date;

public class ClockMock implements Clock {

	private long _datetime;

	public void setDatetime(long milis) {
		_datetime = milis;
	}

	@Override
	public long datetime() {
		return _datetime;
	}
	@Override
	public Date time() {
		return new Date(_datetime);
	}
}
