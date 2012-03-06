package agitter.ui.presenter;

import agitter.domain.comments.Comment;
import agitter.domain.comments.Comments;
import agitter.domain.users.User;
import agitter.ui.view.session.events.CommentsView;


class CommentsPresenter implements CommentsView.Boss {

	private final CommentsView view;
	private final User user;
	private final Comments comments;
	private Object object;

	CommentsPresenter(User user, Comments comments, CommentsView view) {
		this.user = user;
		this.comments = comments;
		this.view = view;
		
		this.view.startReportingTo(this);
	}

	
	@Override
	public void onCommentPosted(String comment) {
		comments.commentOn(object, user, comment);
		refresh();
	}


	void periodicRefresh() {
		refresh();
	}


	void setObject(Object obj) {
		object = obj;
		refresh();
	}


	private void refresh() {
		if (object == null) {
			view.hide();
			return;
		}
		view.show();

		view.clearCommentList();
		for (Comment c : comments.commentsFor(object))
			view.addComment(c.owner().screenName(), ""+c.creationDatetime(), c.text());
		view.show();
	}

}
