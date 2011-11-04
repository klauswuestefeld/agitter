package agitter.uiold.view;

import com.vaadin.ui.NativeButton;

public class AgitterVaadinUtils {

	public static final String DEFAULT_NATIVE_BUTTON_CLASS = "a-default-nativebutton";
	public static final String DEFAULT_ADD_BUTTON_CLASS = "a-default-add-button";

	public static NativeButton createDefaultNativeButton(String text) {
		NativeButton button = new NativeButton(text);
		button.addStyleName(DEFAULT_NATIVE_BUTTON_CLASS);
		button.setSizeUndefined();
		return button;
	}

	public static NativeButton createDefaultAddButton() {
		NativeButton button = new NativeButton();
		button.addStyleName(DEFAULT_ADD_BUTTON_CLASS);
		button.setSizeUndefined();
		return button;
	}

}
