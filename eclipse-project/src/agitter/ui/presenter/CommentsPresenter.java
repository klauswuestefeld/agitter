package agitter.ui.presenter;

import java.text.SimpleDateFormat;

import agitter.domain.comments.Comment;
import agitter.domain.comments.Comments;
import agitter.domain.users.User;
import agitter.ui.view.session.events.CommentsView;


class CommentsPresenter implements CommentsView.Boss {
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm");

	private final CommentsView view;
	private final User user;
	private final Comments comments;
	private Object object;

	CommentsPresenter(User user, Comments comments, CommentsView view) {
		if (comments == null) throw new IllegalStateException();
		this.user = user;
		this.comments = comments;
		this.view = view;
		
		this.view.startReportingTo(this);
	}

	
	@Override
	public void onCommentPosted(String comment) {
		if( user.hasName() ) {
			comments.commentOn(object, user, comment);
			view.clearCommentBox();
			refresh();
		}else {
			view.askForName();
		}
	}
	
	@Override
	public boolean onNameGiven(String name) {
		user.setName( name );
		//MMM O menú de cima precisa se atualizar agora que o usuário tem um nome...
		return user.hasName();
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
			view.addComment(c.owner().screenName(), DATE_FORMAT.format(c.creationDatetime()), c.text());
		view.show();
	}	
	
}

