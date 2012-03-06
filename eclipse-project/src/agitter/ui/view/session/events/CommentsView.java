package agitter.ui.view.session.events;


public interface CommentsView {
	
	public static final boolean COMMENTS_ENABLED = false;
	
	interface Boss {
		void onCommentPosted(String text);
	}
	
	void startReportingTo(Boss boss);

	void show();
	void hide();

	void clearCommentList();
	void addComment(String user, String date, String text);
	
}
