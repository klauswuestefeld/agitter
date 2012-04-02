package vaadinutils;

import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;

public class ElipsisButton extends Button {
	private boolean closed;
	private ClickListener openListener;
	private ClickListener closeListener;
	 
	public ElipsisButton() {
		super();
		
		closed = true;
		updateStyle();
		
		addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
				onButtonClick(event);
		}});
	}
	
	private void onButtonClick(ClickEvent event) {
		toggle();
		
		if (closed && closeListener != null) {
			closeListener.buttonClick(event);
		}
		if (!closed && openListener != null) {
			openListener.buttonClick(event);
		}			
	}
	
	private void updateStyle() {
		setSizeUndefined();
		setStyleName(BaseTheme.BUTTON_LINK);
		
		if (closed) {
			addStyleName("elipsis-closed");
			setCaption("Abrir o restante");
			setDescription("Abre a lista");
		} else {
			addStyleName("elipsis-opened");
			setCaption("Fechar lista");
			setDescription("Fecha a Lista");
		}
	}
	
	public void setOpenListener(ClickListener listener) {
		openListener = listener;
	}
	
	public void setCloseListener(ClickListener listener) {
		closeListener = listener;
	}

	public void close() {
		closed = true;
		updateStyle();
	}
	
	public void open() {
		closed = false;
		updateStyle();
	}
	
	public void toggle() {
		closed = !closed;
		updateStyle();
	}

	public boolean isClosed() {
		return closed;
	}
}
