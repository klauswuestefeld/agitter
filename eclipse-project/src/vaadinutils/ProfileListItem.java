package vaadinutils;

public class ProfileListItem {
	public int level;
	public String key;
	public String caption;
	public String icon;
	
	public ProfileListItem(String key, String caption, String icon) {
		super();
		this.key = key;
		this.caption = caption;
		this.icon = icon;
		this.level = 0;
	}
	
	public ProfileListItem(String key, String caption, String icon, int level) {
		super();
		this.key = key;
		this.caption = caption;
		this.icon = icon;
		this.level = level;
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
	
	public String toHTML() {	
		if (level > 5) level = 5;
		
		if (caption == null || caption.isEmpty()) {
			return "<span class='padding-"+level+"'>" + key + "</span>"; 
		}
		
		if (icon == null)
			return "<span class='a-remov-elem-list-element-value padding-"+level+"'>" + caption + "</span>" +
				   "<span class='a-remov-elem-list-element-key'>" + key + "</span>";
		else 
			return "<img src='" + icon + "' class='v-icon v-icon-list padding-"+level+"'/>" +  
				   "<span class='a-remov-elem-list-element-value'>" + caption + "</span>" +
			       "<span class='a-remov-elem-list-element-key'>" + key + "</span>";
	}
}