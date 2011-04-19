package agitter.util;

import java.util.Date;

public class ClockDummyImpl implements Clock {

	private long _milis;

	public long getMilis() {
		return _milis;
	}
	public void setMilis(long milis) {
		this._milis = milis;
	}

	@Override
	public Date date() {
		if(_milis==0) {
			return new Date();
		}
		return new Date(_milis);
	}
}
