package wg.gwt.widgets.client;

import com.google.gwt.user.client.ui.TextBox;

public class LampTextBox extends TextBox {
	private LampControll _control = null;

	@Override
	protected void onLoad() {
		_control = LampControll.control(this);
	}
	
	public String getTextCleaned() {
		return _control.getText();
	}
	
	public String getOriginalText() {
		return super.getText();
	}
	
	public void resetText() {
		_control.resetText();
	}
}
