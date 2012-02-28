package vaadinutils;


import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.themes.BaseTheme;

public class WidgetUtils {

	static public void focusOrder(Component.Focusable... components) {
		for(int i = 0; i < components.length; ++i)
			components[ i ].setTabIndex( i + 1 );
	}

	public static Button createLinkButton(String caption) {
		Button b = new Button(caption);
		b.setSizeUndefined();
		b.setStyleName(BaseTheme.BUTTON_LINK);
	    return b;
	}

	public static Label createLabel() {
	    return createLabel(null);
	}

	public static Label createLabel(String caption) {
	    Label l = caption != null ? new Label(caption) : new Label();
		l.setSizeUndefined();
	    return l;
	}

	public static Label createLabelXHTML(String caption) {
		Label l = createLabel(caption);
		l.setContentMode(Label.CONTENT_XHTML);
		return l;
	}
	
	public static ProgressIndicator createPoller(int millisToNextRefresh) {
		ProgressIndicator result = new ProgressIndicator();
		result.setPollingInterval(millisToNextRefresh);
		result.setWidth("0px");
		result.setHeight("0px");
		return result;
	}

}
