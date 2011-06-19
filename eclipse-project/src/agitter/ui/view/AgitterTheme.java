package agitter.ui.view;

import com.vaadin.ui.NativeButton;

public class AgitterTheme {

	public static final String DEFAULT_LOGO_COLOR_CLASS = "a-default-logo-color";
	public static final String DEFAULT_NATIVE_BUTTON_CLASS = "a-default-nativebutton";

	public static NativeButton createDefaultNativeButton(String text) {
		NativeButton button = new NativeButton(text);
		button.addStyleName(DEFAULT_NATIVE_BUTTON_CLASS);
		button.setSizeUndefined();
		return button;
	}

}
