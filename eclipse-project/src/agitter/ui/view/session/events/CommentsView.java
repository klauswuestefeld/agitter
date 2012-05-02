package agitter.ui.view.session.events;


public interface CommentsView {
	
	public static final boolean COMMENTS_ENABLED = true;
	
	interface Boss {
		void onCommentPosted(String text);
		boolean onNameGiven(String name);
	}
	
	void startReportingTo(Boss boss);

	void show();
	void hide();

	void clearCommentBox();
	void clearCommentList();
	void addComment(String user, String date, String text);
	void askForName();
	
}
