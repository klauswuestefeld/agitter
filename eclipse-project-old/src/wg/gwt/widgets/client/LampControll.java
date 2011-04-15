package wg.gwt.widgets.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.TextBoxBase;

class LampControll implements FocusHandler, BlurHandler {
	
	private String _defaultText = null;
	private TextBoxBase _textBox;
	
	public static LampControll control(TextBoxBase textBoxBase) {
		return new LampControll(textBoxBase);
	}
	
	private LampControll(TextBoxBase textBoxBase) {
		initialize(textBoxBase);
		setStyle();
	}

	@Override
	public void onFocus(FocusEvent event) {
		clearIfDefaultText();
		setStyle();
	}

	@Override
	public void onBlur(BlurEvent event) {
		putDefaultTextIfEmpty();
		setStyle();
	}

	private void initialize(TextBoxBase textBoxBase) {
		_textBox = textBoxBase;
		_defaultText = _textBox.getText();
		
		_textBox.addFocusHandler(this);
		_textBox.addBlurHandler(this);
	}
	
	private void clearIfDefaultText() {
		if (_textBox.getText().equals(_defaultText))
			_textBox.setText("");
	}
	
	private void putDefaultTextIfEmpty() {
		if (_textBox.getText().equals(""))
			_textBox.setText(_defaultText);
	}
	
	private void setStyle() {
		if (_textBox.getText().equals(_defaultText))
			_textBox.addStyleName("lamp");
		else
			_textBox.removeStyleName("lamp");
	}
	
	public String getText() {
		if (_textBox.getText().equals(_defaultText))
			return "";
		
		return _textBox.getText();
	}

	public String getDefaultText(){
		return _defaultText;
	}
	
	public void resetText() {
		_textBox.setText(_defaultText);
		setStyle();
	}

}
