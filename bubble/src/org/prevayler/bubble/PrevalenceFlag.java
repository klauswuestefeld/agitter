package org.prevayler.bubble;


class PrevalenceFlag {

	private static ThreadLocal<Boolean> _isInsidePrevalence = new ThreadLocal<Boolean>() {  @Override protected Boolean initialValue() {
		return false;
	}};

	static boolean isInsidePrevalence() {
		return _isInsidePrevalence.get();
	}

	static void setInsidePrevalence(boolean newValue) {
		if (newValue == _isInsidePrevalence.get()) throw new Error("Prevalence Flag being set redundantly.");
		_isInsidePrevalence.set(newValue);
	}

}
