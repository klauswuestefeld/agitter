package agitter.ui.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.themes.BaseTheme;

public class AgitterVaadinUtils {

	public static final String DEFAULT_NATIVE_BUTTON_CLASS = "a-default-nativebutton";

	public static NativeButton createDefaultNativeButton(String text) {
		NativeButton button = new NativeButton(text);
		button.addStyleName(DEFAULT_NATIVE_BUTTON_CLASS);
		button.setSizeUndefined();
		return button;
	}

	public static Button createLinkButton(String caption) {
		Button b = new Button(caption);
		b.setSizeUndefined();
		b.setStyleName(BaseTheme.BUTTON_LINK);
	    return b;
	}

	public static Label createLabel(String caption) {
	    Label l = new Label(caption);
		l.setSizeUndefined();
	    return l;
	}
}
