package wg.gwt.widgets.client;

import com.google.gwt.user.client.ui.TextArea;

public class LampTextArea extends TextArea {
	private LampControll _control;

	@Override
	protected void onLoad() {
		_control = LampControll.control(this);
	}

	public String getTextCleaned() {
		return _control.getText();
	}

	public void resetText() {
		_control.resetText();
	}
}
