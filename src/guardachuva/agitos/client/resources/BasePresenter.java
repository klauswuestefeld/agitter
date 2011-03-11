package guardachuva.agitos.client.resources;

import guardachuva.agitos.client.IController;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public abstract class BasePresenter {
	
	protected final IController _controller;
	
	public BasePresenter(IController controller) {
		_controller = controller;
	}

	public String getEmailLogado() {
		return _controller.getUserMail();
	}
	
	public static String dateToStr(Date date) {
		return DateTimeFormat.getFormat("dd/MM/yyyy HH:mm").format(date);
	}
	
	public static Date strToDate(String date, String time) {
		try {
			return DateTimeFormat.getFormat("dd/MM/yyyy HH:mm").parse(date + " " + time);
		} catch(Exception e) {
			return null;
		}
	}

	public static DateTimeFormat getDateFormat() {
		return DateTimeFormat.getFormat("dd/MM/yyyy");
	}

}
