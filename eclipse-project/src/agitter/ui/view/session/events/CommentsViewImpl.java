package agitter.ui.view.session.events;

import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;

class CommentsViewImpl extends CssLayout implements CommentsView {
	
	private final Label commentLabel = new Label("Comentários:"); 
	private final TextArea comment = new TextArea();
	private final NativeButton commentButton = AgitterVaadinUtils.createDefaultAddButton();
	private final Label commentList = new Label();

	private Boss boss;

	
	{
		addComponent(commentLabel);
		
		comment.setNullRepresentation("");
		comment.setInputPrompt("O que Achou?");
		comment.setSizeUndefined();
		
		addComponent(comment);
		comment.addStyleName("a-new-comment");
		
		commentButton.addStyleName("a-comment-post-ignored");
		addComponent(commentButton);
		
		addComponent(commentList);
	}
	
	
	@Override
	public void startReportingTo(Boss b) {
		if (this.boss != null) throw new IllegalStateException();
		this.boss = b;
		commentButton.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			boss.onCommentPosted((String)comment.getValue());
			comment.setValue("");
		}});
	}

	
	@Override public void hide() { setVisibility(false); }
	@Override public void show() { if ( COMMENTS_ENABLED ) setVisibility(true); }


	private void setVisibility(boolean v) {
		commentLabel.setVisible(v); 
		comment.setVisible(v);
		commentButton.setVisible(v);
		commentList.setVisible(v);
	}
	
	
	@Override
	public void addComment(String user, String date, String text) {
		commentList.setValue(commentList.getValue() + "--------- Usuário: " + user + " Data: " + date + " Texto: " + text);
	}


	@Override
	public void clearCommentList() {
		commentList.setValue("");
	}
	
}
