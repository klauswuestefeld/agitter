package spikes.vaadin;

import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class MultiTabCalc extends Application {

	// No per window state should be stored here

	@Override
	public void init() {
		// The first window is the main
		setMainWindow(new CalcWindow());

		// Building the window is separated from the application initialization
	}

	// Overriding getWindow(String) is required to get multiple browser
	// windows/tabs to work
	@Override
	public Window getWindow(String name) {

		// If the window is identified by name, we are good to go
		Window w = super.getWindow(name);

		// If not, we must create a new window for this new browser window/tab
		if (w == null) {
			w = new CalcWindow();

			// Use the random name given by the framework to identify this
			// window in future
			w.setName(name);
			addWindow(w);

			// Move to the url to remember the name in the future
			w.open(new ExternalResource(w.getURL()));
		}

		return w;
	}

	// Per window state and layout initialization is moved from the Application
	// to Window
	private class CalcWindow extends Window implements ClickListener {

		private double current = 0.0;
		private double stored = 0.0;
		private char lastOperationRequested = 'C';
		private final Label display = new Label("0.0");
		final GridLayout layout = new GridLayout(4, 5);

		CalcWindow() {

			setCaption("Calculator Application");
			setContent(layout);
			layout.addComponent(display, 0, 0, 3, 0);

			String[] operations = new String[] { "7", "8", "9", "/", "4", "5",
					"6", "*", "1", "2", "3", "-", "0", "=", "C", "+" };
			for (String caption : operations) {
				Button button = new Button(caption);
				button.addListener(this);
				layout.addComponent(button);
			}
		}

		@Override
		public void buttonClick(ClickEvent event) {

			Button button = event.getButton();
			char requestedOperation = button.getCaption().charAt(0);
			double newValue = calculate(requestedOperation);
			display.setValue(newValue);

		}

		private double calculate(char requestedOperation) {
			if ('0' <= requestedOperation && requestedOperation <= '9') {
				current = current * 10
						+ Double.parseDouble("" + requestedOperation);
				return current;
			}
			switch (lastOperationRequested) {
			case '+':
				stored += current;
				break;
			case '-':
				stored -= current;
				break;
			case '/':
				stored /= current;
				break;
			case '*':
				stored *= current;
				break;
			case 'C':
				stored = current;
				break;
			}
			lastOperationRequested = requestedOperation;
			current = 0.0;
			if (requestedOperation == 'C') {
				stored = 0.0;
			}
			return stored;
		}
	}

}