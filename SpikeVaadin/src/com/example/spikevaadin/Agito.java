package com.example.spikevaadin;


public class Agito {

	final private String _date;
	final private String _description;

	public Agito(String description, String date) {
		_description = description;
		_date = date;
	}

	public String description() {
		return _description;
	}

	public String date() {
		return _date;
	}

}
