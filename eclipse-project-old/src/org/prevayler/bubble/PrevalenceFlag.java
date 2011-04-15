package org.prevayler.bubble;


class PrevalenceFlag {

	private static boolean _isInsidePrevalence = false;

	static boolean isInsidePrevalence() {
		return _isInsidePrevalence;
	}

	static void setInsidePrevalence(boolean newValue) {
		if (_isInsidePrevalence == newValue) throw new IllegalStateException();
		_isInsidePrevalence = newValue;
	}

}
