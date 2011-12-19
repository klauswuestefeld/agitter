package agitter.main.spike;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class MultiTabWindow extends Window {
	
	public MultiTabWindow() {
		setCaption( "This is a window " + this.getName() );
		
		final TextField textField = new TextField("fruta", "abacate");
		this.getContent().addComponent(textField );
		Button submitButton = new Button("Submit");
		submitButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getContent().addComponent(new Label(textField.getValue().toString()));
			}
		});
		this.getContent().addComponent(submitButton);
	}
	
	@Override
	public void setName(String name) throws IllegalStateException {
		super.setName(name);
		setCaption(name);
	}

}
