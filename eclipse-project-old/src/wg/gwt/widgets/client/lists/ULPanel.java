package wg.gwt.widgets.client.lists;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class ULPanel extends ComplexPanel {

	private Element list;

	public ULPanel() {
		list = DOM.createElement("UL");
		setElement(list);
	}

	@Override
	public void add(Widget child) {
		Element li = DOM.createElement("LI");
		list.appendChild(li);
		super.add(child, li);
	}

}
