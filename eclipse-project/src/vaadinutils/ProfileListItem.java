package vaadinutils;

public class ProfileListItem {
	public String key;
	public String caption;
	public String icon;
	
	public ProfileListItem(String key, String caption, String icon) {
		super();
		this.key = key;
		this.caption = caption;
		this.icon = icon;
	}
	
	public ProfileListItem(String key, String caption) {
		super();
		this.key = key;
		this.caption = caption;
	}
	
	public ProfileListItem(String key) {
		super();
		this.key = key;
	}
	
}